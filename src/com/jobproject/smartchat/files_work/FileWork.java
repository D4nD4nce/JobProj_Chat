package com.jobproject.smartchat.files_work;

/*
* General class for work with helping files
* */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FileWork {
    public static final String DEFAULT_ANSWER       = "default answer";
    public static final String DEFAULT_WELCOME      = "default welcome";
    public static final String DEFAULT_GOODBYE      = "default goodbye";

    public static final int READ_RANDOM_STRING      = 1;                // main method, get random string from file
    public static final int READ_ALL                = 2;                // for debug
    public static final int READ_WELCOME            = 3;                // for first "welcome" string from file
    public static final int READ_GOODBYE            = 4;                // for last "goodbye" string from file

    private String currentFilePath;                                     // "./bin/general.txt"
    private String currentAnswer;                                       // current randomly chosen string from list
    private List<String> lstAnswers;                                    // list with all strings from file except first and last
    private String welcomeAnswer;                                       // first line from file
    private String goodbyeAnswer;                                       // last line from file
    private String allFileInfo;                                         // for debug

    public FileWork() {
        initialize();
    }

    // initializing globals, reading current file
    private void initialize() {
        lstAnswers = new ArrayList<>();
        currentAnswer = "default answer";
        setCurrentFile(getRandomFilePath());                                            // setting random helping file for answers
    }

    // choosing path for file to read from
    private void setCurrentFile(String filePath) {
        currentFilePath = filePath;
        readAllIntoArray();                                                             // reading current file and setting all variables
    }

    // choosing new random helping file for user answers
    public void chooseNewFile() {
        setCurrentFile(getRandomFilePath());
    }

    // getting random path for helping user file
    private String getRandomFilePath() {
        Random rand = new Random();
        AllFiles files[] = AllFiles.values();
        int lengh = files.length;
        String currentPath = getCurrentFilePath();                                      // get current path
        String newPath = files[rand.nextInt(lengh-1)].getDescription();          // get new path
        if (currentPath != null && !currentPath.isEmpty()) {
            while(currentPath.compareTo(newPath) == 0) {
                newPath = files[rand.nextInt(lengh-1)].getDescription();         // check for new path, get another if it's the same
            }
        } else {
            newPath = files[rand.nextInt(lengh-1)].getDescription();
        }
        return newPath;
    }

    // read all from file, adding answers into current fields
    private void readAllIntoArray() {
        StringBuffer stringBufferAll = new StringBuffer();
        try(FileInputStream myFile = new FileInputStream(currentFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(myFile, StandardCharsets.UTF_8);
            Reader reader = new BufferedReader(inputStreamReader)) {
            int oneCharCode;
            ArrayList<String> allAnswers;
            while ((oneCharCode = reader.read()) > -1) {
                stringBufferAll.append((char)oneCharCode);                              // getting massive with all strings from file
            }
            allFileInfo = stringBufferAll.toString();                                   // get all info from file into class field
            allAnswers = new ArrayList<>(Arrays.asList(allFileInfo.split("\n"))); // splitting gotten string into list
            lstAnswers.clear();                                                         // clear answers from previous file
            int currentMassSize = allAnswers.size();                                    // get size of answers mass
            if (currentMassSize < 3){
                lstAnswers.add(FileWork.DEFAULT_ANSWER);                                // set defaults in case there're a problem with file
                this.welcomeAnswer = FileWork.DEFAULT_WELCOME;
                this.goodbyeAnswer = FileWork.DEFAULT_GOODBYE;
            } else {
                for(int i = 1; i < currentMassSize-1; i++){
                    lstAnswers.add(allAnswers.get(i));                                  // get general answers from gotten massive
                }
                this.welcomeAnswer = allAnswers.get(0);                                 // get first and last lines from gotten massive
                this.goodbyeAnswer = allAnswers.get(currentMassSize-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get string with path of chosen file witch program reads from
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    // get current variable marked as answer
    public String getCurrentAnswer() {
        return currentAnswer;
    }

    // read chosen File depending on flags
    public String readFile(int flag) {
        switch (flag) {
            case READ_RANDOM_STRING:
                return readRandom();
            case READ_ALL:
                return readAll();
            case READ_WELCOME:
                return readWelcome();
            case READ_GOODBYE:
                return readGoodbye();
        }
        return "";
    }

    // general - get random string from chosen file
    private String readRandom() {
        Random rand = new Random();
        return currentAnswer = lstAnswers.get(rand.nextInt(lstAnswers.size()));
    }

    // returns all info from file (debug)
    private String readAll() {
        return allFileInfo;
    }

    // returns the first line in file
    private String readWelcome() {
        return welcomeAnswer;
    }

    // returns the last line in file
    private String readGoodbye() {
        return goodbyeAnswer;
    }

    // write string to chosen file
    public void writeToFile(String output, int flag) {
        if (output == null)
            return;
        try(FileOutputStream myFile = new FileOutputStream(currentFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myFile, StandardCharsets.UTF_8);
            Writer out = new BufferedWriter(outputStreamWriter)) {
            out.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write array to chosen file
    public void writeToFile(ArrayList<String> output, int flag) {
        if (output == null)
            return;
        try(FileOutputStream myFile = new FileOutputStream(currentFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myFile, StandardCharsets.UTF_8);
            Writer out = new BufferedWriter(outputStreamWriter)) {
            output.forEach((str) -> {
                try {
                    out.append(str);
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}


//    // read all from file, adding answers into current fields
//    private void readAllIntoArray()
//    {
//        StringBuffer stringBuffer = new StringBuffer();
//        StringBuffer stringBufferAll = new StringBuffer();
//        //
//        try(FileInputStream myFile = new FileInputStream(currentFilePath);
//            InputStreamReader inputStreamReader = new InputStreamReader(myFile, StandardCharsets.UTF_8);
//            Reader reader = new BufferedReader(inputStreamReader))
//        {
//            int ch;
//            ArrayList<String> allAnswers = new ArrayList<>();
//
//            // getting massive with all strings from file
//            while ((ch = reader.read()) > -1)
//            {
//                stringBufferAll.append((char)ch);
//                //
//                if (ch != '\n')
//                {
//                    stringBuffer.append((char) ch);
//                } else
//                {
//                    allAnswers.add(stringBuffer.toString());            // save found line
//                    stringBuffer.delete(0, stringBuffer.length()+1);    // clear StringBuffer
//                }
//            }
//            // check gotten massive: first line, last line and at list one answer
//            try
//            {
//                if (allAnswers.size() < 3)
//                    throw new Exception("there are not enough lines with answers in current file");
//            } catch (Exception e2)
//            {
//                e2.printStackTrace();
//            }
//
//            // get all info from file into class field
//            allFileInfo = stringBufferAll.toString();
//
//            // get size of answers mass
//            int currentMassSize = allAnswers.size();
//
//            // get general answers from gotten massive
//            for(int i = 1; i < currentMassSize-1; i++)
//                lstAnswers.add(allAnswers.get(i));
//
//            // get first and last lines from gotten massive
//            this.welcomeAnswer = allAnswers.get(0);
//            this.goodbyeAnswer = allAnswers.get(currentMassSize-1);
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }