package elements;

import elements.ElementData;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Note extends ElementData
{
    private String category;
    private String titleInStr;
    private String contentInStr;

    public Note(String givenCategory, DataType givenDataType
              , String givenTitle, String givenContent)
    {
        super(givenDataType);

        category        = givenCategory;
        titleInStr      = givenTitle;
        contentInStr    = givenContent;
    }//Note

    @Override
    public Element toElement(Document doc)
    {
        Element newNote  = doc.createElement("note");

        Attr createTime = doc.createAttribute("createTime");
        createTime.setValue(super.getTime());

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
        createTime.setValue(super.getTime());
        indexNote.setAttributeNode(createTime);

        return indexNote;
    }//toIndexElement

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
        return super.getTime();
    }//getCreateTime


    public String getContent()
    {
        return contentInStr;
    }//getContent
}//class