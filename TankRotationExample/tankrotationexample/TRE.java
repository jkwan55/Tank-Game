/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel  {


    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int GAME_WIDTH = 1920;
    public static final int GAME_HEIGHT = 1080;
    private BufferedImage world;
    private BufferedImage bg;
    private BufferedImage bulletimg;
    private BufferedImage wall;
    private BufferedImage wall_break;
    private BufferedImage power1;
    private BufferedImage power2;
    ArrayList<Walls> allWalls = new ArrayList<>();
    ArrayList<PowerUp> allPowers = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    private Graphics2D buffer;
    private JFrame jf;
    private Tank t1;
    private Tank t2;
    private BufferedImage t2img;

    public static void main(String[] args) {
        Thread x;
        TRE trex = new TRE();
        trex.init();
        try {

            while (true) {

                trex.t1.update();
                trex.t2.update();
                trex.repaint();


                //System.out.println(trex.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }

    }


    private void init() {

        this.jf = new JFrame("Tank Rotation");
        this.world = new BufferedImage(TRE.GAME_WIDTH, TRE.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null;
        try {
            BufferedImage tmp;
            System.out.println(System.getProperty("user.dir"));
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */
            t1img = read(new File("resources/tank1.png"));
            t2img = read(new File("resources/tank2.png"));
            bg = read(new File("resources/Background.bmp"));
            bulletimg = read(new File("resources/Weapon.gif"));
            wall = read(new File("resources/Wall1.gif"));
            wall_break = read(new File("resources/WoodBox.gif"));
            power1 = read(new File("resources/power1.gif"));
            power2 = read(new File("resources/power2.gif"));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        t1 = new Tank(200, 200, 0, 0, 45, t1img, bulletimg);
        t2 = new Tank(GAME_WIDTH - 200, GAME_HEIGHT - 200, 0 ,0, 225 ,t2img, bulletimg);


        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.jf.setLayout(new BorderLayout());
        this.setBackground(Color.black);
        this.jf.add(this);


        this.jf.addKeyListener(tc1);
        this.jf.addKeyListener(tc2);


        this.jf.setSize(TRE.SCREEN_WIDTH, TRE.SCREEN_HEIGHT + 270);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);

        readMap(); // initial map

    }



    private void drawBackGround(){      //draw sand background tiles
        int TileWidth = bg.getWidth();
        int TileHeight = bg.getHeight();

        int NumberX = (int) (GAME_WIDTH / TileWidth);
        int NumberY = (int) (GAME_HEIGHT / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                buffer.drawImage(bg, j * TileWidth,
                        i * TileHeight, TileWidth,
                        TileHeight, null);
            }
        }
    }

    private void showBullets(Tank t) {
        if (!t.getBulletList().isEmpty()) {
            for (int i = 0; i < t.getBulletList().size(); i++) {
                if ((t.getBulletList().get(i)).getShow())
                    t.getBulletList().get(i).update();
                else
                    t.getBulletList().remove(i);
            }
        }
    }


    private void readMap() {        //read initial map, put into 2d array
        String file = "resources/map.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.isEmpty())
                    continue;
                ArrayList<Integer> rows = new ArrayList<>();
                String[] values = currentLine.trim().split(" ");
                for (String str : values) {
                    if (!str.isEmpty()) {
                        int num = Integer.parseInt(str);
                        rows.add(num);
                    }
                }
                map.add(rows);
            }
        } catch (IOException e) {
        }
    }

    public void mapUpdate(){    //print current map from 2d array
        allWalls.clear();
        int TileWidth = wall.getWidth();
        int TileHeight = wall.getHeight();

        int width = map.get(0).size();
        int height = map.size();
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map.get(j).get(i) == 1) {
                    allWalls.add(new Walls(i * TileWidth, j * TileHeight, TileWidth, TileHeight, false, wall, wall_break, buffer, i, j));
                } else if (map.get(j).get(i) == 2) {
                    allWalls.add(new Walls(i * TileWidth, j * TileHeight, TileWidth, TileHeight, true, wall, wall_break, buffer, i, j));
                } else if (map.get(j).get(i) == 3) {
                    allPowers.add(new PowerUp(i * TileWidth, j * TileHeight, TileWidth, TileHeight,power1,buffer,i,j, 1));
                } else if (map.get(j).get(i) == 4) {
                    allPowers.add(new PowerUp(i * TileWidth, j * TileHeight, TileWidth, TileHeight,power2,buffer,i,j, 2));
                }
            }
        }
    }

    private void collisionCheck(Tank t){

        if(t1.getRect().intersects(t2.getRect())){      //tank into tank
            Rectangle2D inter = t1.getRect().createIntersection(t2.getRect());
            if((inter.getMaxX() < t1.getRect().getMaxX()) && (inter.getHeight() >= inter.getWidth())){ // tank1 going left
                t1.changeX((int)inter.getWidth()/2);
                int newX = (int) (inter.getWidth() * -1)/2;
                t2.changeX(newX);
            }
            if((inter.getMinX() >= t1.getRect().getMinX()) && (inter.getHeight() >= inter.getWidth())){  // tank1 going right
                t2.changeX((int)(inter.getWidth()/2));
                int newX = (int) (inter.getWidth() * -1)/2;
                t1.changeX(newX);
            }
            if((inter.getMaxY() < t1.getRect().getMaxY()) && (inter.getHeight() <= inter.getWidth())){  // tank1 going down
                t1.changeY((int)inter.getHeight()/2);
                int newY = (int) (inter.getHeight() * -1)/2; // change this section
                t2.changeY(newY);
            }
            if((inter.getMinY() >= t1.getRect().getMinY()) && (inter.getHeight() <= inter.getWidth())){  // tank1 going up
                t2.changeY((int)(inter.getHeight())/2);
                int newY = (int) (inter.getHeight() * -1)/2;
                t1.changeY(newY);
            }
        }

        for(int r = 0; r < allPowers.size(); r++){                          // remove power ups after pickup
            if (t.getRect().intersects(allPowers.get(r).getRect())) {
                t.powerUp(allPowers.get(r).getPowerUp());
                allPowers.get(r).setPowerUp(0);
                mapChange(allPowers.get(r).getArrayW(), allPowers.get(r).getArrayH());
            }
        }

        for(int p = 0; p < t1.getBulletList().size(); p++) {                    //bullet into tank2
            if (t1.getBulletList().get(p).getRect().intersects(t2.getRect())) {
                t1.getBulletList().remove(p);
                t2.damage();
            }
        }
        for(int o = 0; o < t2.getBulletList().size();o++){                      //bullet into tank1
            if(t2.getBulletList().get(o).getRect().intersects(t1.getRect())){
                t2.getBulletList().remove(o);
                t1.damage();
            }
        }


        for(int i = 0; i < allWalls.size(); i++){

            if(t.getRect().intersects(allWalls.get(i).getRect())) {             //tank into wall
                Rectangle2D inter = t.getRect().createIntersection(allWalls.get(i).getRect());
                if((inter.getMaxX() < t.getRect().getMaxX()) && (inter.getHeight() >= inter.getWidth()) ){ // going left, push right
                    t.changeX((int)inter.getWidth());
                }
                if((inter.getMinX() >= t.getRect().getMinX()) && (inter.getHeight() >= inter.getWidth())){  // going right, push left..
                    int newX = (int) inter.getWidth() * -1;
                    t.changeX(newX);
                }
                if((inter.getMaxY() < t.getRect().getMaxY()) && (inter.getHeight() <= inter.getWidth())){  // going up, push back down
                    t.changeY((int)inter.getHeight());
                }
                if((inter.getMinY() >= t.getRect().getMinY()) && (inter.getHeight() <= inter.getWidth())){  // going down, push back up..
                    int newY = (int) inter.getHeight() * -1;
                    t.changeY(newY);
                }
            }
            for (int j = 0; j <t.getBulletList().size();j++){           //bullet into wall
                if(t.getBulletList().get(j).getRect().intersects(allWalls.get(i).getRect())){
                    if(allWalls.get(i).getBreak()){
                        mapChange(allWalls.get(i).getArrayW(), allWalls.get(i).getArrayH());
                        t.getBulletList().remove(j);
                    } else {
                        t.getBulletList().remove(j);
                    }
                }
            }
        }
    }



    public void mapChange(int arrayWidth, int arrayHeight){     //remove tile from map
        map.get(arrayHeight).set(arrayWidth, 0);
    }

    private void printBullets(Tank t){
        for (int i = 0; i < t.getBulletList().size(); i++){
            t.getBulletList().get(i).drawImage(buffer);
        }
    }


    @Override
    public void paintComponent(Graphics g) {

        if ((t1.getHEALTH() == 0 && t1.getLives() == 0)|| (t2.getHEALTH() == 0 && t2.getLives() == 0)) {
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.drawString("GAME OVER", SCREEN_WIDTH/2-125,SCREEN_HEIGHT/2);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            buffer = world.createGraphics();
            super.paintComponent(g2);

            collisionCheck(t1);
            collisionCheck(t2);
            showBullets(t1);
            showBullets(t2);
            drawBackGround();
            mapUpdate();
            this.t1.drawImage(buffer);
            this.t2.drawImage(buffer);
            printBullets(t1);
            printBullets(t2);
            g2.drawImage(t1.updateCam(world), 0, 0, null);                         //map for t1
            g2.drawImage(t2.updateCam(world), 1 + TRE.SCREEN_WIDTH / 2, 0, null);      //map for t2

            //print lives and hp
            g2.setColor(Color.gray);
            g2.fillRect(5, 750, 400, 20);
            g2.fillRect(SCREEN_WIDTH - 425, 750, 400, 20);
            g2.setColor(Color.red);
            g2.fillRect(5, 750, t1.getHEALTH() * 4, 20);
            g2.fillRect(SCREEN_WIDTH - 425, 750, t2.getHEALTH() * 4, 20);
            g2.setColor(Color.gray);
            g2.drawRect(5, 750, 400, 20);
            g2.drawRect(SCREEN_WIDTH - 425, 750, 400, 20);
            for(int i = 0; i < t1.getLives(); i++) {
                g2.drawImage(t2img, 80 * (i+1), 800, null);
            }
            for(int k = 0; k < t2.getLives(); k++){
                g2.drawImage(t2img, SCREEN_WIDTH - (75+(80*(k+1))),800,null);
            }

            g2.scale(.2, .2);
            g2.drawImage(world, GAME_WIDTH + (GAME_WIDTH / 6) - 20, GAME_HEIGHT * 3 + 400, null); //minimap
        }
    }

}
