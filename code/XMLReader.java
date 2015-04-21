import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLReader
{
    public enum DataType
    {
        CATEGORY,
        FILE,
        NOTE
    }
    
    public XMLReader()
    {
    }//Constructor

    /**
     * @param
     * @return
     **/
    public ReaderReturnObject readFile(DataType fileType, String fileName)
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document doc;
            ReaderReturnObject returnObject = new ReaderReturnObject();
            
            //Trying to set the create a document object
            try
            {
                //try to build a Document object from the selected file
                File fXmlFile = new File(fileName + ".xml");
                doc  = docBuilder.parse(fXmlFile);
                
                //normalize the document to aviod unexpected format of element content
                doc.getDocumentElement().normalize();

                Node docElement = doc.getDocumentElement();
                if (docElement.getNodeType() == Node.ELEMENT_NODE)
                {
//                     //Print out the category doc that has been read
//                     printCategoryDocElement((Element)docElement);
                    returnObject.setDocElement((Element)docElement);
                }
                else
                {
                    System.out.println("docElement is not an Element");
                    throw new Exception("reading a non-xml struct file");
                }//else
                
                returnObject.setReaderDoc(doc);
                return returnObject;
            }//try
            catch (Exception e)
            {
                System.out.println("Failed to read '" + fileName + "'");
                //if it failed to build a Document object form the selected file
                //(the file does not exist) then build a empty one
                doc = docBuilder.newDocument();
                returnObject.setReaderDoc(doc);
                
                Element rootElement = null;
                switch (fileType)
                {
                    case CATEGORY: 
                              rootElement = doc.createElement("category");
                              break;
                    case INDEX   : 
                              rootElement = doc.createElement("index");
                              break;
                    default      :
                              System.out.println("readFile: cannot identify the file type");
                              break;
                }//switch
                doc.appendChild(rootElement);
                returnObject.setDocElement(rootElement);
                return returnObject;
            }//catch
        }//try
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }//catch
    }//readFile
}//class
