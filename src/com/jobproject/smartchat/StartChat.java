package com.jobproject.smartchat;

import com.jobproject.smartchat.files_work.FileWork;
import com.jobproject.smartchat.output.TextShower;

public class StartChat {

    public static void main (String args[])
    {
        FileWork file = new FileWork();
        TextShower textShower = new TextShower();
        textShower.textOut(file.readFile(0));
        file.writeToFile(0);
    }
}
