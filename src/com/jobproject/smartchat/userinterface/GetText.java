package com.jobproject.smartchat.userinterface;

import java.util.Scanner;

public class GetText
{
    public String inputConsole ()
    {
        Scanner input = new Scanner(System.in);
        String result;
        if (input.hasNextLine() && !(result = input.nextLine()).isEmpty())
            return result;
        //
        return "";
    }
}
