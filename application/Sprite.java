package application;
/**
 * Write a description of class Sprite here.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public class Sprite
{
    public byte Layer = 0;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int spriteSize = 0; //always AxA or square.
    private int spriteLocX = -1;
    private int spriteLocY = -1;
    Sprite(){}
    Sprite(int iWSize, int iHSize, int sSize, int spriteNum){
        imageWidth = iWSize;
        imageHeight = iHSize;
        spriteSize = sSize;
        if((spriteNum*spriteSize) > imageHeight || (spriteNum*spriteSize) > imageWidth){return;}
        spriteLocX = ((spriteNum*spriteSize)%imageWidth);
        spriteLocY = (spriteNum*spriteSize)/imageWidth;
    }
    
    int getSprX(){return spriteLocX;}
    int getSprY(){return spriteLocY;}
    int[] getImgProps(){return new int[]{spriteLocX, spriteLocY, spriteSize};}
}
