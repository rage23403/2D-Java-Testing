package misc;


/**
 * Handles all the possibilities for an error happening in code. 
 * Treated like an exception but instead of printing to console
 * it paints a message on the screen.
 * 
 * @author Circle Onyx
 * @version 1.0
 */
public class Error
{
    ErrorType errT;
    ErrorCode errC;
    public Error(){}
    public Error(ErrorType type, ErrorCode code){
        errT = type;
        errC = code;
    }
    public Error(String type, int code){
        switch(code%4){
            case 1:errC = ErrorCode.Code_X01; break;
            case 2:errC = ErrorCode.Code_X02; break;
            case 3:errC = ErrorCode.Code_X03; break;
            case 0:errC = ErrorCode.Code_X04; break;
        }
        type = type.toLowerCase();
        switch(type){
            case "io":errT = ErrorType.IO;break;
            case "paint":errT = ErrorType.Paint;break;
            case "code":errT = ErrorType.Code;break;
            default:errT = ErrorType.Code; errC = ErrorCode.Code_X04; break;
        }
    }
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        switch(errC){
            case Code_X01:b.append("Code_&01: Illegal Arguments");break;
            case Code_X02:b.append("Code_&02: No Arguments");break;
            case Code_X03:b.append("Code_&03: Insufficient Arguments");break;
            case Code_X04:b.append("Code_&04");break;
        }
        switch(errT){
            case Paint:b.replace(5,6,"1");if(b.toString().equals("Code_104")){b.append(": Image not found");}break;
            case Code:b.replace(5,6,"2"); if(b.toString().equals("Code_204")){b.append(": Coder's Brain not found");}break;
            case IO:b.replace(5,6,"3");if(b.toString().equals("Code_304")){b.append(": File not found");}break;
        }
        return b.toString();
    }
}
