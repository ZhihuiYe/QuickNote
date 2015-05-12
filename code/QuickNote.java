// package code;
// import code.*;
import elementObjects.*;
import exceptions.*;

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
        SHOW_ALL,
        FIND_CATEGORY,
        FIND_NOTE,
        CREATE_CATEGORY,
        CREATE_NOTE,
        HELP
    }//Command

    private static ArrayList<String> userInputs = null;
    private static XMLReader xmlreader = null;

    public static void main(String[] arg)
    {
        xmlreader = new XMLReader();
        ReaderReturnObject indexFile = xmlreader.readFile(ElementData.FileType.INDEX, null);;
        ReaderReturnObject categoryFile;

        Command command = userInputHandler(arg);
        xmlreader = new XMLReader();

        switch(command)
        {
            case      SHOW_ALL:
                    Print.printIndexDoc(indexFile.getDocElement());
                    break;
            case FIND_CATEGORY:
                    String targetCategory = userInputs.get(0);
                    String searchCategoryResult = findACategoryFromAIndexFile(indexFile.getDocElement(), targetCategory);
                    if (searchCategoryResult == null)
                    {
                        System.out.println("Category does not exist");
                        System.exit(0);
                    }//if
                    else//if the category file exist then read the file
                    {
                        categoryFile = xmlreader.readFile(ElementData.FileType.CATEGORY, searchCategoryResult);
                        Print.printCategory(categoryFile.getDocElement());
                        System.exit(0);
                    }//else
                    break;
            case     FIND_NOTE:
                    String targetNote = userInputs.get(0);
                    ArrayList<String> categoriesContainTheSimilarNotes = findSimilarNoteFromIndexFile(indexFile.getDocElement(), targetNote);
                    if (categoriesContainTheSimilarNotes.isEmpty())
                    {
                        System.out.println("Note does not exist");
                        System.exit(0);
                    }//if
                    else
                    {
                        ArrayList<Element> selectedNotes = new ArrayList<>();
                        for (int currentCategory = 0; currentCategory < categoriesContainTheSimilarNotes.size(); currentCategory++)
                        {
                            categoryFile = xmlreader.readFile(ElementData.FileType.CATEGORY, categoriesContainTheSimilarNotes.get(currentCategory));
                            Element categoryElement = categoryFile.getDocElement();
                            NodeList notes = categoryElement.getElementsByTagName("note");
                            for (int noteIndex = 0; noteIndex < notes.getLength(); noteIndex++)
                            {
                                Node currentNote = notes.item(noteIndex);
                                if (currentNote.getNodeType() == Node.ELEMENT_NODE)
                                {
                                    String currentNoteTitle = ((Element)currentNote).getElementsByTagName("title").item(0).getTextContent();
                                    System.out.println("find title: " + currentNoteTitle);
                                    if(targetNote.equalsIgnoreCase(currentNoteTitle))
                                        selectedNotes.add((Element)currentNote);
                                }//if
                            }//for
                        }//for
                        if (selectedNotes.isEmpty())
                        {
                            System.out.println("The note cannot find from any of the category files");
                            System.exit(0);
                        }//if
                        else
                        {
                            for(Element currentNote : selectedNotes)
                                Print.printNote(currentNote);

                        }//else
                    }//else
                    break;
            case   CREATE_NOTE:
                    //creating new note
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                    Calendar now = Calendar.getInstance();
                    Note newNote = new Note(userInputs.get(0), ElementData.DataType.NOTE, userInputs.get(1)
                                            ,sdf.format(now.getTime()), userInputs.get(2));


                    categoryFile = xmlreader.readFile(ElementData.FileType.CATEGORY, userInputs.get(0));
                    XMLWriter  noteWriter = new XMLWriter(categoryFile);
                    Element includedNewNode = noteWriter.writeFile(ElementData.DataType.NOTE, newNote);
                    Print.printCategory(includedNewNode);

                    XMLWriter indexWriter = new XMLWriter(indexFile);
                    Element updatedIndex  = indexWriter.writeFile(ElementData.DataType.INDEX_NOTE  , newNote);
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
            case        "showall":
                    return Command.SHOW_ALL;
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


    private static ArrayList<String> findSimilarNoteFromIndexFile(Element indexElement, String targetNote)
    {
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
                        if (title.equalsIgnoreCase(targetNote))
                            categoriesContainTheSimilarNotes.add( ((Element)currentCategory).getAttribute("categoryName"));
                    }//if
                }//for
            }//if
        }//for

        return categoriesContainTheSimilarNotes;
    }//findSimilarNoteFromIndexFile

    private static String findACategoryFromAIndexFile(Element indexElement, String targetCategory)
    {
        NodeList categories = indexElement.getElementsByTagName("category");
        for (int catIndex = 0; catIndex < categories.getLength(); catIndex++)
        {
            Node currentCategory = categories.item(catIndex);
            if (currentCategory.getNodeType() == Node.ELEMENT_NODE)
            {
                String categoryName = ((Element)currentCategory).getAttribute("categoryName");
                if (categoryName.equalsIgnoreCase(targetCategory))
                    return categoryName;
            }//if
        }//for

        return null;
    }//findCategory
}//class
