package application;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Fixed size 64x64
 * WIP
 **/
public class Tile {
	float rate;
	Image Sprite;
	boolean animated;
	Animation anim;
	Tile(float danger, Image sprite){
		rate = danger;
		Sprite = sprite;
	}
	
	public static WritableImage append(WritableImage image, Tile appendix) {
		
		return new WritableImage(0, 0);
	}
	
	
}
