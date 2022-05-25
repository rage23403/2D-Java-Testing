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
    private static final int BrushSize = 4;
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
        int h = 60;
        int w = 20;
        g.setFill(Color.BLACK);
        g.fillRect(0,0,canWidth,canHeight);
        g.setFill(Color.RED);
        g.fillRect((w+20)*BrushSize,(20+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+20)*BrushSize,(30+h)*BrushSize,(10)*BrushSize,(20)*BrushSize);
        g.fillRect((w+30)*BrushSize,(35+h)*BrushSize,(10)*BrushSize,(10)*BrushSize);
        g.fillRect((w+20)*BrushSize,(50+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+60)*BrushSize,(20+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+60)*BrushSize,(30+h)*BrushSize,(10)*BrushSize,(30)*BrushSize);
        g.fillRect((w+60)*BrushSize,(35+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+80)*BrushSize,(30+h)*BrushSize,(10)*BrushSize,(5)*BrushSize);
        g.fillRect((w+70)*BrushSize,(45+h)*BrushSize,(10)*BrushSize,(5)*BrushSize);
        g.fillRect((w+70)*BrushSize,(50+h)*BrushSize,(15)*BrushSize,(5)*BrushSize);
        g.fillRect((w+75)*BrushSize,(55+h)*BrushSize,(15)*BrushSize,(5)*BrushSize);
        g.fillRect((w+100)*BrushSize,(20+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+100)*BrushSize,(30+h)*BrushSize,(10)*BrushSize,(30)*BrushSize);
        g.fillRect((w+100)*BrushSize,(35+h)*BrushSize,(30)*BrushSize,(10)*BrushSize);
        g.fillRect((w+120)*BrushSize,(30+h)*BrushSize,(10)*BrushSize,(5)*BrushSize);
        g.fillRect((w+110)*BrushSize,(45+h)*BrushSize,(10)*BrushSize,(5)*BrushSize);
        g.fillRect((w+110)*BrushSize,(50+h)*BrushSize,(15)*BrushSize,(5)*BrushSize);
        g.fillRect((w+115)*BrushSize,(55+h)*BrushSize,(15)*BrushSize,(5)*BrushSize);
        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(err.toString(),(w+70)*BrushSize,(80+h)*BrushSize);
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
