import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import java.io.File;

public class XMLReader
{
<<<<<<< HEAD
    /**
     * Constructor reequired nothing
     **/
=======
    public enum DataType
    {
        CATEGORY,
        FILE,
        NOTE
    }
    
>>>>>>> 6151e3f386415fbaee28541b9ead79bdbcfb9350
    public XMLReader()
    {
    }//Constructor

    /**
     * It will read a xml file, if the file does exist then it will create a 
     * ducument object from the file and read the rootElement of the file then
     * store them in a ReaderReturnObject. If the file does not exist then it 
     * will generate a new empty xml document and create a new rootElement; the 
     * element type is depent on the type of the file.
     * @param fileType The type of file xml file; INDEX, CATEGORY, etc
     * @param fileName the name of the xml file
     * @return a object contains Document object for reading the xml file and 
     * the rootElement of the xml file
     **/
<<<<<<< HEAD
    public ReaderReturnObject readFile(ElementData.DataType fileType, String fileName)
            throws XMLReaderException
=======
    public ReaderReturnObject readFile(DataType fileType, String fileName)
>>>>>>> 6151e3f386415fbaee28541b9ead79bdbcfb9350
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
<<<<<<< HEAD
                System.out.println(ElementPrinter.ANSI_RED + "Failed to read "
                                    + "'" + fileName + "'"
                                    + ElementPrinter.ANSI_RESET);
=======
                System.out.println("Failed to read '" + fileName + "'");
>>>>>>> 6151e3f386415fbaee28541b9ead79bdbcfb9350
                //if it failed to build a Document object form the selected file
                //(the file does not exist) then build a empty one
                doc = docBuilder.newDocument();
                returnObject.setReaderDoc(doc);
                
                Element rootElement = null;
                switch (fileType)
                {
<<<<<<< HEAD
                    case CATEGORY:
                              rootElement = doc.createElement("category");
                              Attr idAttr = doc.createAttribute("id");
                              idAttr.setValue(fileName);
                              rootElement.setAttributeNode(idAttr);
                              break;
                    case FILE    : 
=======
                    case CATEGORY: 
                              rootElement = doc.createElement("category");
                              break;
                    case INDEX   : 
>>>>>>> 6151e3f386415fbaee28541b9ead79bdbcfb9350
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
            throw new XMLReaderException("Failed to read file" + fileName, e);
        }//catch
    }//readFile
}//class
