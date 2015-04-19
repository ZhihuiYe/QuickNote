import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class ReaderReturnObject
{
    //the Document object that XMLReader has used
    //It will reused by XMLWriter
    private final Document readerDoc;
    //the file content read by XMLReader 
    private final Element fileContentInElement;
    
    public ReaderReturnObject()
    {
        //construct does nothing
    }//construct
    
    public void setDocElement(Element requireFileContentInElement)
    {
        fileContentInElement = requireFileContentInElement;
    }//setDocElement
    
    public void setReaderDoc(Document requireReaderDoc)
    {
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
