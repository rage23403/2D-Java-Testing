package misc;


/**
 * Codes:
 * X01 - illegal arguments (usually args are greater than they're supposed to be)
 * X02 - no arguments (no code/image found)
 * X03 - arguments missing (usually args are less than they're supposed to be)
 * X04 - Paint: image not found, IO: input or output not found, Code: Coder's brain not found
 *
 * @author Circle Onyx
 * @version 1.0
 */
public enum ErrorCode
{   //X01 = illegal args, X02 = no args, X03 = args missing, X04 = not found
    Code_X01, Code_X02, Code_X03, Code_X04
}
