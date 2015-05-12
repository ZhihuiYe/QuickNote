package exceptions;

public class XMLReaderException extends RuntimeException
{
    //Create XMLReaderException with no message and no cause
    public XMLReaderException()
    {
        super();
    }//XMLReaderException

    //Create XMLReaderException with message but no cause.
    public XMLReaderException(String message)
    {
        super(message);
    }//XMLReaderException

    //Create XMLReaderException with message with cause
    public XMLReaderException(String message, Throwable cause)
    {
        super(message, cause);
    }//XMLReaderException

    //Create XMLReaderException with no message with cause
    public XMLReaderException(Throwable cause)
    {
        super(cause);
    }//XMLReaderException

}//XMLReaderException