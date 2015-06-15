package controllers;

import elements.ElementData.FileType;
import elements.ElementData.DataType;
import views.*;
import models.*;
import elements.*;
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

public class CommandController
{
    //the type of command the program will accept
    public enum Command
    {
        SHOW_ALL        ("ShowAll        : <null>"),
        FIND_CATEGORY   ("FindCategory   : <categoryName>"),
        FIND_NOTE       ("FindNote       : <noteTitle>"),
        CREATE_CATEGORY ("CreateCategory : <categoryName>"),
        CREATE_NOTE     ("CreateNote     : <categoryName(new/existing)> <noteTitle> <noteContent>"),
        HELP            ("Help           : <null>");

        private final String inString;
        Command(String inString) { this.inString = inString; }
        public String toString() { return inString; }

        /**
         * @return all the command in a String
         */
        public static String allString()
        {
            String allCommandInStr = "Commands        : Require Info\n";
            for(Command currentCommand : Command.values())
                allCommandInStr += "-" + currentCommand + "\n";

            return allCommandInStr + "\n";
        }//allString
    }//Command


    private static RootElementAndDoc indexFile    = null;
    private static XMLReader xmlreader            = null;
    private static RootElementAndDoc categoryFile = null;

    public static void execute(ArrayList<String> inputs, Command selectedCommand)
        throws CommandControllerException, IOException
    {
        if (xmlreader == null)
            xmlreader = new XMLReader();

        if (indexFile == null)
            indexFile = xmlreader.readFile(FileType.INDEX, null);

        switch(selectedCommand)
        {
            case      SHOW_ALL:
                    executeShowallCommand();
                    break;
            case FIND_CATEGORY:
                    executeFindCategoryCommand(inputs);
                    break;
            case     FIND_NOTE:
                    executeFindNoteCommand(inputs);
                    break;
            case   CREATE_CATEGORY:
                    executeFindCategoryCommand(inputs);
                    break;
            case   CREATE_NOTE:
                    executeCreateNoteCommand(inputs);
                    break;
            case          HELP:
                    executeHelpCommand();
                    break;
            default:
                    throw new CommandControllerException("Cannot identify the command.");
        }//command
    }//execute

    //-----COMMANDS-----------------------------------------------------------------
    private static void executeHelpCommand()
    {
        System.out.println(Print.ANSI_GREEN + "Help Memu:\n"
                           + Command.allString()
                           + Print.ANSI_RESET);
    }//executeHelpCommand

    private static void executeFindNoteCommand(ArrayList<String>  userInputs)
    {
        String targetNote = userInputs.get(0);
        //search the node from the index file
        ArrayList<String> categoriesContainTheSimilarNotes = Search.findSimilarNoteFromIndexFile(indexFile.getDocElement(), targetNote);
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
                categoryFile = xmlreader.readFile(FileType.CATEGORY, categoriesContainTheSimilarNotes.get(currentCategory));
                Element categoryElement = categoryFile.getDocElement();
                NodeList notes = categoryElement.getElementsByTagName("note");
                for (int noteIndex = 0; noteIndex < notes.getLength(); noteIndex++)
                {
                    Node currentNote = notes.item(noteIndex);
                    if (currentNote.getNodeType() == Node.ELEMENT_NODE)
                    {
                        String currentNoteTitle = ((Element)currentNote).getElementsByTagName("title").item(0).getTextContent();
                        if(targetNote.equalsIgnoreCase(currentNoteTitle.replace(" ", "")))
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
    }//executeFindNoteCommand

    private static void executeFindCategoryCommand(ArrayList<String> userInputs)
    {
        String targetCategory = userInputs.get(0);
        ArrayList<String> searchCategoryResults = Search.findACategoryFromAIndexFile(indexFile.getDocElement(), targetCategory);

        if (searchCategoryResults.isEmpty())
        {
            System.out.println("Category does not exist");
            System.exit(0);
        }//if

        //if the category exist then read the category file and print out
        //else acknowledge the user
        for (String currentCategory : searchCategoryResults)
        {
            categoryFile = xmlreader.readFile(FileType.CATEGORY, currentCategory);
            Print.printCategory(categoryFile.getDocElement());
        }//foreach
    }//executeFindCategoryCommand

    private static void executeCreateNoteCommand(ArrayList<String> userInputs)
        throws IOException
    {
        String currentCategory    = userInputs.get(0);
        String currentNoteTitle   = userInputs.get(1);
        String currentNoteContent = userInputs.get(2);

        //creating a new note
        Note newNote = new Note(currentCategory, DataType.NOTE
                                , currentNoteTitle, currentNoteContent);

        //read the selected category file
        categoryFile = xmlreader.readFile(FileType.CATEGORY, currentCategory);
        XMLWriter noteWriter = new XMLWriter(categoryFile);
        Element includedNewNode = noteWriter.writeFile(DataType.NOTE, newNote);
        Print.printCategory(includedNewNode);

        //write the new note into the file
        XMLWriter indexWriter = new XMLWriter(indexFile);
        Element updatedIndex  = indexWriter.writeFile(DataType.INDEX_NOTE  , newNote);
        Print.printIndexDoc(updatedIndex);
    }//executeCreateNoteCommand

    private static void executeCreateCategoryCommand(ArrayList<String>  userInputs)
    {
    }//executeCreateCategoryCommand

    private static void executeShowallCommand()
    {
        Print.printIndexDoc(indexFile.getDocElement());
    }//executeShowallCommand

//----- Ccommands -----------------------------------------------------------------


}//class