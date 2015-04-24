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
        
        XMLReader xmlreader = new XMLReader();
        ReaderReturnObject oldCategoryFileContent = xmlreader.readFile(ElementData.DataType.CATEGORY, givenCategory);

        XMLWriter xmlwriter = new XMLWriter(oldCategoryFileContent);
        
        ElementPrinter elementPrinter = new ElementPrinter();
        Note newNote;
        Element includedNewNode;
        
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "1", "12/04/2015", givenNoteContent + "1");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);
        
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "2", "13/05/2015", givenNoteContent + "2");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);
        
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "3", "14/06/2015", givenNoteContent + "3");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);

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
