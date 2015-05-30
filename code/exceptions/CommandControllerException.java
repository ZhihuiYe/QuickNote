package exceptions;

public class CommandControllerException extends RuntimeException
{
    //Create CommandControllerException with no message and no cause
    public CommandControllerException()
    {
        super();
    }//CommandControllerException

    //Create CommandControllerException with message but no cause.
    public CommandControllerException(String message)
    {
        super(message);
    }//CommandControllerException

    //Create CommandControllerException with message with cause
    public CommandControllerException(String message, Throwable cause)
    {
        super(message, cause);
    }//CommandControllerException

    //Create CommandControllerException with no message with cause
    public CommandControllerException(Throwable cause)
    {
        super(cause);
    }//CommandControllerException

}//CommandControllerException