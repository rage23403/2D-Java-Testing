package application;
import javafx.scene.image.*;
import java.io.File;
import misc.Error;
import misc.ErrorCode;
import misc.ErrorType;

/**
 * Write a description of class Player here.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public class Player
{
    private Image model;
    private Sprite sprite;
    private Animation anim;
    public misc.ErrorCode error;
    Player(int SpawnX, int SpawnY, String cModel){
        try{
            model = new Image(cModel);
            anim = new Animation(new int[]{1,0,2,1,4,0,4,1,3,0,4},(int)model.getWidth(), (int)model.getHeight(), 64, this);
        }catch(IllegalArgumentException e){
            sprite = new Sprite(); 
            model = null;
            File temp = new File(cModel);
            if(temp.exists()){error = misc.ErrorCode.Code_X01;}
            else{error = misc.ErrorCode.Code_X04;}
        }
        anim.setX(SpawnX);
        anim.setY(SpawnY);
        ignoreColor();
    }

    public boolean checkAnim(){if(sprite.getSprX() < 0){return false;}return true;}

    public Image getImage(){return model;}

    public int getX(){return anim.getX();}

    public int getY(){return anim.getY();}

    public void animate(){
        anim.start();
    }
    
    public Animation getAnimation(){
        return anim;
    }

    public void setSprite(Sprite s){
        sprite = s;
        inputManager.RequestPaint();
    }

    public void move(Direction d){
        anim.move(d);
        inputManager.RequestPaint();
    }

    public void handlErr(){
        Error err;
        if(error != null){
            err = new Error(ErrorType.IO, error);
        }
        else{err = new Error(ErrorType.IO, ErrorCode.Code_X04);}
        inputManager.PaintError(err);
    }
    
    public void ignoreColor(){
        WritableImage temp = new WritableImage((int)model.getWidth(), (int)model.getHeight());
        PixelReader r = model.getPixelReader();
        PixelWriter w = temp.getPixelWriter();
        for(int i = 0; i < model.getHeight(); i++){
            for(int j = 0; j < model.getWidth(); j++){
                int argb = r.getArgb(j,i);
                if((argb & 0xFFFFFF) == inputManager.ignoreARGB){
                    w.setArgb(j,i, 0x00000000);
                }
                else{
                    w.setArgb(j,i, argb);
                }
            }
        }
        model = temp;
    }

}
