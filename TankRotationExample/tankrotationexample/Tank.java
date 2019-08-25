package tankrotationexample;



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank{


    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;

    private final int R = 2;
    private final int ROTATIONSPEED = 4;




    private BufferedImage img;
    private BufferedImage imgBullet;
    ArrayList<Bullet> allBullets = new ArrayList<>();
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean FirePressed;
    private Rectangle rect;
    private long lastAttack;
    private long lastItem;
    private int HEALTH;
    private int dmg;
    private int lives;
    private int spawnX;
    private int spawnY;
    private int spawnA;
    private long cooldown = 1000;
    private int oldX;
    private int oldY;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img, BufferedImage imgBullet) {
        this.spawnX = x;
        this.spawnY = y;
        this.spawnA = angle;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.imgBullet = imgBullet;
        rect = new Rectangle(this.x, this.y, img.getWidth(), img.getHeight());
        this.HEALTH = 100;
        this.dmg = 25;
        this.lives = 2;
    }

    public int getX(){return oldX;}
    public int getY(){return oldY;}


    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleFirePressed(){ this.FirePressed = true; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleFirePressed() { this.FirePressed = false; }


    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.FirePressed) {
            this.shoot();
        }
        rect.x = x;
        rect.y = y;
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
        if(this.angle%360 == 0){
            this.angle = 0;
        }
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
        if(this.angle%360 == 0){
            this.angle = 0;
        }
    }

    private void moveBackwards() {
        oldX = this.x;
        oldY = this.y;
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
    }

    private void moveForwards() {
        oldX = this.x;
        oldY = this.y;
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }

    private void shoot(){       //shoot once per second
        long time = System.currentTimeMillis();
        if(time > lastAttack + cooldown) {
            allBullets.add(new Bullet(x+img.getWidth()/2,y+img.getHeight()/2,angle,imgBullet));
            lastAttack = time;
        }
    }



    public ArrayList<Bullet> getBulletList(){
        return allBullets;
    }

    public Rectangle getRect(){ return rect; }

    public void setHEALTH(int hp){
        this.HEALTH = hp;
    }

    public int getHEALTH(){
        return this.HEALTH;
    }



    public void damage(){                   // 25dmg per shot, 3 lives, respawn location
        this.HEALTH = this.HEALTH - dmg;
        if (this.HEALTH <= 0 && this.lives != 0){
            this.HEALTH = 100;
            this.lives--;
            this.x = spawnX;
            this.y = spawnY;
            this.angle = spawnA;
            this.cooldown = 1000;
        }
    }

    public void setDmg (int dam) {
        this.dmg = dam;
    }

    public int getDmg(){
        return this.dmg;
    }

    public void moveBack(){
        moveBackwards();
    }

    public int getLives() { return this.lives; }

    public void setLives(int l) { this.lives = l; }

    public void powerUp(int power) {                    //cooldown for power ups due to bug
        long time = System.currentTimeMillis();
        long cd = 1000;
        if (time > lastItem + cd) {
            if (power == 2) {
                this.cooldown = this.cooldown/2;
            } else if (power == 1) {
                if (this.HEALTH == 100) {
                    lives++;
                } else {
                    this.HEALTH = 100;
                }
            }
            lastItem = time;
        }
    }


    public BufferedImage updateCam(BufferedImage screen){       //for split screen 1st and 2nd half
        BufferedImage cam;
        int screenW = x - TRE.SCREEN_WIDTH/4;
        int screenH = y - TRE.SCREEN_HEIGHT/2;

        if(x - TRE.SCREEN_WIDTH/4 <= 0){ //left portion
            screenW = 0;
        }
        if(x + TRE.SCREEN_WIDTH/4 >= TRE.GAME_WIDTH ){
            screenW = TRE.GAME_WIDTH - TRE.SCREEN_WIDTH/2;
        }
        if(y - TRE.SCREEN_HEIGHT/2 <= 0){ //top portion
            screenH = 0;
        }
        if(y + TRE.SCREEN_HEIGHT/2 >= TRE.GAME_HEIGHT){
            screenH = TRE.GAME_HEIGHT - TRE.SCREEN_HEIGHT;
        }
        cam = screen.getSubimage(screenW,screenH,TRE.SCREEN_WIDTH/2,TRE.SCREEN_HEIGHT);
        return cam;
    }

    public void setX(int theX) {this.x = theX;}

    public void setY(int theY){this.y = theY;}

    public void changeX(int valueX){        //changeX based on wall
        this.x = this.x + valueX;
        this.rect.x = this.x;
    }

    public void changeY(int valueY){        //changeY based on wall
        this.y = this.y + valueY;
        this.rect.y = this.y;
    }


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.drawRect(x,y, this.img.getWidth(), this.img.getHeight());
    }



}
