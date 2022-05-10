package application;
import javafx.scene.image.Image;
/**
 * Write a description of interface Layerable here.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public interface Layerable
{
    byte getLayer();//returns the layer value for the sprite
    void moveUp();//moves the sprite up a layer
    void moveDown();//moves the sprite down a layer
    Image getImage();//returns the sprite's Image
    int[] getImgProps();//gets width, height, x coords, y coords and where to crop sprite at x and y
}
