// package code;
import exceptions.RootElementAndDocException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class RootElementAndDoc
{
    //the Document object that XMLReader has used
    //It will reused by XMLWriter
    private Document readerDoc;
    //the file content read by XMLReader
    private Element fileContentInElement;


    public RootElementAndDoc()
    {
        //construct does nothing
    }//construct

    /**
     * save the root element of a xml file
     * @param requireFileContentInElement the root element of a xml file
     */
    public void setDocElement(Element requireFileContentInElement)
        throws RootElementAndDocException
    {
        if (fileContentInElement != null)
            throw new RootElementAndDocException
                    ("RootElementAndDoc: fileContentInElement already setted");
        fileContentInElement = requireFileContentInElement;
    }//setDocElement

    /**
     * save the Document object for XMLWriter to user
     * @param requireReaderDoc the Document object that we used to read the xml file
     */
    public void setReaderDoc(Document requireReaderDoc)
        throws RootElementAndDocException
    {
        if (readerDoc != null)
            throw new RootElementAndDocException
                    ("RootElementAndDoc: readerDoc already setted");
        readerDoc = requireReaderDoc;
    }//setReaderDoc

    /**
     * @return the rootElement that we have saved
     */
    public Element getDocElement()
    {
        return fileContentInElement;
    }//getReaderDoc

    /**
     * @return the Document object that we have saved
     */
    public Document getReaderDoc()
    {
        return readerDoc;
    }//getDocElement
}//class
