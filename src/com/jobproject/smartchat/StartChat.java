package com.jobproject.smartchat;

import com.jobproject.smartchat.userinterface.GetText;

public class StartChat
{
    public static void main (String args[])
    {
        boolean flag = true;
        String userText;
        GetText getText = new GetText();
        Commands comWork = new Commands();

        while(flag)
        {
            userText = getText.inputConsole();
            flag = comWork.answer(userText);
        }

        //file.writeToFile("some text to input", 0);
    }
}
