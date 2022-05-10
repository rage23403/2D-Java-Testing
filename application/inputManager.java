package application;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import misc.Error;
import misc.ErrorCode;
import misc.ErrorType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Write a description of JavaFX class inputManager here.
 *
 * @author Circle Onyx
 * @version 0.5a
 */
public class inputManager extends Application
{
    public static final int ignoreARGB = 0xB6519E;
    static int canWidth=640,canHeight=640;
    Canvas can;
    static GraphicsContext g;
    //private final int BrushSize = 1;
    private static boolean parsingError = false;
    public static ArrayList<Layerable> paintOrder;
    public static Map<KeyCode,GameButtons> keyCodeAction = new HashMap<KeyCode,GameButtons>();
    public static Map<GameButtons,Runnable> actionPress = new HashMap<GameButtons,Runnable>();
    public static Map<GameButtons,Runnable> actionRelease = new HashMap<GameButtons,Runnable>();
    @SuppressWarnings("removal")
	@Override
    public void start(Stage stage)
    {
        // Create a new grid pane
        BorderPane borders = new BorderPane();
        GridPane pane = new GridPane();

        can = new Canvas(canWidth,canHeight);
        g = can.getGraphicsContext2D();

        Button b = new Button("save controls");
        Button b2 = new Button("defaults");
        b.setFocusTraversable(false);
        b2.setFocusTraversable(false);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem item = new MenuItem("Controls");
        menu.getItems().add(item);
        menuBar.getMenus().add(menu);

        borders.setCenter(pane);
        borders.setTop(menuBar);
        pane.add(can,0,1);
        pane.add(b,1,1);
        pane.add(b2,2,1);

        Scene scene = new Scene(borders, 800,700);
        stage.setTitle("JavaFX Example");
        stage.setScene(scene);

        actionPress.put(GameButtons.UP, () -> {Game.up = true;});
        actionPress.put(GameButtons.DOWN, () -> {Game.down = true;});
        actionPress.put(GameButtons.LEFT, () ->  {Game.left = true;});
        actionPress.put(GameButtons.RIGHT, () ->  {Game.right = true;});
        actionPress.put(GameButtons.ACTION, () -> {Game.a = true;});
        actionPress.put(GameButtons.START, () -> {
                if(Game.paused){
                    Game.loop.resume();
                }
                else{ 
                    Game.loop.suspend();
                }
            });

        actionRelease.put(GameButtons.UP, () -> {Game.up = false;});
        actionRelease.put(GameButtons.DOWN, () -> {Game.down = false;});
        actionRelease.put(GameButtons.LEFT, () -> {Game.left = false;});
        actionRelease.put(GameButtons.RIGHT, () -> {Game.right = false;});
        actionRelease.put(GameButtons.ACTION, () -> {Game.a = false;});
        actionRelease.put(GameButtons.START, () -> {});

        try{
            keyCodeAction = (Map<KeyCode,GameButtons>)JSONComp.inputAppend("hello.json");
        } catch(Exception ex){ex.printStackTrace();}

        // Show the Stage (window)
        stage.setOnCloseRequest(event -> {System.exit(1);});
        b.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e){
                    try{
                        JSONComp.outputAppend(keyCodeAction, "hello.json");
                    } catch(Exception ex){ex.printStackTrace();}
                }
            });
        b2.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e){
                    defaultControls();
                }
            });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
                public void handle(KeyEvent e){
                    if(keyCodeAction.containsKey(e.getCode()))
                        actionPress.get(keyCodeAction.get(e.getCode())).run();
                }
            });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
                public void handle(KeyEvent e){
                    if(keyCodeAction.containsKey(e.getCode())){
                        Game.held = false;
                        actionRelease.get(keyCodeAction.get(e.getCode())).run();
                    }
                }
            });
        paintOrder = new ArrayList<Layerable>();

        Game.loop.start();
        stage.show();
    }

    public static void RequestPaint(){
        if(!checkSorted()){sortLayers();}
        if(!parsingError){
            Platform.runLater(() ->{
                    for(int i = 0; i < paintOrder.size(); i++){  
                        int[] imgProps = paintOrder.get(i).getImgProps();
                        try{ //image, x offset, y offset, size, size, canvas x, canvas y, size, size
                            PaintCan(paintOrder.get(i).getImage(), imgProps[2], imgProps[3], imgProps[4], imgProps[5], imgProps[0], imgProps[1], imgProps[4], imgProps[5]);
                        }catch(ArrayIndexOutOfBoundsException e){
                            ErrorCode c;
                            if(imgProps.length < 1){c = ErrorCode.Code_X02;}
                            else if(imgProps.length > 6){c = ErrorCode.Code_X01;}
                            else if(1 < imgProps.length && imgProps.length < 6){c = ErrorCode.Code_X03;System.out.println(imgProps.length);}
                            else{c = ErrorCode.Code_X04;}
                            Error err = new Error(ErrorType.Code, c);
                            PaintError(err);
                        }catch(NullPointerException e){
                        }
                    }
                });
        }
    }

    public static void PaintCan(Image sprite, int imgX, int imgY, int imgW, int imgH, int canX, int canY, int canW, int canH) throws NullPointerException{
        if(sprite == null){throw new NullPointerException("requested image is null");} 
        else if(!parsingError){
            g.drawImage(sprite, imgX, imgY, imgW, imgH, canX, canY, canW, canH);
        }
    }

    public static void addLayer(Layerable l, int index){
        paintOrder.add(index, l);
    }

    @SuppressWarnings("removal")
	public static void PaintError(Error err){
        parsingError = true;
        int mult = 4;
        int h = 60;
        int w = 20;
        g.setFill(Color.BLACK);
        g.fillRect(0,0,canWidth,canHeight);
        g.setFill(Color.RED);
        g.fillRect((w+20)*mult,(20+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+20)*mult,(30+h)*mult,(10)*mult,(20)*mult);
        g.fillRect((w+30)*mult,(35+h)*mult,(10)*mult,(10)*mult);
        g.fillRect((w+20)*mult,(50+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+60)*mult,(20+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+60)*mult,(30+h)*mult,(10)*mult,(30)*mult);
        g.fillRect((w+60)*mult,(35+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+80)*mult,(30+h)*mult,(10)*mult,(5)*mult);
        g.fillRect((w+70)*mult,(45+h)*mult,(10)*mult,(5)*mult);
        g.fillRect((w+70)*mult,(50+h)*mult,(15)*mult,(5)*mult);
        g.fillRect((w+75)*mult,(55+h)*mult,(15)*mult,(5)*mult);
        g.fillRect((w+100)*mult,(20+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+100)*mult,(30+h)*mult,(10)*mult,(30)*mult);
        g.fillRect((w+100)*mult,(35+h)*mult,(30)*mult,(10)*mult);
        g.fillRect((w+120)*mult,(30+h)*mult,(10)*mult,(5)*mult);
        g.fillRect((w+110)*mult,(45+h)*mult,(10)*mult,(5)*mult);
        g.fillRect((w+110)*mult,(50+h)*mult,(15)*mult,(5)*mult);
        g.fillRect((w+115)*mult,(55+h)*mult,(15)*mult,(5)*mult);
        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(err.toString(),(w+70)*mult,(80+h)*mult);
        Game.loop.suspend();
    }

    public static void sortLayers(){
        Collections.sort(paintOrder, (o1, o2) -> o1.getLayer() - o2.getLayer());
        checkSorted();
    }

    public static boolean checkSorted(){
        for(int i = 0; i < paintOrder.size(); i++){
            if(paintOrder.get(i).getLayer() < paintOrder.get(i+1).getLayer()){
                return false;
            }
            System.out.print(paintOrder.get(i).getLayer() + " ");
        }
        return true;
    }

    public static void defaultControls(){
        keyCodeAction.put(KeyCode.UP, GameButtons.UP);
        keyCodeAction.put(KeyCode.DOWN, GameButtons.DOWN);
        keyCodeAction.put(KeyCode.LEFT, GameButtons.LEFT);
        keyCodeAction.put(KeyCode.RIGHT, GameButtons.RIGHT);
        keyCodeAction.put(KeyCode.A, GameButtons.ACTION);
        keyCodeAction.put(KeyCode.ENTER, GameButtons.START);
    }
    public static void Main(String[] args) {
    	launch(args);
    }
}
