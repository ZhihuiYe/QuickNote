// package code;
// import code.*;
import elementObjects.*;
import exceptions.*;

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
    /**
     * Constructor reequired nothing
     **/
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
    public ReaderReturnObject readFile(ElementData.FileType fileType, String givenFileName)
            throws XMLReaderException
    {
        String fileName = "DefaultFileName";
        switch(fileType)
        {
            case CATEGORY:
                fileName = "data/" + givenFileName;
                break;
            case    INDEX:
                fileName = "data/" + XMLWriter.indexFileName;
                break;
            default:
                System.out.println("ReadFile: cannot identify the file type");
                System.exit(-1);
                break;
        }//switch

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
                System.out.println(Print.ANSI_RED + "Failed to read "
                                    + "'" + fileName + "'"
                                    + Print.ANSI_RESET);

                //if it failed to build a Document object form the selected file
                //(the file does not exist) then build a empty one
                doc = docBuilder.newDocument();
                returnObject.setReaderDoc(doc);

                Element rootElement = null;
                switch (fileType)
                {
                    case CATEGORY:
                              System.out.println("Creating a new category file: " + fileName);
                              rootElement = doc.createElement("category");
                              Attr idAttr = doc.createAttribute("id");
                              idAttr.setValue(fileName);
                              rootElement.setAttributeNode(idAttr);
                              break;

                    case    INDEX:
                              System.out.println("Creating a new index file");
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
