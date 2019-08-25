
package tankrotationexample;



import java.awt.*;
import java.awt.image.BufferedImage;


/**
 *
 * @author anthony-pc
 */

public class PowerUp {


    private int x;
    private int y;
    private Rectangle rect;
    private BufferedImage power;
    private Graphics2D buffer;
    private int width;
    private int height;
    private int TileWidth;
    private int TileHeight;
    private int powerUp;


    PowerUp(int x, int y, int TileWidth, int TileHeight, BufferedImage power, Graphics2D buffer, int width, int height, int powerUp) {
        this.x = x;
        this.y = y;
        this.TileWidth = TileWidth;
        this.TileHeight = TileHeight;
        this.power = power;
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.powerUp = powerUp;
        rect = new Rectangle(this.x, this.y, this.TileWidth, this.TileHeight);
        drawImage(this.buffer);
    }

    public Rectangle getRect(){
        return rect;
    }

    public int getArrayW(){
        return width;
    }

    public int getArrayH(){
        return height;
    }

    public int getPowerUp(){ return powerUp; }

    public void setPowerUp(int num){ this.powerUp = num; }

    public void drawImage(Graphics g){
        g.drawImage(power,x,y,null);
        g.drawRect(x,y,TileWidth,TileHeight);
    }


}
