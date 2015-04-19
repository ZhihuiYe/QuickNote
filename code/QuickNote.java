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
        XMLReader xmlreader = new XMLReader();
        
//         Element notes = xmlreader.read("data/Note");
//         xmlreader.printNoteDoc(notes);
        
        Element index = xmlreader.read("data/NoteIndexTemplate");
        xmlreader.printIndexDoc(index);
    }//main
    
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
    }//findCategory
}//class