package com.jobproject.smartchat.commands;

/*
 * Class works with general logic, gets and executes user commands
 * */

import com.jobproject.smartchat.files_work.FileWork;
import com.jobproject.smartchat.userinterface.TextShower;

import java.util.Map;

public class CommandWork {
    private static final String STRING_IS_EMPTY         = "ur message is empty, please, type something else";
    private static final String ANOTHER_FILE            = "new file chosen";

    private String userText;
    private FileWork file;
    private AllCommands currentCommand;
    private TextShower textShower;

    public CommandWork() {
        file = new FileWork();                  // initializing current file
        textShower = new TextShower();
        showWelcome();                          // showing "welcome" message
    }

    // shows "welcome" string (on startup only)
    private void showWelcome() {
        textShower.setOuputText(file.readFile(FileWork.READ_WELCOME));
    }

    // general answer for user, return false on shutdown command
    public boolean answer(String txt) {
        if (txt == null)
            return false;
        this.userText = txt;
        this.currentCommand = AllCommands.commandCheck(txt);
        //
        switch (currentCommand) {
            case NO_COMMANDS_FOUND:
                this.showText();
                break;
            case CHANGE_FILE:
                this.changeFile();
                break;
            case EMPTY_STRING:
                this.stringIsEmpty();
                break;
            case SHOW_HELP:
                this.showHelp();
                break;
            case CLOSE_CHAT:
                this.closeProgram();
                return false;
        }
        return true;
    }

    // shows general random answer for user
    private void showText() {
        textShower.setOuputText(file.readFile(FileWork.READ_RANDOM_STRING));
    }

    // changing current file
    private void changeFile() {
        file.chooseNewFile();
        textShower.setOuputText(CommandWork.ANOTHER_FILE);
    }

    // answer for empty input
    private void stringIsEmpty() {
        textShower.setOuputText(CommandWork.STRING_IS_EMPTY);
    }

    // show help: commands and description
    private void showHelp() {
        Map<String,String> descriptionMap = AllCommands.getCommandsWithDescription();
        descriptionMap.forEach((k,v) -> textShower.setOuputText(k + "\t\t\t\t" + v));
    }

    // shows "goodbye" string (on shutdown only)
    private void closeProgram() {
        textShower.setOuputText(file.readFile(FileWork.READ_GOODBYE));
    }

    // get text user wrote
    public String getUserText() {
        return userText;
    }

    // get current command if it exists
    public AllCommands getCurrentCommand() {
        return currentCommand;
    }
}
