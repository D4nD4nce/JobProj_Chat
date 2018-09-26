package com.jobproject.smartchat.files_work;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileWork
{
    private String currentAnswer;
    private StringBuffer stringBuffer;

    public FileWork()
    {
        initialize();
    }

    private void initialize()
    {
        stringBuffer = new StringBuffer();
    }

    public String readFile(int flag)
    {

        try(FileInputStream myFile = new FileInputStream("./bin/abc.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(myFile, StandardCharsets.UTF_8);
            Reader reader = new BufferedReader(inputStreamReader))
        {
            int ch;
            //
            while ((ch = reader.read()) > -1)
                stringBuffer.append((char)ch);
            //
            currentAnswer = stringBuffer.toString();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return currentAnswer;
    }

    public void writeToFile(String input, int flag)
    {
        try(FileOutputStream myFile = new FileOutputStream("./bin/abcd.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myFile, StandardCharsets.UTF_8);
            Writer out = new BufferedWriter(outputStreamWriter);)
        {
            //String myAddress = "my address is here";
            out.write(input);

        } catch (IOException e)
        {
            e.printStackTrace();
            // ..
        }
    }
}
