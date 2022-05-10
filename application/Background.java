package application;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
/**
 * Background image used in canvas.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public class Background implements Layerable
{
    private byte Layer = -1;
    private Image bg;
    private Color exceptionColor;
    private int width, height;
    
    public byte getLayer(){return Layer;}
    public void moveUp(){}
    public void moveDown(){}
    public Image getImage(){return bg;}
    public int[] getImgProps(){
        return new int[]{0, 0, 0, 0, width, height};
    }
    Background(int w, int h, String img){
        try{
            bg = new Image(img);
        }catch(Exception e){bg = null;}
        width = w;
        height = h;
    }

    Background(int w, int h, String img, Color backup){
        try{
            bg = new Image(img);
        }catch(Exception e){exceptionColor = backup; bg = null;}
        width = w;
        height = h;
    }
    
    public Color getColor(){
        return exceptionColor;
    }

    public int getWidth(){return width;}

    public int getHeight(){return height;}
}
