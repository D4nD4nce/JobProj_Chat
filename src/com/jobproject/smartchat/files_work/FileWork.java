package com.jobproject.smartchat.files_work;

import com.jobproject.smartchat.output.TextShower;

import java.io.*;

public class FileWork
{
    private String currentAnswer;
    private FileReader fileReader;
    private StringBuffer stringBuffer;

    public FileWork()
    {
        initialize();
    }

    private void initialize()
    {
        stringBuffer = new StringBuffer();

        try(FileInputStream myFile = new FileInputStream("./bin/abc.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(myFile, "UTF8");
            Reader reader = new BufferedReader(inputStreamReader))
        {
            int ch;
            //
            while ((ch = reader.read()) > -1)
                stringBuffer.append((char)ch);
            //
            String result = stringBuffer.toString();

            new TextShower(result);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
