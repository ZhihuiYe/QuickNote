import controllers.CommandController;
import controllers.CommandController.Command;
import elements.ElementData.FileType;
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

public class QuickNoteCLInterface
{

    //the user inputs (selected category, note title etc). Update by userInputHandler
    private static ArrayList<String> userInputs = null;
    private static XMLReader xmlreader = null;
    private static RootElementAndDoc indexFile;
    private static RootElementAndDoc categoryFile;
    private static Boolean inputConfirmation = true;

    /**
     * the user inputs will passed to userInputHandler to decode
     * the method will return a Command enum and the required info will stored in userInputs
     * then the main program will process the command
     * @param arg contains the user inputs; Command + parameters
     */
    public static void main(String[] arg)
    {
        try
        {
            xmlreader = new XMLReader();
            indexFile = xmlreader.readFile(FileType.INDEX, null);;

            //passed user's inputs to this method to decode the command and
            //ask the user to provide the necessary info and store into "userInputs"
            Command command = userInputHandler(arg);
            CommandController.execute(userInputs, command);
        }//try
        catch(CommandControllerException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }//catch
        catch(Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
        }//catch
    }//main



//----- Helper Methods ---------------------------------------------------------
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
                                    + Command.allString(), false);
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
                            userInputs.add(getUserInput("Category: ", true));
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
                            userInputs.add(getUserInput("Category: ", true));
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
                            userInputs.add(getUserInput("Note Title: ", true));
                        default:
                            break;
                    }//switch
                    return Command.FIND_NOTE;

            case     "createnote":
                    getCreateNoteInputs(inputs);
                    return Command.CREATE_NOTE;
            case           "help":
                    return Command.HELP;
            default:
                    System.out.println("Cannot identify the command");
                    System.exit(-1);
        }//command
        return null;
    }//userInputHandler

    private static void getCreateNoteInputs(String[] inputs)
        throws IOException
    {
        String currentCategory    = null;
        String currentNoteTitle   = null;
        String currentNoteContent = null;

        String similarCategory = null;
        Boolean answer         = null;
        Boolean finalAnswer    = null;

        //Start checking the given category name -------------------------------
        if (inputs.length > 1)
            currentCategory = inputs[1];
        else
            currentCategory = getUserInput("Category: ", ! inputConfirmation);

        if (inputConfirmation)
        {
            //while user not happy with the category/title he/she entered
            //does the user wish to write into an existing category?
            //or does the user wish to create a new category?
            do
            {
                similarCategory = Search.findACategoryFromAIndexFile(indexFile.getDocElement(), currentCategory);
                if (similarCategory == null)
                    System.out.print(Print.ANSI_RED + "The category "
                                        + Print.ANSI_GREEN + "DOES NOT " + Print.ANSI_RESET
                                        + Print.ANSI_RED
                                        + "exist.\nDo you wish to create a new category?("
                                        + currentCategory + ")\n" + Print.ANSI_RESET);
                else
                    System.out.print(Print.ANSI_RED + "The category "
                                        + Print.ANSI_GREEN + "DOES " + Print.ANSI_RESET
                                        + Print.ANSI_RED
                                        + "exist.\nDo you wish to write into this category?("
                                        + similarCategory + ")\n" + Print.ANSI_RESET);

                //if user wish to change the category
                answer = getYesOrNoAnswer();
                if (! answer)
                    currentCategory = getUserInput("New category: ", false);
                else
                    if(similarCategory != null)
                        currentCategory = similarCategory;
            }while( !answer );
        }//if

        //start checking the note title-----------------------------------------
        if (inputs.length > 2)
            currentNoteTitle = inputs[2];
        else
            currentNoteTitle = getUserInput("Title: ", false);

        ArrayList<String> similarTitles = null;
        do
        {
            similarTitles = Search.findSimilarNoteFromIndexFile(indexFile.getDocElement()
                                                            ,currentNoteTitle);
            if (similarTitles.size() > 0)
                currentNoteTitle = getUserInput("The note title '" + currentNoteTitle
                                                + "' is already existed, \nplease enter an new title: "
                                                , true);
        }while(similarTitles.size() > 0);

        if (inputs.length > 3)
            currentNoteContent = inputs[3];
        else
            currentNoteContent = getUserInput("Content: ", true);

        userInputs.add(currentCategory);
        userInputs.add(currentNoteTitle);
        userInputs.add(currentNoteContent);
    }//getCreateNoteInputs


    private static BufferedReader sInput = null;
    /**
     * @param message the message that will show to the user before user enter their input
     * @param inputCorrection true when it need user to confirm theirs input
     * @return the user's input
     */
    private static String getUserInput(String message, Boolean inputCorrection)
        throws IOException
    {
        System.out.print(Print.ANSI_RED + message + Print.ANSI_RESET);

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
                else
                {
                    System.out.println("Please enter again: ");
                    if ( (input = sInput.readLine()) == null )
                        System.out.println("Something wrong.");
                }//else
            }//if
            else if ( ! inputCorrection && input.length() != 0 )
                break;
        }//while

        return input;
    }//getUserInput

    private static Boolean getYesOrNoAnswer()
        throws IOException
    {
        String answer = null;
        do
        {
            answer = getUserInput("", false);
        }while(   (! answer.equalsIgnoreCase("Yes"))
               && (! answer.equalsIgnoreCase("Y"  ))
               && (! answer.equalsIgnoreCase("No" ))
               && (! answer.equalsIgnoreCase("n"  )));

        if(answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("Y"))
            return true;
        else
            return false;
    }//getYesOrNoAnswer
//----- Helper Methods ---------------------------------------------------------
}//class