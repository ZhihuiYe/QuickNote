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
        
        public XMLWriter(ReaderReturnObject fileContent)
        {
            doc = fileContent.getReaderDoc();
            rootElement =fileContent.getDocElement();
        }//XMLWriter
        
        /**
         * Append the new note into a document.
         * the document is given by the return object 'ReaderReturnObject'
         * which is empty if it faild to read a category file
         **/
        public Element writeFile(String fileName, ElementData givenElementDataObject)
        {
          try
          {
                Element dataInElement = givenElementDataObject.toElement(doc);

                rootElement.appendChild(dataInElement);

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
        
        
        // write the content into xml file
        private static void generateXMLFile(String fileName)
            throws ParserConfigurationException, TransformerException
        {

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

//                 StreamResult result =  new StreamResult(System.out);
//                 transformer.transform(source, result);

                StreamResult result = new StreamResult(new File(fileName + ".xml"));
                transformer.transform(source, result);

                System.out.println(fileName + " generated");
        }//generateXMLFile
        
        
        
        //Helpper methods --------------------------------------------------------------
        
        //Helpper methods --------------------------------------------------------------
}//class
