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


public class QuickNote
{
    //the type of command the program will accept
    private enum Command
    {
        SHOW_ALL,
        FIND_CATEGORY,
        FIND_NOTE,
        CREATE_CATEGORY,
        CREATE_NOTE,
        HELP
    }//Command

    //the user inputs (selected category, note title etc). Update by userInputHandler
    private static ArrayList<String> userInputs = null;
    private static XMLReader xmlreader = null;

    /**
     * the user inputs will passed to userInputHandler to decode
     * the method will return a Command enum and the required info will stored in userInputs
     * then the main program will process the command
     */
    public static void main(String[] arg)
    {
        xmlreader = new XMLReader();
        RootElementAndDoc indexFile = xmlreader.readFile(ElementData.FileType.INDEX, null);;
        RootElementAndDoc categoryFile;

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
                    //if the category exist then read the category file and print out
                    //else acknowledge the user
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
                    //search the node from the index file
                    ArrayList<String> categoriesContainTheSimilarNotes = findSimilarNoteFromIndexFile(indexFile.getDocElement(), targetNote);
                    //if there has at least one category file contain the similar note
                    //then print out the notes
                    //else acknowledge the user
                    if (categoriesContainTheSimilarNotes.isEmpty())
                    {
                        System.out.println("Note does not exist");
                        System.exit(0);
                    }//if
                    else
                    {
                        ArrayList<Element> selectedNotes = new ArrayList<>();
                        //print out all the notes
                        for (int currentCategory = 0; currentCategory < categoriesContainTheSimilarNotes.size();
                                currentCategory++)
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
                                    if(targetNote.equalsIgnoreCase(currentNoteTitle))
                                        selectedNotes.add((Element)currentNote);
                                }//if
                            }//for
                        }//for

                        //if no note is found
                        if (selectedNotes.isEmpty())
                        {
                            System.out.println("The note cannot find from any of the category files");
                            System.exit(0);
                        }//if
                        else//print out all the notes
                        {
                            for(Element currentNote : selectedNotes)
                                Print.printNote(currentNote);

                        }//else
                    }//else
                    break;
            case   CREATE_NOTE:
                    //does the category exit?
                    String foundCategoryName = findACategoryFromAIndexFile(indexFile.getDocElement(), userInputs.get(0));
                    if (foundCategoryName == null)
                        foundCategoryName = userInputs.get(0);

                    //creating a new note
                    Note newNote = new Note(foundCategoryName, ElementData.DataType.NOTE
                                            , userInputs.get(1), userInputs.get(2));




                    //read the selected category file
                    categoryFile = xmlreader.readFile(ElementData.FileType.CATEGORY, foundCategoryName);
                    XMLWriter noteWriter = new XMLWriter(categoryFile);
                    Element includedNewNode = noteWriter.writeFile(ElementData.DataType.NOTE, newNote);
                    Print.printCategory(includedNewNode);

                    //write the new note into the file
                    XMLWriter indexWriter = new XMLWriter(indexFile);
                    Element updatedIndex  = indexWriter.writeFile(ElementData.DataType.INDEX_NOTE  , newNote);
                    Print.printIndexDoc(updatedIndex);
                    break;
            case          HELP:
                    System.out.println("Comming soon.");
                    break;
            default:
                    System.out.println("Cannot identify the command");
                    break;
        }//command
    }//main



    /**
     * gather all the necessary info from the user and identify the command that the user
     * has selected
     * @param inputs the user inputs
     * @return the type of command that the user want to run
     */
    private static Command userInputHandler(String[] inputs)
    {
//         if (inputs.length == 0)
//         {
//
//         }//if
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


    /**
     * find a similar note from the index file
     * @param targetNote the note that we want to search
     * @return a list of categories name that contain the targetNote
     */
    private static ArrayList<String> findSimilarNoteFromIndexFile(Element indexElement, String targetNote)
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
                        if (title.equalsIgnoreCase(targetNote))
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
    private static String findACategoryFromAIndexFile(Element indexElement, String targetCategory)
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
                if (categoryName.equalsIgnoreCase(targetCategory))
                    return categoryName;
            }//if
        }//for

        return null;
    }//findCategory
}//class
