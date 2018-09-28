package com.jobproject.smartchat.userinterface;

/*
 * Class encapsulates work with output for user
 * */

public class TextShower
{
    public void setOuputText(String str)
    {
        try
        {
            if (str == null)
                throw new NullPointerException("string for output is null");
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        //
        outputConsole(str);
    }

    private void outputConsole(String str)
    {
        System.out.println(str);
    }

}
