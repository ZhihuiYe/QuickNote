package views;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Print
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


    /**
     * Print out all content of the index file
     * @param indexElement
     */
    public static void printIndexDoc(Element indexElement)
    {
        //Index tag
        System.out.println(ANSI_RED  + "Printing : " + ANSI_RESET
                                   + indexElement.getNodeName() + "\n"
                         + ANSI_BLUE + "lastUpdate:   " + ANSI_RESET
                                   + indexElement.getAttribute("lastUpdate") + "\n");

        //Category tag
        NodeList categories = indexElement.getElementsByTagName("category");
        for (int cIndex = 0; cIndex < categories.getLength(); cIndex++)
        {
            Node currentNode = categories.item(cIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
                printIndexCategory((Element)currentNode);
        }//for
    }//printIndexDoc

    /**
     * Print out all content of a category element(from a index file)
     * @param givenCategoryElement the categoryElement that we want to print
     */
    public static void printIndexCategory(Element givenCategoryElement)
    {
        //Category tag
        System.out.println(/*ANSI_GREEN + "Printing : " + ANSI_RESET
                                      + givenCategoryElement.getNodeName()
                         +*/ "\nCategory:       " + givenCategoryElement.getAttribute("categoryName")
                         + "\nLastUpdate:     " + givenCategoryElement.getAttribute("lastUpdate")
                         + "\nLatestNote:     " + givenCategoryElement.getAttribute("latestNote") + "\n");

        //Print all note that the category element contains
        NodeList nList = givenCategoryElement.getElementsByTagName("note");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
            {
                //Note Attribute
                Element currentElement = (Element)currentNode;
                System.out.println(ANSI_CYAN   + "\tTime:  " + ANSI_RESET
                                  + currentElement.getAttribute("createTime")  + "\n"
                                  + ANSI_GREEN + "\tTitle: " + ANSI_RESET
                                  + currentElement.getTextContent() + "\n");
            }//if
        }//for
    }//printIndexCategory


    /**
     * Print out the notes element content from a document element(from a category file)
     * @param categoryElement the category that we want to print
     **/
    public static void printCategory(Element categoryElement)
    {
            System.out.println(ANSI_RED  + "Printing :" + ANSI_RESET + " :"
                                         + categoryElement.getNodeName()              + "\n"
                        + "id:         " + categoryElement.getAttribute("id")         + "\n"
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


    /**
     * Print out a note element (from a category file)
     * @param givenNote the note that we want to print
     */
    public static void printNote(Element givenNote)
    {
        System.out.println(/*"CurrentElement: " + givenNote.getNodeName() + "\n"
            + */ANSI_GREEN + "Title:          " + ANSI_RESET
                         + givenNote.getElementsByTagName("title").item(0).getTextContent() + "\n"
                         + "Create Time:    " + givenNote.getAttribute("createTime") + "\n"
                         + "Content:        " + givenNote.getElementsByTagName("content").item(0).getTextContent());
        System.out.println("\n");
    }//printNote
}//class