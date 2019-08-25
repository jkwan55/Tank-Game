
package tankrotationexample;



import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author anthony-pc
 */

public class Walls {


    private int x;
    private int y;
    private Rectangle rect;
    private boolean breakable;
    private BufferedImage wall;
    private BufferedImage wall_break;
    private Graphics2D buffer;
    private int width;
    private int height;
    private int TileWidth;
    private int TileHeight;


    Walls(int x, int y, int TileWidth, int TileHeight, boolean breakable, BufferedImage wall , BufferedImage wall_break , Graphics2D buffer, int width, int height) {
        this.x = x;
        this.y = y;
        this.TileWidth = TileWidth;
        this.TileHeight = TileHeight;
        this.breakable = breakable;
        this.wall = wall;
        this.wall_break = wall_break;
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        rect = new Rectangle(this.x, this.y, this.TileWidth, this.TileHeight);
        drawImage(this.buffer);
    }

    public Rectangle getRect(){
        return rect;
    }

    public boolean getBreak(){
        return breakable;
    }

    public int getArrayW(){
        return width;
    }

    public int getArrayH(){
        return height;
    }


    public void drawImage(Graphics g){
        if(breakable){
            g.drawImage(wall_break,x,y,null);
            g.drawRect(x,y,TileWidth,TileHeight);
        } else {
            g.drawImage(wall,x,y,null);
            g.drawRect(x,y,TileWidth,TileHeight);
        }

    }


}
