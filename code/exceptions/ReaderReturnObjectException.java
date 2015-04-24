public class ReaderReturnObjectException extends RuntimeException
{
    //Create ReaderReturnObjectException with no message and no cause
    public ReaderReturnObjectException()
    {
        super();
    }//ReaderReturnObjectException
    
    //Create ReaderReturnObjectException with message but no cause.
    public ReaderReturnObjectException(String message)
    {
        super(message);
    }//ReaderReturnObjectException
    
    //Create ReaderReturnObjectException with message with cause
    public ReaderReturnObjectException(String message, Throwable cause)
    {
        super(message, cause);
    }//ReaderReturnObjectException
    
    //Create ReaderReturnObjectException with no message with cause
    public ReaderReturnObjectException(Throwable cause)
    {
        super(cause);
    }//ReaderReturnObjectException
    
}//ReaderReturnObjectException