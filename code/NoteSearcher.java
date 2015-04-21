import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NoteSearcher
{
    public NoteSearcher()
    {
        //construtor does nothing
    }//construtor
    
    public Element searchCategory(String givenCategory)
    {
        XMLReader xmlReader = new XMLReader();
        
        ReaderReturnObject readerReturnObject = xmlReader.readCategoryFile(givenCategory);
        
        //return the category element
        return readerReturnObject.getDocElement();
    }//searchCategory
}//class