package database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * All data base work logic
 */

public class DBGeneral {
    // ----------- commands -----------
    public static final int READ_RANDOM_STRING          = 1;                // main method, get random string from file
    public static final int READ_WELCOME                = 2;                // for first "welcome" string from file
    public static final int READ_GOODBYE                = 3;                // for last "goodbye" string from file

    // ----------- defaults -----------
    private static final String DEFAULT_ANSWER          = "default answer";
    private static final String DEFAULT_WELCOME         = "default welcome";
    private static final String DEFAULT_GOODBYE         = "default goodbye";

    // ----------- connection -----------
    public static final String DATABASE_DRIVER          = "jdbc:h2:";
    public static final String DATABASE_PATH            = "./bin/Answers";   // default path
    public static final String DATABASE_USER            = "root";
    public static final String DATABASE_PASSWORD        = "root";

    // ----------- tables -----------
    public static final String ANSWERS_TABLE_NAME       = "random_answers";
    public static final String MOODS_TABLE_NAME         = "moods";

    // ----------- row ID -----------
    public static final String ID_FIELD                 = "id_";             // name of id field (the same for all tables)

    // ----------- fields table 1 -----------
    public static final String ANSWERS_FIELD            = "answer";
    public static final String ID_MOODS_FIELD           = "id_mood";

    // ----------- fields table 2 -----------
    public static final String MOODS_FIELD              = "mood";
    public static final String HI_FIELD                 = "hi";
    public static final String BYBY_FIELD               = "byby";

    private DBConnection currentConnection;
    private DBMoods currentMood;
    private String currentAnswer;                                           // current randomly chosen answer
    private List<String> lstRandomAnswers;                                  // list with current random answers
    private String welcomeAnswer;                                           // "welcome" answer from current bunch
    private String goodbyeAnswer;                                           // "goodbye" answer from current bunch

    // general constructor
    public DBGeneral(){
        initialize();
    }

    // opening connection, initializing globals, reading current table
    private void initialize() {
        lstRandomAnswers = new ArrayList<>();
        this.currentAnswer = DBGeneral.DEFAULT_ANSWER;
        this.welcomeAnswer = DBGeneral.DEFAULT_WELCOME;
        this.goodbyeAnswer = DBGeneral.DEFAULT_GOODBYE;
        currentConnection = new DBConnection();
        if (!DBCreator.isExists(DATABASE_PATH)) {
            createDB();
        }
        currentConnection.openConnection();
        chooseNewAnswers();                                                 // choosing and reading answers
    }

    // creates data base if it doesn't exists
    private void createDB() {
        currentConnection.openConnection();
        DBCreator dbCreator = new DBCreator(currentConnection);
        dbCreator.createAll();
        currentConnection.closeConnection();
    }

    // choosing another bunch of answers
    public void chooseNewAnswers() {
        this.currentMood = DBMoods.getRandomMood(currentMood);
        readAllIntoArray();                                                 // reading current table and setting all variables
    }

    // getting random bunch of answers
    private int getNewAnswers(int oldRow) {

        // count all rows with ID of chosen mood
        String sql = "SELECT COUNT(" + ID_FIELD +") FROM" + ANSWERS_TABLE_NAME + " WHERE " + ID_MOODS_FIELD + " = ";// + moodID;
        ResultSet resultSet = currentConnection.executeQuery(sql);
//        Random rand = new Random();
//        int newRow = rand.nextInt(rowsCount);
//        while ((newRow == oldRow) || (newRow == 0 )) {
//            newRow = rand.nextInt(rowsCount);
//        }
        return oldRow;
    }

    // read all from table, adding answers into current fields
    private void readAllIntoArray() {
        /*
         * 1. get count of chosen type answers (allValues)
         * 2. compare with limit (limit)
         * 3. if it's bigger then limit:
         *  - get min id in ordered answers of current type
         *  - get random value that is  < (allValues - limit)
         *  - get bunch of answers starting from random id of current type and with limit
         * 4. if it's less or equals limit:
         *  - get bunch of chosen type answers
         * 5. get hi and by answers
         */

//        try {
//            Statement statement = currentConnection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT " + StringHelper.arrayToString(AllFieldsAnswer.getFieldsList()) +
//                    " FROM " + DBGeneral.ANSWER_TABLE_NAME +
//                    " WHERE " + FormatSQL.ID_FIELD + " = " + currentRow);
//
//            lstRandomAnswers.clear();
//            resultSet.first();
//            this.welcomeAnswer = resultSet.getString(AllFieldsAnswer.WELCOME.name());
//            this.goodbyeAnswer = resultSet.getString(AllFieldsAnswer.GOODBYE.name());
//            lstRandomAnswers.add(resultSet.getString(AllFieldsAnswer.RANDOM_1.name()));
//            lstRandomAnswers.add(resultSet.getString(AllFieldsAnswer.RANDOM_2.name()));
//            lstRandomAnswers.add(resultSet.getString(AllFieldsAnswer.RANDOM_3.name()));
//            //statement.close();
//            //resultSet.close();
//        } catch (SQLException | NullPointerException e) {
//            e.printStackTrace();
//        }
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
                currentConnection.closeConnection();
                return readGoodbye();
            default:
                return "";
        }
    }

    // general - get random string from chosen Table
    private String readRandom() {
        if (!lstRandomAnswers.isEmpty()) {
            Random rand = new Random();
            currentAnswer = lstRandomAnswers.get(rand.nextInt(lstRandomAnswers.size()));
        }
        return currentAnswer;
    }

    // returns "welcome" value
    private String readWelcome() {
        return welcomeAnswer;
    }

    // returns "goodbye" value
    private String readGoodbye() {
        return goodbyeAnswer;
    }
}