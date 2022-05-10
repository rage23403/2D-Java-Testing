package application;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import java.util.Map;
import javafx.scene.input.KeyCode;
import java.io.File;
/**
 * Write a description of class JSONComp here.
 *
 * @author Circle Onyx
 * @version 1.0
 */
public class JSONComp
{
    public static TypeReference<Map<KeyCode,GameButtons>> example = new TypeReference<Map<KeyCode,GameButtons>>() {};
    private static ObjectMapper obj = new ObjectMapper();
    public static void outputAppend(Map<KeyCode,GameButtons> o, String fileLocation) throws Exception{
        obj.writeValue(new File(fileLocation), o);
    }
    public static Map<KeyCode,GameButtons> inputAppend(String fileLocation) throws Exception{
        return obj.readValue(new File(fileLocation), example);
    }
}
