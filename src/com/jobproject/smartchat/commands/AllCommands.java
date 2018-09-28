package com.jobproject.smartchat.commands;

import java.util.HashMap;
import java.util.Map;

public enum AllCommands
{
    NO_COMMANDS_FOUND,              // no special commands, return random answer
    CHANGE_FILE,                    // change helping file with answers
    CLOSE_CHAT,                     // shutdown program
    EMPTY_STRING,                   // no text
    SHOW_HELP;                      // show available commands

    // static map with all commands: input key, enum value, description
    private static final Map<String,CommandsDescription> commandsMap = Map.ofEntries(
            Map.entry("/close", new CommandsDescription(CLOSE_CHAT, "close program")),
            Map.entry("/change", new CommandsDescription(CHANGE_FILE, "choose another file to get answers")),
            Map.entry("/help", new CommandsDescription(SHOW_HELP, "get all commands description"))
    );

    // checking input for having command
    public static AllCommands commandCheck(String txt)
    {
        try
        {
            if (txt == null)
                throw new NullPointerException("null string to check");
            //
            if (txt.isEmpty())
                return EMPTY_STRING;

        } catch (NullPointerException eNull)
        {
            eNull.printStackTrace();
        }
        //
        if(commandsMap.containsKey(txt))
            return commandsMap.get(txt).getCommandValue();
        //
        return NO_COMMANDS_FOUND;
    }

    // get all commands with description
    public static Map<String,String> getCommandsWithDescription()
    {
        Map<String,String> result = new HashMap<>();
        commandsMap.forEach((k,v) -> result.put(k,v.getCommandDescription()));
        //
        return result;
    }

    // static nested class encapsulates commands with descriptions
    private static final class CommandsDescription
    {
        private AllCommands commandValue;
        private String commandDescription;

        CommandsDescription(AllCommands value, String description)
        {
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
