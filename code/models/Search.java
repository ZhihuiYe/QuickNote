package models;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.ArrayList;

public class Search
{
    //----- Search Methods ---------------------------------------------------------
    /**
     * find a similar note from the index file
     * @param targetNote the note that we want to search
     * @return a list of categories name that contain the targetNote
     */
    public static ArrayList<String> findSimilarNoteFromIndexFile(Element indexElement, String targetNote)
    {
        if (indexElement == null)
        {
            System.out.println("findSimilarNoteFromIndexFile: indexElement is null.");
            System.exit(0);
        }//if

        ArrayList<String> categoriesContainTheSimilarNotes = new ArrayList<>();

        NodeList categories = indexElement.getElementsByTagName("category");
        for (int catIndex = 0; catIndex < categories.getLength(); catIndex++)
        {
            Node currentCategory = categories.item(catIndex);
            if (currentCategory.getNodeType() == Node.ELEMENT_NODE)
            {
                NodeList notes = ((Element)currentCategory).getElementsByTagName("note");
                for(int noteIndex = 0; noteIndex < notes.getLength(); noteIndex++)
                {
                    Node currentNote = notes.item(noteIndex);
                    if (currentNote.getNodeType() == Node.ELEMENT_NODE)
                    {
                        String title = ((Element)currentNote).getTextContent();
                        if (title.replace(" ", "").equalsIgnoreCase(targetNote.replace(" ", "")))
                            categoriesContainTheSimilarNotes.add( ((Element)currentCategory).getAttribute("categoryName"));
                    }//if
                }//for
            }//if
        }//for

        return categoriesContainTheSimilarNotes;
    }//findSimilarNoteFromIndexFile

    /**
     * find a category name from the index file
     * @param targetCategory the category that we want to find
     * @return return null if no search category or return the name of the category file
     */
    public static String findACategoryFromAIndexFile(Element indexElement, String targetCategory)
    {
        if (indexElement == null)
        {
            System.out.println("findACategoryFromAIndexFile: indexElement is null.");
            System.exit(0);
        }//if

        NodeList categories = indexElement.getElementsByTagName("category");
        for (int catIndex = 0; catIndex < categories.getLength(); catIndex++)
        {
            Node currentCategory = categories.item(catIndex);
            if (currentCategory.getNodeType() == Node.ELEMENT_NODE)
            {
                String categoryName = ((Element)currentCategory).getAttribute("categoryName");
                if (categoryName.equalsIgnoreCase(targetCategory.replace(" ", "")))
                    return categoryName;
            }//if
        }//for

        return null;
    }//findCategory
//----- Search Methods ---------------------------------------------------------
}//class