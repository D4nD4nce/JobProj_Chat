package database;

import helpers.NumberHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    // ----------- limit -----------
    private static final int SELECTION_MAX_LIMIT        = 5;                // limit for number of rows in sql requests
    private static final int SELECTION_MIN_LIMIT        = 10;               // bunch of answers should be bigger or equals this value to separate sql requests

    private DBConnection currentConnection;
    private DBTypesKeys currentType;
    private String currentAnswer;                                           // current randomly chosen answer
    private List<String> lstRandomAnswers;                                  // list with current random answers
    private String hiAnswer;                                                // "welcome" answer from current bunch
    private String byAnswer;                                                // "goodbye" answer from current bunch
    private AnswerCounter answerCounter;                                    // helps to count answers if they are separated

    // general constructor
    public DBGeneral(){
        initialize();
    }

    // opening connection, initializing globals, reading current table
    private void initialize() {
        lstRandomAnswers = new ArrayList<>();
        answerCounter = new AnswerCounter();
        this.currentAnswer = DBGeneral.DEFAULT_ANSWER;
        this.hiAnswer = DBGeneral.DEFAULT_WELCOME;
        this.byAnswer = DBGeneral.DEFAULT_GOODBYE;
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
        this.currentType = DBTypesKeys.getRandomType(currentType);
        readAnswersFromDB();                                                 // reading current table and setting all variables
    }

    // read answers from table, adding answers into current fields
    private void readAnswersFromDB() {
        /*
         * 1. get count of chosen type answers (allValues)
         * 2. compare with limit (limit)
         * 3. if it's bigger then limit:
         *  - get random value that is  < (allValues - limit)
         *  - get bunch of answers with random value offset of current type and with limit
         * 4. if it's less or equals limit:
         *  - get bunch of chosen type answers
         * 5. get hi and by answers
         */
        lstRandomAnswers.clear();
        answerCounter.clear();
        try {
            String requestForCountRows = DBRequests.getCountOfChosenTypeRows(currentType.getTypeId());
            ResultSet resultSetAnswersCount = currentConnection.executeQuery(requestForCountRows);      // get count of chosen type answers
            int answersCount = resultSetAnswersCount.getInt(1);
            ResultSet resultSetAnswers;
            int randomOffset = 0;
            // less answers then limit - taking all from DB
            // more answers then limit - taking only limited bunch with random offset
            if (answersCount >= SELECTION_MIN_LIMIT) {
                int maxBound = answersCount - SELECTION_MAX_LIMIT;
                randomOffset = NumberHelper.getRandomValue(0, maxBound, -1);
                answerCounter.setCounter(SELECTION_MAX_LIMIT);                                          // set counter for answers
            }
            String requestRandomAnswers = DBRequests.getAnswers_typeLimitOffset(currentType.getTypeId(), SELECTION_MAX_LIMIT, randomOffset);
            resultSetAnswers = currentConnection.executeQuery(requestRandomAnswers);
            while(!resultSetAnswers.isAfterLast()) {                                                    // get random answers
                lstRandomAnswers.add(resultSetAnswers.getString(1));
                resultSetAnswers.next();
            }
            String requestTwoFields = DBRequests.getMoodsTableValue(HI_FIELD + "," + BYBY_FIELD, currentType.getTypeId());
            ResultSet resultSetHiByAnswers = currentConnection.executeQuery(requestTwoFields);          // get hi and by answers
            hiAnswer = resultSetHiByAnswers.getString(HI_FIELD);
            byAnswer = resultSetHiByAnswers.getString(BYBY_FIELD);
            resultSetHiByAnswers.close();
            resultSetAnswersCount.close();
            resultSetAnswers.close();
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
                return getRandom();
            case READ_WELCOME:
                return getWelcome();
            case READ_GOODBYE:
                currentConnection.closeConnection();
                return getGoodbye();
            default:
                return "";
        }
    }

    // general - get random string from chosen Table, check for counter and getting new answers if they are separated
    private String getRandom() {
        if (!lstRandomAnswers.isEmpty()) {
            int oldIndex = (lstRandomAnswers.contains(currentAnswer)) ? lstRandomAnswers.indexOf(currentAnswer) : -1;
            int maxRandom = lstRandomAnswers.size() - 1;
            currentAnswer = lstRandomAnswers.get(NumberHelper.getRandomValue(0, maxRandom, oldIndex));
            if (answerCounter.isActivated()) {
                answerCounter.doCount();
                if (answerCounter.isTriggered()) {
                    readAnswersFromDB();                                                                // get new bunch of random answers without changing type
                }
            }
        }
        return currentAnswer;
    }

    // returns hi value
    private String getWelcome() {
        return hiAnswer;
    }

    // returns by value
    private String getGoodbye() {
        return byAnswer;
    }

    /*
     * helps to control number of answers in case of separation
     * counts how many answers have been shown to find out when to get new one's from dataBase
     */
    private class AnswerCounter {
        private int counter;
        private boolean activated = false;
        private boolean trigger = false;

        private void setCounter(int count) {
            this.counter = count;
            if(isPositive(count)) {
                activated = true;
            }
        }

        private void doCount() {
            if (activated) {
                counter -= 1;
                if (!isPositive(counter)) {
                    trigger = true;
                }
            }
        }

        private void clear() {
            activated = false;
            trigger = false;
            counter = 0;
        }

        private int getCounter() {
            return counter;
        }

        private boolean isActivated() {
            return activated;
        }

        private boolean isTriggered() {
            return trigger;
        }

        private boolean isPositive(int count) {
            return count > 0;
        }
    }
}