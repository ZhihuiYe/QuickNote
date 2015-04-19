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
        
        public XMLWriter()
        {
        }//XMLWriter
        
        /**
         * If the category is already exist, then the new note will append to it
         * else will create a new category
         **/
        public Boolean write(String givenCategory, Note givenNote, Element oldCategory)
        {
          try
          {
                /* Initialising the document -------------------------------- */
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
                /* Initialising the document -------------------------------- */
                
                //transfer Note object to Note Element--------------------------
                Element newNote  = doc.createElement("note");
                
                Attr createTime = doc.createAttribute("createTime");
                createTime.setValue(givenNote.getCreateTime());
                
                Element title    = doc.createElement("title");
                title.appendChild(doc.createTextNode(givenNote.getTitle()));
                
                Element content  = doc.createElement("content");
                content.appendChild(doc.createTextNode(givenNote.getContent()));
                
                newNote.setAttributeNode(createTime);
                newNote.appendChild(title);
                newNote.appendChild(content);
                //transfer Note object to Note Element--------------------------

                Element rootElement = null;
                
                if (oldCategory == null)
                {
                    // root element-------------------------------------------------
                    rootElement = doc.createElement("category");
                    Attr rootId     = doc.createAttribute("id");
                    rootId.setValue(givenCategory);
                    Attr lastUpdate = doc.createAttribute("lastUpdate");
                    lastUpdate.setValue(givenNote.getCreateTime());
                    
                    rootElement.setAttributeNode(rootId);
                    rootElement.setAttributeNode(lastUpdate);
                    // root element-------------------------------------------------
                }//if
                else
                {
                    rootElement = oldCategory;
                }//else
                
                rootElement.appendChild(newNote);
                doc.appendChild(rootElement);
                
                generateXMLFile(givenCategory);
                
                return true;
          }//try
          catch (ParserConfigurationException pce)
          {
                pce.printStackTrace();
                return false;
          }//catch
          catch (TransformerException tfe)
          {
                tfe.printStackTrace();
                return false;
          }//catch
        }//write
        
        
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
}//class