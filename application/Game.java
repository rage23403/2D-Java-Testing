package application;
/**
 * Write a description of class Game here.
 *
 * @author  Circle Onyx
 * @version 0.5a
 */
public class Game
{
    static boolean game = true;
    public static Player p;
    public static BasicTimer timeCheck;

    public static boolean held = false;
    public static boolean left = false;
    public static boolean up = false;
    public static boolean right = false;
    public static boolean down = false;
    public static boolean a = false;
    public static boolean paused = false;
    public static Thread loop = new Thread(){
            public void run(){
                try{
                Thread.sleep(2500);
            }catch(Exception e){}
                gLoop();
            }
        };
    private static void gLoop(){
        timeCheck = new BasicTimer();
        Background b = new Background(600,600,"ugly.png");
        p = new Player(5,5,"sprite_sheet.png");
        inputManager.addLayer(b,0);
        inputManager.addLayer(p.getAnimation(),1);
        inputManager.RequestPaint();
        timeCheck.start();
        while(game){
            if(!held || timeCheck.Timing == 15){
                timeCheck.Timing = 0;
                if(left){
                    p.move(Direction.LEFT);
                    held = true;
                }
                if(right){
                    p.move(Direction.RIGHT);
                    held = true;
                }
                if(up){
                    p.move(Direction.UP);
                    held = true;
                }
                if(down){
                    p.move(Direction.DOWN);
                    held = true;
                }
                if(a){
                    p.animate();
                    held = true;
                }
            }
            try{Thread.sleep(16);}catch(Exception e){}
        }
    }
    static Player getPlayer(){
        return p;
    }
}
