
package tankrotationexample;



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


/**
 *
 * @author anthony-pc
 */

public class Bullet {

    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private boolean show;
    private Rectangle rect;

    private final int R = 3;

    private BufferedImage imgBullet;


    Bullet(int x, int y, int angle, BufferedImage imgBullet) {
        int radius = (7/2)*imgBullet.getWidth();
        this.angle = angle;
        //position shots in front of tank
        this.x = (x - imgBullet.getWidth()/2) + (radius * (int) Math.round(Math.cos(Math.toRadians(angle))));
        this.y = (y - imgBullet.getHeight()/2) + (radius * (int) Math.round(Math.sin(Math.toRadians(angle))));

        this.imgBullet = imgBullet;
        this.show = true;
        rect = new Rectangle(this.x, this.y, imgBullet.getWidth(), imgBullet.getHeight());

    }

    public void update() {      //move forward
        if(checkBorder() == false && show) {
            vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            x += vx;
            y += vy;
        } else {
            this.show = false;
        }
        rect.x = x;
        rect.y = y;
    }

    public Rectangle getRect(){
        return rect;
    }

    public boolean getShow(){ return this.show; }

    private boolean checkBorder() {     //remove bullet on edges
        if (x < 35) {
            return true;
        }
        if (x >= TRE.GAME_WIDTH - 55) {
            return true;
        }
        if (y < 35) {
            return true;
        }
        if (y >= TRE.GAME_HEIGHT - 45) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void drawImage(Graphics g){
        if(show) {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.imgBullet.getWidth() / 2.0, this.imgBullet.getHeight() / 2.0);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.imgBullet, rotation, null);
            g2d.drawRect(x,y, this.imgBullet.getWidth(), this.imgBullet.getHeight());
        }
    }


}
