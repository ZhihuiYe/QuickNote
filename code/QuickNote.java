import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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
        ReaderReturnObject indexFile;
        String targetCategory;

        Command command = userInputHandler(arg);
        XMLReader xmlreader = new XMLReader();
        switch(command)
        {
            case FIND_CATEGORY:
                    indexFile = xmlreader.readFile(ElementData.FileType.INDEX, null);
                    targetCategory = userInputs.get(0);
                    String result = findIndexCategory(indexFile.getDocElement(), targetCategory);
                    if (result == null)
                    {
                        System.out.println("Category does not exist");
                        System.exit(0);
                    }//if
                    else//if the category file exist then read the file
                    {
                        xmlreader = new XMLReader();
                        ReaderReturnObject categoryContent = xmlreader.readFile(ElementData.FileType.CATEGORY, result);
                        Print.printCategory(categoryContent.getDocElement());
                        System.exit(0);
                    }//else
                    break;
            case     FIND_NOTE:
                    indexFile = xmlreader.readFile(ElementData.FileType.INDEX, null);
                    break;
            case   CREATE_NOTE:

                    //creating new note
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                    Calendar now = Calendar.getInstance();
                    Note newNote = new Note(userInputs.get(0), ElementData.DataType.NOTE, userInputs.get(1)
                                            ,sdf.format(now.getTime()), userInputs.get(2));


                    ReaderReturnObject categoryFileContent = xmlreader.readFile(ElementData.FileType.CATEGORY, userInputs.get(0));
                    XMLWriter  noteWriter = new XMLWriter(categoryFileContent);
                    Element includedNewNode = noteWriter.writeFile(ElementData.DataType.NOTE, newNote);
                    Print.printCategory(includedNewNode);

                    ReaderReturnObject indexFileContent    = xmlreader.readFile(ElementData.FileType.INDEX   , null);
                    XMLWriter indexWriter = new XMLWriter(indexFileContent);
                    Element updatedIndex    = indexWriter.writeFile(ElementData.DataType.INDEX_NOTE  , newNote);
                    Print.printIndexDoc(updatedIndex);
                    break;
            case          HELP:
                    break;
            default:
                    System.out.println("Cannot identify the command");
                    break;
        }//command
    }//main



    private static Command userInputHandler(String[] inputs)
    {
        String command = inputs[0].toLowerCase();
        userInputs = new ArrayList<>();

        switch(command)
        {
            case   "findcategory":
                    if(inputs.length < 1)
                    {
                        System.out.println("Please enter the category that you want to search");
                        System.exit(0);
                    }//if
                    userInputs.add(inputs[1]);
                    return Command.FIND_CATEGORY;

            case "createcategory":
                    if(inputs.length < 1)
                    {
                        System.out.println("Please enter the category that you want to create");
                        System.exit(0);
                    }//if
                    userInputs.add(inputs[1]);
                    return Command.CREATE_CATEGORY;
            case       "findnote":
                    if(inputs.length < 1)
                    {
                        System.out.println("Please enter the title of the note that you want to search");
                        System.exit(0);
                    }//if
                    userInputs.add(inputs[1]);
                    return Command.FIND_NOTE;
            case     "createnote":
                    if(inputs.length < 3)
                    {
                        System.out.println("Please enter the category, note title and note content");
                        System.exit(0);
                    }//if
                    for(int i = 1; i < 4; i++)
                        userInputs.add(inputs[i]);
                    return Command.CREATE_NOTE;
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
                if (categoryName.equalsIgnoreCase(targetCategory))
                    return categoryName;
            }//if
        }//for

        return null;
    }//findCategory
}//class
