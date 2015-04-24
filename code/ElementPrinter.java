import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ElementPrinter
{
    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN   = "\u001B[36m";
    public static final String ANSI_WHITE  = "\u001B[37m";

    public ElementPrinter()
    {
    }//ElementPrinter
    
    
    public void printIndexDoc(Element categoryElement)
    {
        //Index tag
        System.out.println(ANSI_RED  + "Root element: " + ANSI_RESET 
                                   + categoryElement.getNodeName() + "\n"
                       + ANSI_BLUE + "lastUpdate:   " + ANSI_RESET 
                                   + categoryElement.getAttribute("lastUpdate") + "\n");
        
        //File tag
        NodeList nList = categoryElement.getElementsByTagName("file");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                printFile((Element)currentNode);
        }//for
    }//printIndexDoc
    
    public void printFile(Element givenFileElement)
    {
        //File tag
        System.out.println(ANSI_GREEN + "CurrentElement: " + ANSI_RESET 
                                    + givenFileElement.getNodeName()
                         + "lastUpdate:     " + givenFileElement.getAttribute("lastUpdate")
                         + "File Name:      " + givenFileElement.getAttribute("fileName"));
        
        //Earliest Note
        Element earliestNote = (Element)givenFileElement.getElementsByTagName("earliestNote").item(0);
        System.out.println(ANSI_PURPLE + "Earliest note:  " + ANSI_RESET 
                                     + earliestNote.getAttribute("time") + " " 
                                     + earliestNote.getTextContent());
        
        //Note
        NodeList nList = givenFileElement.getElementsByTagName("note");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
            {
                //Note Attribute
                Element currentElement = (Element)currentNode;
                System.out.println(ANSI_GREEN + "Title: " + ANSI_RESET
                                            + currentElement.getAttribute("title") + "\n"
                               + ANSI_CYAN  + "Time:  " + ANSI_RESET 
                                            + currentElement.getAttribute("time")  + "\n");

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
    public void printCategory(Element categoryElement)
    {
            System.out.println(ANSI_RED  + "Root element" + ANSI_RESET + " :"
                                       + categoryElement.getNodeName()             + "\n"
                        + "id:         " + categoryElement.getAttribute("id")        + "\n"
                        + "lastUpdate: " + categoryElement.getAttribute("lastUpdate") + "\n");

            // Extract all the driver element from the document
            NodeList nList = categoryElement.getElementsByTagName("note");

            for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
            {
                    Node currentNode = nList.item(nIndex);

                    if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                            printNote((Element)currentNode);
            }//for
    }//printCategory

    private void printNote(Element givenNote)
    {
        System.out.println("CurrentElement: " + givenNote.getNodeName() + "\n"
            + ANSI_GREEN + "Title:          " + ANSI_RESET
                         + givenNote.getElementsByTagName("title").item(0).getTextContent() + "\n"
                         + "Create Time:    " + givenNote.getAttribute("createTime") + "\n"
                         + "Content:        " + givenNote.getElementsByTagName("content").item(0).getTextContent());
        System.out.println("\n");
    }//printNote
}//class