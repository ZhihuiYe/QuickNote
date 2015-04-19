import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class QuickNote
{
    public static void main(String[] arg)
    {
        String givenCategory    = arg[0];
        String givenNoteTitle   = arg[1];
        String givenNoteContent = arg[2];
        
        XMLWriter xmlwriter = new XMLWriter();
        XMLReader xmlreader = new XMLReader();
        
        Element oldCategoryFile = xmlreader.read(givenCategory);
        Note newNote = new Note(givenNoteTitle, "12/04/2015", givenNoteContent);
        
        xmlwriter.write(givenCategory, newNote, oldCategoryFile);
        
        xmlreader.printCategoryDoc(oldCategoryFile);
        
//         Element index = xmlreader.read("data/");
//         xmlreader.printIndexDoc(index);
        
    }//main

    
//--- Helper methods -----------------------------------------------------------
    /**
     * Find the match category if the category exist then return
     * a string that is same as given or only find similar categories
     * return them as a string array
     **/
    public static String[] findCategory(String givenCategory)
    {
        File folder = new File("data/categories");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) 
        {
            if (listOfFiles[i].isFile()) 
            {
                System.out.println("File " + listOfFiles[i].getName());
            }//if
            else if (listOfFiles[i].isDirectory()) 
            {
                System.out.println("Directory " + listOfFiles[i].getName());
            }//else if
        }//for
        
        return null;
    }//findCategory
//--- Helper methods -----------------------------------------------------------
}//class