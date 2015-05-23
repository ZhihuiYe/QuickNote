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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QuickNote
{
    //the type of command the program will accept
    private enum Command
    {
        SHOW_ALL        ("Show All        : null"),
        FIND_CATEGORY   ("Find Category   : categoryName"),
        FIND_NOTE       ("Find Note       : noteTitle"),
        CREATE_CATEGORY ("Create Category : categoryName"),
        CREATE_NOTE     ("Create Note     : categoryName(new/existing), noteTitle, noteContent"),
        HELP            ("Help            : null");

        private final String inString;
        Command(String inString) { this.inString = inString; }
        public String toString() { return inString; }

        /**
         * @return all the command in a String
         */
        public static String allString()
        {
            String allCommandInStr = "Commands         : Require Info\n";
            for(Command currentCommand : Command.values())
                allCommandInStr += "-" + currentCommand + "\n";

            return allCommandInStr;
        }//allString
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
        try
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
                        System.out.println(Print.ANSI_GREEN + "Help Memu:\n"
                                           + Command.allString() + Print.ANSI_RESET);
                        break;
                default:
                        System.out.println("Cannot identify the command");
                        break;
            }//command
        }//try
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
        }//catch
    }//main

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

    /**
     * gather all the necessary info from the user and identify the command that the user
     * has selected
     * @param inputs the user inputs
     * @return the type of command that the user want to run
     */
    private static Command userInputHandler(String[] inputs)
        throws IOException
    {
        String command = null;
        userInputs = new ArrayList<>();

        //if the user has not enter the command, then ask the user
        if (inputs.length == 0)
        {
            String allCommand = "";

            command = getUserInput("Please selected your command."
                                    + "\nOptions are below:\n"
                                    + Command.allString(), true);
        }//if
        else
            command = inputs[0];

        //the case and space of the command will be ignored
        switch (command.toLowerCase().replace(" ", ""))
        {
            case        "showall":
                    return Command.SHOW_ALL;
            case   "findcategory":
                    //copy all the parameter form the program parameter
                    for (int i = 1; i < inputs.length; i++)
                        userInputs.add(inputs[i]);

                    //the rest necessary parameter for the command will ask the user to get it
                    switch (userInputs.size())
                    {
                        case  0:
                            userInputs.add(getUserInput("Please enter the category that you want to search", true));
                        default:
                            break;
                    }//switch

                    return Command.FIND_CATEGORY;

            case "createcategory":
                    //copy all the parameter form the program parameter
                    for (int i = 1; i < inputs.length; i++)
                        userInputs.add(inputs[i]);

                    //the rest necessary parameter for the command will ask the user to get it
                    switch (userInputs.size())
                    {
                        case  0:
                            userInputs.add(getUserInput("Please enter the category that you want to create", true));
                        default:
                            break;
                    }//switch

                    return Command.CREATE_CATEGORY;
            case       "findnote":
                    //copy all the parameter form the program parameter
                    for (int i = 1; i < inputs.length; i++)
                        userInputs.add(inputs[i]);

                    //the rest necessary parameter for the command will ask the user to get it
                    switch (userInputs.size())
                    {
                        case  0:
                            userInputs.add(getUserInput("Please enter the title of the note that you want to find", true));
                        default:
                            break;
                    }//switch
                    return Command.FIND_NOTE;

            case     "createnote":
                    //copy all the parameter form the program parameter
                    for (int i = 1; i < inputs.length; i++)
                        userInputs.add(inputs[i]);

                    //the rest necessary parameter for the command will ask the user to get it
                    switch (userInputs.size())
                    {
                        case  0:
                            userInputs.add(getUserInput("Please enter the category(new/existing one)"
                                            + " that you want to put your new note into.", true));
                        case  1:
                            userInputs.add(getUserInput("Please enter the title of the note.", true));
                        case  2:
                            userInputs.add(getUserInput("Please enter the content of the note.", true));
                        default:
                            break;
                    }//switch
                    return Command.CREATE_NOTE;
            case           "help":
                    return Command.HELP;
            default:
                    System.out.println("Cannot identify the command");
                    System.exit(-1);
        }//command
        return null;
    }//userInputHandler


    private static BufferedReader sInput = null;
    /**
     * @param message the message that will show to the user before user enter their input
     * @param inputCorrection true when it need user to confirm theirs input
     * @return the user's input
     */
    private static String getUserInput(String message, Boolean inputCorrection)
        throws IOException
    {
        System.out.println(Print.ANSI_RED + message + Print.ANSI_RESET);

        if (sInput == null)
            sInput = new BufferedReader(new InputStreamReader(System.in));

        String input = null;
        String answer = null;

        while ( (input = sInput.readLine()) != null)
        {
            if (inputCorrection)
            {
                System.out.println("Is '" + input + "' correct?(Yes/No)");

                if ( (answer = sInput.readLine()) == null ) break;

                if (answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("Y"))
                    break;
            }//if
            else if ( ! inputCorrection && input.length() != 0 )
                break;
        }//while

        return input;
    }//getUserInput

}//class