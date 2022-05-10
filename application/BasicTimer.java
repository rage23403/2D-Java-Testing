package application;
import javafx.animation.AnimationTimer;

/**
 * Write a description of class BasicTimer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BasicTimer extends AnimationTimer
{
    public int Timing = 0;
    public void handle(long time){
        if(Timing < 16){
            Timing++;
        }
    }
}
