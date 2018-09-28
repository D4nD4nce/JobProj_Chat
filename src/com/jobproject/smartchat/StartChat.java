package com.jobproject.smartchat;

import com.jobproject.smartchat.commands.CommandWork;
import com.jobproject.smartchat.userinterface.GetText;

public class StartChat
{
    public static void main (String args[])
    {
        boolean flag = true;
        // class to get user input
        GetText getText = new GetText();
        // class to interpret user commands and execute them
        CommandWork comWork = new CommandWork();

        // take input in cycle while user writes anything until activating shutdown command
        while(flag)
        {
            flag = comWork.answer(getText.inputConsole());
        }

        //file.writeToFile("some text to input", 0);
    }
}
