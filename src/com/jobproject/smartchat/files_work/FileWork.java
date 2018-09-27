package com.jobproject.smartchat.files_work;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class FileWork
{
    public static final int READ_RANDOM_STRING      = 0x000000001;      // main method, get random string from file
    public static final int READ_ALL                = 0x000000010;      // for tests
    public static final int READ_WELCOME            = 0x000000011;      // for first "welcome" string from file
    public static final int READ_GOODBYE            = 0x000000100;      // for last "goodbye" string from file

    private String currentFilePath;                                     // "./bin/abc.txt"
    private String currentAnswer;                                       // current randomly chosen string from list
    private ArrayList<String> lstAnswers;                               // list with all strings from file except first and last
    private String welcomeAnswer;                                       // first line from file
    private String goodbyeAnswer;                                       // last line from file
    private String allFileInfo;                                         // for debug

    public FileWork()
    {
        initialize();
    }

    // initializing globals, reading current file
    private void initialize()
    {
        lstAnswers = new ArrayList<>();

        currentAnswer = "no answer initialized";
        setCurrentFile("./bin/abc.txt");       // default
    }

    // choosing path for file to read from
    private void setCurrentFile(String filePath)
    {
        currentFilePath = filePath;
        readAllIntoArray();
    }

    // read all from file, adding answers into current fields
    private void readAllIntoArray()
    {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBufferAll = new StringBuffer();
        //
        try(FileInputStream myFile = new FileInputStream(currentFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(myFile, StandardCharsets.UTF_8);
            Reader reader = new BufferedReader(inputStreamReader))
        {
            int ch;
            ArrayList<String> allAnswers = new ArrayList<>();

            // getting massive with all strings from file
            while ((ch = reader.read()) > -1)
            {
                stringBufferAll.append((char)ch);
                //
                if (ch != '\n')
                {
                    stringBuffer.append((char) ch);
                } else
                {
                    allAnswers.add(stringBuffer.toString());            // save found line
                    stringBuffer.delete(0, stringBuffer.length()+1);    // clear StringBuffer
                }
            }
            // check gotten massive: first line, last line and at list one answer
            try
            {
                if (allAnswers.size() < 3)
                    throw new Exception("there are not enough lines with answers in current file");
            } catch (Exception e2)
            {
                e2.printStackTrace();
            }

            // get all info from file into class field
            allFileInfo = stringBufferAll.toString();

            // get size of answers mass
            int currentMassSize = allAnswers.size();

            // get general answers from gotten massive
            for(int i = 1; i < currentMassSize-1; i++)
                lstAnswers.add(allAnswers.get(i));

            // get first and last lines from gotten massive
            this.welcomeAnswer = allAnswers.get(0);
            this.goodbyeAnswer = allAnswers.get(currentMassSize-1);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // read chosen File
    public String readFile(int flag)
    {
        switch (flag)
        {
            case READ_RANDOM_STRING:
                return readRandom();
            case READ_ALL:
                return readAll();
            case READ_WELCOME:
                return readWelcome();
            case READ_GOODBYE:
                return readGoodbye();
            default:
                return "no flag found";
        }
    }

    // get string with path of chosen file witch program reads from
    public String getCurrentFilePath()
    {
        return currentFilePath;
    }

    // get current variable marked as answer
    public String getCurrentAnswer()
    {
        return currentAnswer;
    }

    // general - get random string from chosen file
    private String readRandom()
    {
        Random rand = new Random();
        return currentAnswer = lstAnswers.get(rand.nextInt(lstAnswers.size()));
    }

    // returns all info from file (debug)
    private String readAll()
    {
        return allFileInfo;
    }

    // returns the first line in file
    private String readWelcome()
    {
        return welcomeAnswer;
    }

    // returns the last line in file
    private String readGoodbye()
    {
        return goodbyeAnswer;
    }

    // write string to chosen file
    public void writeToFile(String output, int flag)
    {
        try(FileOutputStream myFile = new FileOutputStream(currentFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myFile, StandardCharsets.UTF_8);
            Writer out = new BufferedWriter(outputStreamWriter))
        {
            //String myAddress = "my address is here";
            out.write(output);

        } catch (IOException e)
        {
            e.printStackTrace();
            // ..
        }
    }

    // write array to chosen file
    public void writeToFile(ArrayList<String> output, int flag)
    {
        try(FileOutputStream myFile = new FileOutputStream(currentFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myFile, StandardCharsets.UTF_8);
            Writer out = new BufferedWriter(outputStreamWriter))
        {
            //String myAddress = "my address is here";
            output.forEach((str) ->
            {
                try
                {
                    out.append(str);
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            });
            //
        } catch (IOException e)
        {
            e.printStackTrace();
            // ..
        }
    }
}
