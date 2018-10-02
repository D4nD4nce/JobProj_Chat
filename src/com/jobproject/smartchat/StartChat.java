package com.jobproject.smartchat;

import com.jobproject.smartchat.commands.CommandWork;
import com.jobproject.smartchat.userinterface.GetText;

public class StartChat {
    public static void main (String args[]) {
        boolean flag = true;
        GetText getText = new GetText();                    // class to get user input
        CommandWork comWork = new CommandWork();            // class to interpret user commands and execute them

        while(flag){
            flag = comWork.answer(getText.getInputText());  // take input in cycle while user writes anything until activating shutdown command
        }
    }
}
