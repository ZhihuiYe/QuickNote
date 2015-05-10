import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class ReaderReturnObject
{
    //the Document object that XMLReader has used
    //It will reused by XMLWriter
    private Document readerDoc;
    //the file content read by XMLReader 
    private Element fileContentInElement;
    
    public ReaderReturnObject()
    {
        //construct does nothing
    }//construct
    
    public void setDocElement(Element requireFileContentInElement) 
        throws ReaderReturnObjectException
    {
        if (fileContentInElement != null)
            throw new ReaderReturnObjectException
                    ("ReaderReturnObject: fileContentInElement already setted");
        fileContentInElement = requireFileContentInElement;
    }//setDocElement
    
    public void setReaderDoc(Document requireReaderDoc)
        throws ReaderReturnObjectException
    {
        if (readerDoc != null)
            throw new ReaderReturnObjectException
                    ("ReaderReturnObject: readerDoc already setted");
        readerDoc = requireReaderDoc;
    }//setReaderDoc

    public Element getDocElement()
    {
        return fileContentInElement;
    }//getReaderDoc
    
    public Document getReaderDoc()
    {
        return readerDoc;
    }//getDocElement
}//class
