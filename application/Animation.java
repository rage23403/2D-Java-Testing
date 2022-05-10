package application;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
/**
 * Handles the animation timing for the superuser Player.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public class Animation extends AnimationTimer  implements Layerable
{
    private int x, y;
    Sprite[] spriteNums;
    Player prefer; //Will probably change to creature to be usable for enemies
    float time = 0;
    float tmult = 0.16f;
    Animation(int[] nums, int imgWidth, int imgHeight, int sizes, Player p){
        spriteNums = new Sprite[nums.length];
        prefer = p;
        for(int i = 0; i < nums.length; i++){
            spriteNums[i] = new Sprite(imgWidth, imgHeight, sizes, nums[i]);
        }
        p.setSprite(spriteNums[0]);
    }
    
    @Override
    public void handle(long now){
        if(time<spriteNums.length-1){
            time += tmult;
        }
        else{time = 0;}
        prefer.setSprite(spriteNums[(int)time]);
    }
    
    public void move(Direction d){
        switch(d){
            case UP:y--;break;
            case DOWN:y++;break;
            case LEFT:x--;break;
            case RIGHT:x++;break;
        }
    }
    
    public void setX(int i){x = i;}
    public void setY(int i){y = i;}
    public int getX(){return x;}
    public int getY(){return y;}
    public byte getLayer(){return spriteNums[(int)time].Layer;}
    public void moveUp(){}
    public void moveDown(){}
    public Image getImage(){return prefer.getImage();}
    public int[] getImgProps(){
        int[] get = spriteNums[(int)time].getImgProps();
        return new int[]{get[2]*x,get[2]*y,get[0],get[1],get[2],get[2]};
    }
}
