package com.jobproject.smartchat;

import com.jobproject.smartchat.files_work.FileWork;
import com.jobproject.smartchat.userinterface.TextShower;

public class Commands {

    public static final int NO_COMMANDS_FOUND = 0;
    public static final int CHANGE_FILE = 1;
    public static final int CLOSE_CHAT = 2;

    String userText;
    FileWork file;
    //int currentCommand;

    public Commands()
    {
        file = new FileWork();
    }

    public static int commandCheck(String txt)
    {
        //this.userText = text;
        // if commands ==
        return NO_COMMANDS_FOUND;
    }

    public boolean answer(String txt)
    {
        this.userText = txt;
        //
        switch (Commands.commandCheck(txt))
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

    private void showText()
    {
        TextShower textShower = new TextShower();
        textShower.outputConsole(file.readFile(FileWork.READ_RANDOM_STRING));
    }

    private void changeFile()
    {

    }

    private void closeProgram()
    {

    }
}
