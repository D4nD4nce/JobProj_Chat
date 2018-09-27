package com.jobproject.smartchat;

/*
 * Class works with general logic, gets and executes user commands
 * */

import com.jobproject.smartchat.files_work.FileWork;
import com.jobproject.smartchat.userinterface.TextShower;

public class Commands {

    public static final int NO_COMMANDS_FOUND = 0;
    public static final int CHANGE_FILE = 1;
    public static final int CLOSE_CHAT = 2;

    private String userText;
    private FileWork file;
    private int currentCommand;
    private TextShower textShower;


    public Commands()
    {
        // initializing current file
        file = new FileWork();
        textShower = new TextShower();

        // showing "welcome" message
        showWelcome();
    }

    public static int commandCheck(String txt)
    {
        switch (txt)
        {
            case "/close":
                return CLOSE_CHAT;
            case "/change":
                return CHANGE_FILE;
        }
        //this.userText = text;
        // if commands ==
        return NO_COMMANDS_FOUND;
    }

    // general answer for user, return false on shutdown command
    public boolean answer(String txt)
    {
        this.userText = txt;
        this.currentCommand = Commands.commandCheck(txt);
        //
        switch (currentCommand)
        {
            case NO_COMMANDS_FOUND:
                this.showText();
                break;
            case CHANGE_FILE:
                this.changeFile();
                break;
            case CLOSE_CHAT:
                this.closeProgram();
                return false;
        }

        return true;
    }

    // shows general answer for user
    private void showText()
    {
        textShower.outputConsole(file.readFile(FileWork.READ_RANDOM_STRING));
    }

    // shows "welcome" string (on startup only)
    private void showWelcome()
    {
        textShower.outputConsole(file.readFile(FileWork.READ_WELCOME));
    }

    // shows "goodbye" string
    private void closeProgram()
    {
        textShower.outputConsole(file.readFile(FileWork.READ_GOODBYE));
    }

    // changing current file
    private void changeFile()
    {
        textShower.outputConsole("file chosen");
    }
}
