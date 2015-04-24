import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Note extends ElementData
{
    private String titleInStr;
    private String createTimeInStr;
    private String contentInStr;
    
    public Note(DataType givenDataType
               , String givenTitle, String givenCreateTime, String givenContent)
    {
        super(givenDataType);
        titleInStr      = givenTitle;
        createTimeInStr = givenCreateTime;
        contentInStr    = givenContent;
    }//Note
    
    @overide
    public Element toElement(Document doc)
    {
        Element newNote  = doc.createElement("note");
                    
        Attr createTime = doc.createAttribute("createTime");
        createTime.setValue(createTimeInStr);
        
        Element title    = doc.createElement("title");
        title.appendChild(doc.createTextNode(titleInStr));
        
        Element content  = doc.createElement("content");
        content.appendChild(doc.createTextNode(contentInStr));
        
        newNote.setAttributeNode(createTime);
        newNote.appendChild(title);
        newNote.appendChild(content);
        
        return newNote;
    }//toElement
    
    public String getTitle()
    {
        return titleInStr;
    }//getTitle
    
    public String getCreateTime()
    {
        return createTimeInStr;
    }//getCreateTime
    
    public String getContent()
    {
        return contentInStr;
    }//getContent
    
    
}//class