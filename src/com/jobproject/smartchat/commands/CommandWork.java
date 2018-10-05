package com.jobproject.smartchat.commands;

/*
 * Class works with general logic, gets and executes user commands
 * */

import com.jobproject.smartchat.db_work.DataBase;
import com.jobproject.smartchat.userinterface.TextShower;

import java.util.Map;

public class CommandWork {
    private static final String STRING_IS_EMPTY         = "ur message is empty, please, type something else";
    private static final String ANOTHER_FILE            = "new file chosen";

    private String userText;
    private DataBase baseDB;
    private AllCommands currentCommand;
    private TextShower textShower;

    public CommandWork() {
        baseDB = new DataBase();                    // initializing current file
        textShower = new TextShower();
        showWelcome();                              // showing "welcome" message
    }

    // shows "welcome" string (on startup only)
    private void showWelcome() {
        textShower.setOutputText(baseDB.getAnswer(DataBase.READ_WELCOME));
    }

    // general answer for user, return false on shutdown command
    public boolean answer(String txt) {
        if (txt == null){
            return false;
        }
        this.userText = txt;
        this.currentCommand = AllCommands.commandCheck(txt);
        if (currentCommand != null){
            switch (currentCommand) {
                case NO_COMMANDS_FOUND:
                    this.showText();
                    return true;
                case CHANGE_ANSWERS:
                    this.changeAnswers();
                    return true;
                case EMPTY_STRING:
                    this.stringIsEmpty();
                    return true;
                case SHOW_HELP:
                    this.showHelp();
                    return true;
                case CLOSE_CHAT:
                    this.closeProgram();
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    // shows general random answer for user
    private void showText() {
        textShower.setOutputText(baseDB.getAnswer(DataBase.READ_RANDOM_STRING));
    }

    // changing place where answers are read from
    private void changeAnswers() {
        baseDB.chooseNewTable();
        textShower.setOutputText(CommandWork.ANOTHER_FILE);
    }

    // answer for empty input
    private void stringIsEmpty() {
        textShower.setOutputText(CommandWork.STRING_IS_EMPTY);
    }

    // show help: commands and description
    private void showHelp() {
        Map<String,String> descriptionMap = AllCommands.getCommandsWithDescription();
        descriptionMap.forEach((k,v) -> textShower.setOutputText(k + "\t\t\t\t" + v));
    }

    // shows "goodbye" string (on shutdown only)
    private void closeProgram() {
        textShower.setOutputText(baseDB.getAnswer(DataBase.READ_GOODBYE));
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
