import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter
{
        private static Document doc;
        private static Element rootElement;
        
        /**
         * XMLReader and Writer have to use the same Document object
         * @param ReaderReturnObject the object that return from XMLReader
         **/
        public XMLWriter(ReaderReturnObject fileContent)
        {
            doc = fileContent.getReaderDoc();
            rootElement =fileContent.getDocElement();
        }//XMLWriter
        
        /**
         * Append the new note into a document.
         * the document is given by the return object 'ReaderReturnObject'
         * which is empty if it faild to read a category file
         * @param String fileName the name of the xml file that will be producted
         * @param ElementData A object contains the data to product a subelement
         * of the file; e.g. Note, File etc
         * @return Element the rootElement contains the new element
         **/
        public Element writeFile(String fileName, ElementData givenData)
        {
          try
          {
                Element dataInElement = givenData.toElement(doc);
                rootElement.appendChild(dataInElement);

                //update the lastUpdate of the rootElement of the file ---------
                Attr newTime = doc.createAttribute("lastUpdate");
                newTime.setValue(givenData.getTime());
                rootElement.setAttributeNode(newTime);
                //update the lastUpdate of the rootElement of the file ---------

                generateXMLFile(fileName);
                return rootElement;
          }//try
          catch (ParserConfigurationException pce)
          {
                pce.printStackTrace();
                return null;
          }//catch
          catch (TransformerException tfe)
          {
                tfe.printStackTrace();
                return null;
          }//catch
        }//writeCategoryFile
        
        private static Boolean updateIndex(ElementData givenData)
        {
            
            return true;
        }//updateIndex


        // write the content into xml file
        private static Boolean generateXMLFile(String fileName)
            throws ParserConfigurationException, TransformerException
        {

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

//                 StreamResult result =  new StreamResult(System.out);
//                 transformer.transform(source, result);

                StreamResult result = new StreamResult(new File(fileName + ".xml"));
                transformer.transform(source, result);

                System.out.println(ElementPrinter.ANSI_RED 
                                 + "System: '" + fileName + "' generated"
                                 + ElementPrinter.ANSI_RESET + "\n");
                                 
                return true;
        }//generateXMLFile
        
        
        
        //Helpper methods --------------------------------------------------------------
        
        //Helpper methods --------------------------------------------------------------
}//class
