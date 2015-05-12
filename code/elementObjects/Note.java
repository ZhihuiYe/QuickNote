package elementObjects;
import elementObjects.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Note extends ElementData
{
    private String category;
    private String titleInStr;
    private String createTimeInStr;
    private String contentInStr;

    public Note(String givenCategory, DataType givenDataType
              , String givenTitle, String givenCreateTime, String givenContent)
    {
        super(givenDataType);
        category        = givenCategory;
        titleInStr      = givenTitle;
        createTimeInStr = givenCreateTime;
        contentInStr    = givenContent;
    }//Note

    @Override
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

    @Override
    public Element toIndexElement(Document doc)
    {
        Element indexNote = doc.createElement("note");
        indexNote.appendChild(doc.createTextNode(titleInStr));

        Attr createTime = doc.createAttribute("createTime");
        createTime.setValue(createTimeInStr);
        indexNote.setAttributeNode(createTime);

        return indexNote;
    }//toIndexElement

    @Override
    public String getTime()
    {
        return createTimeInStr;
    }//getTime

    public String getCategory()
    {
        return category;
    }//getCategory

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