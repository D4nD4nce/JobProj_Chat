package com.jobproject.smartchat.commands;

/*
* general enum class for all special commands in program
* includes string keys, enum values and descriptions
 */

import java.util.HashMap;
import java.util.Map;

public enum AllCommands {
    NO_COMMANDS_FOUND,              // no special commands, return random answer
    CHANGE_FILE,                    // change helping file with answers
    CLOSE_CHAT,                     // shutdown program
    EMPTY_STRING,                   // no text
    SHOW_HELP;                      // show available commands

    // static map with all commands: input key, enum value, description
    private static final Map<String,CommandsDescription> commandsMap;
    static {
       commandsMap = new HashMap<>();
       commandsMap.put("/close", new CommandsDescription(CLOSE_CHAT, "close program"));
       commandsMap.put("/change", new CommandsDescription(CHANGE_FILE, "choose another file to get answers"));
       commandsMap.put("/help", new CommandsDescription(SHOW_HELP, "get all commands description"));
    }

    // get all commands with description
    public static Map<String,String> getCommandsWithDescription() {
        Map<String,String> result = new HashMap<>();
        commandsMap.forEach((k,v) -> result.put(k,v.getCommandDescription()));
        return result;
    }

    // checking input for having command
    public static AllCommands commandCheck(String txt) {
        if (txt == null || txt.isEmpty()){
            return EMPTY_STRING;
        }
        return (commandsMap.containsKey(txt)) ? commandsMap.get(txt).getCommandValue() : NO_COMMANDS_FOUND;
    }

    // static nested class encapsulates commands with descriptions
    private static final class CommandsDescription {
        private AllCommands commandValue;
        private String commandDescription;

        CommandsDescription(AllCommands value, String description) {
            commandValue = value;
            commandDescription = description;
        }

        AllCommands getCommandValue() {
            return commandValue;
        }

        String getCommandDescription() {
            return commandDescription;
        }
    }

}
