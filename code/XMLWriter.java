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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class XMLWriter
{
        private static Document doc;
        private static Element rootElement;
        public static final String indexFileName = "index";

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
        public Element writeFile(ElementData.DataType fileType, ElementData givenData)
        {
          try
          {
                String fileName = "DefaultFileName";
                switch(fileType)
                {
                   case       NOTE:
                                    Element noteElement = givenData.toElement(doc);
                                    rootElement.appendChild(noteElement);
                                    fileName = ((Note)givenData).getCategory();
                                    break;
                   case INDEX_NOTE:
                                    Element indexNoteElement = givenData.toIndexElement(doc);
                                    Element categoryElement = findCategory( ((Note)givenData).getCategory() );

                                    Attr lastUpdate = doc.createAttribute("lastUpdate");
                                    lastUpdate.setValue(givenData.getTime());
                                    categoryElement.setAttributeNode(lastUpdate);

                                    Attr latestNote = doc.createAttribute("latestNote");
                                    latestNote.setValue(((Note)givenData).getTitle());
                                    categoryElement.setAttributeNode(latestNote);

                                    categoryElement.appendChild(indexNoteElement);
                                    rootElement.appendChild(categoryElement);

                                    fileName = indexFileName;
                                    break;
                   default:
                                    System.out.println("Can not identify the file type");
                                    System.exit(-1);
                                    break;
                }//switch

                //update the lastUpdate attr of the rootElement of the file
                Attr newTime = doc.createAttribute("lastUpdate");
                newTime.setValue(givenData.getTime());
                rootElement.setAttributeNode(newTime);
                generateXMLFile(fileName);

                //return the rootElement for debuging
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


        private static Element findCategory(String targetCategory)
        {
            NodeList nList = rootElement.getElementsByTagName("category");
            for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
            {
                Node currentNode = nList.item(nIndex);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    String categoryName = ((Element)currentNode).getAttribute("categoryName");
                    if (categoryName.equals(targetCategory))
                        return (Element)currentNode;
                }//if
            }//for

            Element newCategory = doc.createElement("category");
            Attr newCategoryName = doc.createAttribute("categoryName");
            newCategoryName.setValue(targetCategory);
            newCategory.setAttributeNode(newCategoryName);

            return newCategory;
        }//findCategory



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

                System.out.println(Print.ANSI_RED
                                 + "System: '" + fileName + "' generated/updated"
                                 + Print.ANSI_RESET + "\n");

                return true;
        }//generateXMLFile
}//class
