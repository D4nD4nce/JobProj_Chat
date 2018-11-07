package db_work;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * All data base work logic
 *
 */

public class DataBase {
    public static final int READ_RANDOM_STRING          = 1;                // main method, get random string from file
    public static final int READ_WELCOME                = 3;                // for first "welcome" string from file
    public static final int READ_GOODBYE                = 4;                // for last "goodbye" string from file

    private static final String DATABASE_DRIVER         = "jdbc:h2:";
    private static final String DATABASE_PATH           = "./bin/AnswersDataBase";
    private static final String DATABASE_USER           = "root";
    private static final String DATABASE_PASSWORD       = "root";
    private static final String DEFAULT_ANSWER          = "default answer";
    private static final String DEFAULT_WELCOME         = "default welcome";
    private static final String DEFAULT_GOODBYE         = "default goodbye";

    private Connection currentConnection;                                   // opened connection obj
    private String currentTable;                                            // chosen table
    private String currentAnswer;                                           // current randomly chosen string from list
    private List<String> lstAnswers;                                        // list with all strings from file except first and last
    private String welcomeAnswer;                                           // first line from file
    private String goodbyeAnswer;                                           // last line from file

    // general constructor
    public DataBase(){
        initialize();
    }

    // test mode for creating all tables
    public DataBase(int testMode){
        if (testMode > 0) {
            openConnection();
            insertInfo();
        }
    }

    // opening connection, initializing globals, reading current table
    private void initialize() {
        lstAnswers = new ArrayList<>();
        this.currentAnswer = DataBase.DEFAULT_ANSWER;
        this.welcomeAnswer = DataBase.DEFAULT_WELCOME;
        this.goodbyeAnswer = DataBase.DEFAULT_GOODBYE;
        openConnection();
        chooseNewTable();                                                   // choose table and read answers
    }

    // open new connection to DB
    private void openConnection() {
        try {
            currentConnection = DriverManager.getConnection(DataBase.DATABASE_DRIVER + DataBase.DATABASE_PATH,
                    DataBase.DATABASE_USER,
                    DataBase.DATABASE_PASSWORD);
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    // close current DB connection
    private void closeConnection() {
        try{
            if (currentConnection != null){
                currentConnection.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // choosing new random helping table with user answers
    public void chooseNewTable() {
        setCurrentTable(AllTables.getNewRandomTable(currentTable));
    }

    // choosing table and reading answers
    private void setCurrentTable(String tablePath) {
        this.currentTable = tablePath;
        readAllIntoArray();                                                 // reading current Table and setting all variables
    }

    // read all from table, adding answers into current fields
    private void readAllIntoArray() {
        try {
            Statement statement = currentConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ANSWER FROM " + currentTable);
            resultSet.first();
            lstAnswers.clear();
            while (!resultSet.isAfterLast()) {
                if (resultSet.isFirst()) {
                    this.welcomeAnswer = resultSet.getString(1);
                } else if (resultSet.isLast()) {
                    this.goodbyeAnswer = resultSet.getString(1);
                } else {
                    lstAnswers.add(resultSet.getString(1));
                }
                resultSet.next();
            }
            //statement.close();
            //resultSet.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // get current variable marked as answer
    public String getCurrentAnswer() {
        return currentAnswer;
    }

    // get answer depending on flags
    public String getAnswer(int flag) {
        switch (flag) {
            case READ_RANDOM_STRING:
                return readRandom();
            case READ_WELCOME:
                return readWelcome();
            case READ_GOODBYE:
                closeConnection();
                return readGoodbye();
            default:
                return "";
        }
    }

    // general - get random string from chosen Table
    private String readRandom() {
        Random rand = new Random();
        return currentAnswer = lstAnswers.get(rand.nextInt(lstAnswers.size()));
    }

    // returns the first row value from Table
    private String readWelcome() {
        return welcomeAnswer;
    }

    // returns the last row value from Table
    private String readGoodbye() {
        return goodbyeAnswer;
    }

    // creating base tables in chosen DB
    private void insertInfo() {
        try {
            Statement statement = currentConnection.createStatement();
            statement.execute("CREATE TABLE TEST_ANSWERS_1(ID INT, ANSWER VARCHAR(100))");
            statement.execute("CREATE TABLE TEST_ANSWERS_2(ID INT, ANSWER VARCHAR(100))");
            statement.execute("CREATE TABLE TEST_ANSWERS_3(ID INT, ANSWER VARCHAR(100))");
            statement.execute("CREATE TABLE TEST_ANSWERS_4(ID INT, ANSWER VARCHAR(100))");
            statement.execute("INSERT INTO TEST_ANSWERS_1 VALUES (1, 'welcome!'), (2, 'answer1'), (3, 'answer2'), (4, 'answer3'), (5, 'answer4'), (6, 'goodbye!')");
            statement.execute("INSERT INTO TEST_ANSWERS_2 VALUES (1, 'big_welcome!'), (2, 'big_answer1'), (3, 'big_answer2'), (4, 'big_answer3'), (5, 'big_answer4'), (6, 'big_goodbye!')");
            statement.execute("INSERT INTO TEST_ANSWERS_3 VALUES (1, 'welcome to chat'), (2, 'type another thought'), (3, 'nice'), (4, 'good choice'), (6, 'wish you good luck')");
            statement.execute("INSERT INTO TEST_ANSWERS_4 VALUES (1, 'glad to see you'), (2, 'random_1'), (3, 'random_2'), (4, 'random_3'), (6, 'have a nice day')");
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
