import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class QuickNote
{
    private enum Command
    {
        FIND_CATEGORY,
        FIND_NOTE,
        CREATE_CATEGORY,
        CREATE_NOTE,
        HELP
    }//Command
    
    private static ArrayList<String> userInputs = null;
    
    public static void main(String[] arg)
    {
        Command command = userInputHandler(arg);
        XMLReader xmlreader = new XMLReader(); 
        switch(command)
        {
            case FIND_CATEGORY:
                    ReaderReturnObject indexFile = xmlreader.readFile(ElementData.DataType.INDEX, null);
                    String targetCategory = userInputs.get(0);
                    String result = findIndexCategory(indexFile.getDocElement(), targetCategory);
                    if (result != null)
                    {
                        System.out.println("Category does not exist");
                        System.exit(0);
                    }//if
                    else
                    {
                        xmlreader = new XMLReader();
                        ReaderReturnObject categoryContent = xmlreader.readFile(ElementData.DataType.CATEGORY, result);
                        Print.printCategory(categoryContent.getDocElement());
                        System.exit(0);
                    }//else
                    break;
            case   CREATE_NOTE:
                    
                    break;
            case     FIND_NOTE:
                    break;
            case          HELP:
                    break;
            default:
                    System.out.println("Cannot identify the command");
                    break;
        }//command
        /*
        String givenCategory    = arg[0];
        String givenNoteTitle   = arg[1];
        String givenNoteContent = arg[2];
<<<<<<< HEAD

=======
        
        XMLReader xmlreader = new XMLReader();
        ReaderReturnObject oldCategoryFileContent = xmlreader.readFile(ElementData.DataType.CATEGORY, givenCategory);

        XMLWriter xmlwriter = new XMLWriter(oldCategoryFileContent);
        
>>>>>>> defe8ccac325a0a0b19f61b6358799210d12ee96
        ElementPrinter elementPrinter = new ElementPrinter();

        XMLReader xmlreader = new XMLReader();

        ReaderReturnObject oldCategoryFileContent = xmlreader.readFile(ElementData.DataType.CATEGORY, givenCategory);
        XMLWriter noteWriter = new XMLWriter(oldCategoryFileContent);

        ReaderReturnObject indexFile = xmlreader.readFile(ElementData.DataType.INDEX, null);
        XMLWriter indexWriter = new XMLWriter(indexFile);

        Note newNote;
        Element includedNewNode;
        Element updatedIndex;

        Note newNote0 = new Note(givenCategory, ElementData.DataType.NOTE, givenNoteTitle + "1", "12/04/2015", givenNoteContent + "1");
        Note newNote1 = new Note(givenCategory+"1A", ElementData.DataType.NOTE, givenNoteTitle + "2", "13/05/2015", givenNoteContent + "2");
        Note newNote2 = new Note(givenCategory, ElementData.DataType.NOTE, givenNoteTitle + "3", "14/06/2015", givenNoteContent + "3");

        includedNewNode = noteWriter.writeFile(ElementData.DataType.CATEGORY, newNote0);
        updatedIndex   = indexWriter.writeFile(ElementData.DataType.INDEX, newNote0);
        elementPrinter.printIndexDoc(updatedIndex);

        includedNewNode = noteWriter.writeFile(ElementData.DataType.CATEGORY, newNote1);
        updatedIndex   = indexWriter.writeFile(ElementData.DataType.INDEX, newNote1);
        elementPrinter.printIndexDoc(updatedIndex);

        includedNewNode = noteWriter.writeFile(ElementData.DataType.CATEGORY, newNote2);
        updatedIndex   = indexWriter.writeFile(ElementData.DataType.INDEX, newNote2);
        elementPrinter.printIndexDoc(updatedIndex);
        
<<<<<<< HEAD
        String result = null;
        result = findIndexCategory(updatedIndex, givenCategory);
        if(result == null)
            System.out.println("cannot find " +givenCategory);
        else
            System.out.println(result);
            
        result = findIndexCategory(updatedIndex, givenCategory+" 1a");
        if(result == null)
            System.out.println("cannot find " +givenCategory);
        else
            System.out.println(result);
            
        result = findIndexCategory(updatedIndex, "afdsafs");
        if(result == null)
            System.out.println("cannot find ");
        else
            System.out.println(result);
            */
=======
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "1", "12/04/2015", givenNoteContent + "1");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);
        
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "2", "13/05/2015", givenNoteContent + "2");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);
        
        newNote = new Note(ElementData.DataType.NOTE, givenNoteTitle + "3", "14/06/2015", givenNoteContent + "3");
        includedNewNode = xmlwriter.writeFile(givenCategory, newNote);
        elementPrinter.printCategory(includedNewNode);

>>>>>>> defe8ccac325a0a0b19f61b6358799210d12ee96
    }//main



    private static Command userInputHandler(String[] inputs)
    {
        String command = inputs[0].toLowerCase();
        userInputs = new ArrayList<>();

        switch(command)
        {
            case   "findcategory":
                    return Command.FIND_CATEGORY;
            case "createCategory":
                    return Command.CREATE_CATEGORY;
            default:
                    System.out.println("Cannot identify the command");
                    System.exit(-1);
        }//command
        return null;
    }//userInputHandler
    
    
    private static String findIndexCategory(Element indexElement, String targetCategory)
    {
        NodeList nList = indexElement.getElementsByTagName("category");
        for (int nIndex = 0; nIndex < nList.getLength(); nIndex++)
        {
            Node currentNode = nList.item(nIndex);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
            {
                String categoryName = ((Element)currentNode).getAttribute("categoryName");
                if (categoryName.equalsIgnoreCase(targetCategory.replaceAll(" ", "")))
                    return categoryName;
            }//if
        }//for

        return null;
    }//findCategory



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
//--- HELPer methods -----------------------------------------------------------
}//class
