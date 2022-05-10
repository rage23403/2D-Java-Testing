package misc;


/**
 * Enumeration class for the Error Types.
 * Used to remove typo possibilities bringing strange or incorrect errors
 * 
 * Paint: error happening with something being painted. Ex. Invalid stuff painted to screen.
 * Code: error happening with code being written badly. Ex. Incorrect array size.
 * IO: error happening with files being imported. Ex. getting the file for "spritsheet.png" instead of "spritesheet.png"
 *
 * @author Circle Onyx
 * @version 1.0
 */
public enum ErrorType
{
    Paint, Code, IO 
}
