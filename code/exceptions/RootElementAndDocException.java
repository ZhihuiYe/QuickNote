package exceptions;

public class RootElementAndDocException extends RuntimeException
{
    //Create RootElementAndDocException with no message and no cause
    public RootElementAndDocException()
    {
        super();
    }//RootElementAndDocException

    //Create RootElementAndDocException with message but no cause.
    public RootElementAndDocException(String message)
    {
        super(message);
    }//RootElementAndDocException

    //Create RootElementAndDocException with message with cause
    public RootElementAndDocException(String message, Throwable cause)
    {
        super(message, cause);
    }//RootElementAndDocException

    //Create RootElementAndDocException with no message with cause
    public RootElementAndDocException(Throwable cause)
    {
        super(cause);
    }//RootElementAndDocException

}//RootElementAndDocException