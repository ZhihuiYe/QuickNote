package exceptions;

public class XMLWriterException extends RuntimeException
{
    //Create XMLWriterException with no message and no cause
    public XMLWriterException()
    {
        super();
    }//XMLWriterException

    //Create XMLWriterException with message but no cause.
    public XMLWriterException(String message)
    {
        super(message);
    }//XMLWriterException

    //Create XMLWriterException with message with cause
    public XMLWriterException(String message, Throwable cause)
    {
        super(message, cause);
    }//XMLWriterException

    //Create XMLWriterException with no message with cause
    public XMLWriterException(Throwable cause)
    {
        super(cause);
    }//XMLWriterException

}//XMLWriterException