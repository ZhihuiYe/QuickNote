import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLReader
{
    public XMLReader()
    {
    }//Constructor

    /**
     * @param
     * @return
     **/
    public ReaderReturnObject read(String fileName)
    {
        try
        {
            ReaderReturnObject returnObject = new ReaderReturnElement();
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            
            //Trying to set the create a document object
            try
            {
                //try to build a Document object from the selected file
                File fXmlFile = new File(fileName + ".xml");
                returnObject.setReaderDoc(dBuilder.parse(fXmlFile));
            }//try
            catch (Exception e)
            {
                //if it failed to build a Document object form the selected file
                //(the file does not exist) then build a empty one
                returnObject.setReaderDoc(docBuilder.newDocument());
                returnObject.setDocElement(doc.createElement("category"));
                return returnObject;
            }//catch

            //-If successfully created the document object by the givenFileName-
            //normalize the document to aviod unexpected format of element content
            doc.getDocumentElement().normalize();

            Node docElement = doc.getDocumentElement();
            if (docElement.getNodeType() == Node.ELEMENT_NODE)
                returnObject.setDocElement((Element)docElement);
            else
            {
                System.out.println("docElement is null");
                returnObject.setDocElement(doc.createElement("category"));
            }//else
            
            return returnObject;
        }//try
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }//catch
    }//read


    public void printIndexDoc(Element docElement)
    {
        //Index tag
        System.out.println("Root element: " + docElement.getNodeName() + "\n"
                         + "lastUpdate:   " + docElement.getAttribute("lastUpdate") + "\n");
        
        //File tag
        NodeList nList = docElement.getElementsByTagName("file");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                printFile((Element)currentNode);
        }//for
    }//printIndexDoc
    
    public void printFile(Element givenNote)
    {
        //File tag
        System.out.println("CurrentElement: " + givenNote.getNodeName()
                         + "lastUpdate:     " + givenNote.getAttribute("lastUpdate")
                         + "File Name:      " + givenNote.getAttribute("fileName"));
        
        //Earliest Note
        Element earliestNote = (Element)givenNote.getElementsByTagName("earliestNote").item(0);
        System.out.println("Earliest note:  " + earliestNote.getAttribute("time") + " " 
                                              + earliestNote.getTextContent());
        
        //Note
        NodeList nList = givenNote.getElementsByTagName("note");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
            {
                //Note Attribute
                Element currentElement = (Element)currentNode;
                System.out.println("Title: " + currentElement.getAttribute("title") + "\n"
                                 + "Time:  " + currentElement.getAttribute("time")  + "\n");

                //Keywords
                NodeList kwList = currentElement.getElementsByTagName("keywords");
                for (int kwI = 0; kwI < kwList.getLength(); kwI++)
                    System.out.println("keywords: " + kwList.item(kwI).getTextContent());
            }//if
        }//for
    }//printFile

    
    /**
     * Print out the notes element content from a document element
     **/
    public void printCategoryDoc(Element docElement)
    {
            System.out.println("Root element :" + docElement.getNodeName());
            System.out.println("id: " + docElement.getAttribute("id"));
            System.out.println("lastUpdate: " + docElement.getAttribute("lastUpdate") + "\n");

            // Extract all the driver element from the document
            NodeList nList = docElement.getElementsByTagName("note");

            for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
            {
                    Node currentNode = nList.item(nIndex);

                    if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                            printNote((Element)currentNode);
            }//for
    }//printCategoryDoc

    private void printNote(Element givenNote)
    {
        System.out.println("CurrentElement: " + givenNote.getNodeName() + "\n"
                         + "Create Time:    " + givenNote.getAttribute("createTime") + "\n"
                         + "Title:          " + givenNote.getElementsByTagName("title").item(0).getTextContent() + "\n"
                         + "Content:        " + givenNote.getElementsByTagName("content").item(0).getTextContent());
        System.out.println("\n\n");
    }//printNote
}//class