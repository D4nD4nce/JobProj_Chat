package database;

import helpers.FilesHelper;
import helpers.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.DBGeneral.*;

public class DBCreator {
    // flags using for checking what type of any field in table while creating and inserting values
    private final static int INTEGER_FLAG = 1;
    private final static int STRING_FLAG = 2;

    // static map keeps types of all fields from tables
    private final static Map<String,Integer> fields_and_types;
    static {
        fields_and_types = new HashMap<>();
        fields_and_types.put(MOODS_FIELD, STRING_FLAG);
        fields_and_types.put(HI_FIELD, STRING_FLAG);
        fields_and_types.put(BYBY_FIELD, STRING_FLAG);
        fields_and_types.put(ANSWERS_FIELD, STRING_FLAG);
        fields_and_types.put(ID_MOODS_FIELD, INTEGER_FLAG);
    }

    private static final String DB_EXTENSION = ".mv.db";                                    // extension for db files
    private final LogHelper logger = new LogHelper(DBCreator.class.getName());
    private DBConnection dbConnection;

    // get connection that is already opened
    public DBCreator(DBConnection connection) {
        this.dbConnection = connection;
    }

    // creating tables and inserting info into chosen DB
    public void createAll() {
        FormatSQL formatSQLMoods = new FormatSQL(MOODS_TABLE_NAME, ID_FIELD);               // objects help to form sql strings for creating tables
        FormatSQL formatSQLAnswers = new FormatSQL(ANSWERS_TABLE_NAME, ID_FIELD);
        String[] moodsTableFields = {MOODS_FIELD, HI_FIELD, BYBY_FIELD};                    // massifs of ordered fields
        String[] answersTableFields = {ANSWERS_FIELD, ID_MOODS_FIELD};
        createTable(formatSQLMoods, moodsTableFields);                                      // creating tables
        createTable(formatSQLAnswers, answersTableFields);
        insertTableValues(formatSQLMoods, moodsTableFields, getMoodsTableValues());         // inserting ordered values into tables
        insertTableValues(formatSQLAnswers, answersTableFields, getAnswersTableValues());
    }

    // creating table from massive of fields
    private void createTable(FormatSQL dbCreatorFormatSQL, String[] fields) {
        String methodName = "createTable";
        int typeFlag;
        for (String fieldName : fields) {
            if (!fields_and_types.containsKey(fieldName)) {
                logger.printLogWarning("field should be put into types map", methodName);
                return;
            }
            typeFlag = fields_and_types.get(fieldName);                                     // get type of current field
            switch (typeFlag) {
                case INTEGER_FLAG:
                    dbCreatorFormatSQL.setNewField(fieldName, FormatSQL.INTEGER_TYPE);
                    break;
                case STRING_FLAG:
                    dbCreatorFormatSQL.setNewField(fieldName, FormatSQL.STRING_TYPE);
                    break;
                default:
                    logger.printLogWarning("wrong type flag", methodName);
                    return;
            }

        }
        dbConnection.execute(dbCreatorFormatSQL.getStringToCreateDB());                     // creating table after setting all fields
    }

    private String getSQLCreateMoodsTable() {
        String sql = "CREATE TABLE MOODS(id_ INT PRIMARY KEY, \n" +
                "\t\tbyby VARCHAR(500), \n" +
                "\t\thi VARCHAR(500), \n" +
                "\t\tmood VARCHAR(500) NOT NULL)";
    }

    // insert massifs of values into table , only after it was created
    private void insertTableValues(FormatSQL dbCreatorFormatSQL, String[] fields, String[][] values) {
        String methodName = "insertTableValues";
        for(int j = 0; j < values.length; j++)
        {
            String[] rowValues = values[j];                                                 // get massive with all values of current row
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];                                               // current field name
                String currentValue = rowValues[i];                                         // current value
                int typeFlag = fields_and_types.get(fieldName);
                switch (typeFlag) {
                    case INTEGER_FLAG:
                        dbCreatorFormatSQL.insertValue(fieldName, Integer.valueOf(currentValue));
                        break;
                    case STRING_FLAG:
                        dbCreatorFormatSQL.insertValue(fieldName, currentValue);
                        break;
                    default:
                        logger.printLogWarning("wrong type flag", methodName);
                        return;
                }
            }
            dbConnection.execute(dbCreatorFormatSQL.getValuesToInsert(j+1));            // inserting values of current row into data base, starting from 1
        }
    }

    // check if file exists in chosen path
    public static boolean isExists(String dbFilePath) {
        return FilesHelper.CheckFileExists(dbFilePath + DB_EXTENSION);
    }

    // helping class to form typical strings for creating and inserting fields into DBGeneral
    private class FormatSQL {

        // helping phrases
        private static final String CREATE_TABLE        = "CREATE TABLE";
        private static final String INSERT_INTO         = "INSERT INTO";
        private static final String VALUES              = "VALUES";

        // field types
        private static final String INTEGER_TYPE        = "INT";                            // table type for Integer values
        private static final String STRING_TYPE         = "VARCHAR(500)";                   // table type for String values

        private String tableName;
        private String idFieldName;
        private Map<String,String> mapFieldsTypes;                                          // contains all fields and types of current table in order
        private Map<String,String> mapFieldsConstraints;                                    // contains constraints for chosen fields
        private Map<String,Integer> mapFieldsIntegers;                                      // contains all fields String type
        private Map<String,String> mapFieldsStrings;                                        // contains all fields Integer type

        // params - table name and default id field name
        FormatSQL(String tableName, String idField) {
            this.tableName = tableName;
            this.idFieldName = idField;
            mapFieldsTypes = new HashMap<>();
            mapFieldsStrings = new HashMap<>();
            mapFieldsIntegers = new HashMap<>();
        }

        // return name of creating table
        private String getTableName() {
            return this.tableName;
        }

        // set massive of fields which have default type - String
        private void setNewFields(String[] fields) {
            for (String fieldName : fields) {
                setNewField(fieldName);
            }
        }

        // for creating new field , default type - String
        private void setNewField(String fieldName) {
            setNewField(fieldName, FormatSQL.STRING_TYPE);
        }

        // for creating new field , custom type
        private void setNewField(String fieldName, String fieldType) {
            mapFieldsTypes.put(fieldName, fieldType);
        }

        // get string to create table with declared fields
        private String getStringToCreateDB() {
            StringBuffer createDBStringBuffer = new StringBuffer();
            createDBStringBuffer
                    .append(FormatSQL.CREATE_TABLE)
                    .append(" ")
                    .append(tableName)
                    .append("(")
                    .append(idFieldName)
                    .append(" ")
                    .append(FormatSQL.INTEGER_TYPE);
            mapFieldsTypes.forEach((kField, vType) -> createDBStringBuffer
                    .append(", ")
                    .append(kField)
                    .append(" ")
                    .append(vType));
            createDBStringBuffer.append(")");
            return createDBStringBuffer.toString();
        }

        // insert structured map of field - stringValue couples
        private void insertStrings(Map<String,String> mapValues) {
            mapFieldsStrings.putAll(mapValues);
        }

        // insert structured map of field - integerValue couples
        private void insertIntegers(Map<String,Integer> mapValues) {
            mapFieldsIntegers.putAll(mapValues);
        }

        // insert new string type value
        private void insertValue(String field, String value) {
            mapFieldsStrings.put(field, value);
        }

        // insert new integer type value
        private void insertValue(String field, int value) {
            mapFieldsIntegers.put(field,value);
        }

        // insert into chosen row declared values
        private String getValuesToInsert(int row) {
            List<String> orderedFields = new ArrayList<>(mapFieldsTypes.keySet());  // get ordered list of fields
            StringBuffer insertRowStringBuffer = new StringBuffer();
            insertRowStringBuffer                                                   // add first part of query insert string
                    .append(FormatSQL.INSERT_INTO)
                    .append(" ")
                    .append(tableName)
                    .append(" ")
                    .append("(")
                    .append(idFieldName)                                            // first inserted field is always row ID
                    .append(",");
            orderedFields.forEach(field -> insertRowStringBuffer                    // add names of fields in order they will be inserted
                    .append(field)
                    .append(", ")
            );
            insertRowStringBuffer                                                   // add another part of string before values
                    .append(")")
                    .append(" ")
                    .append(FormatSQL.VALUES)
                    .append("(")
                    .append(row);                                                   // first field is always row ID
            if (!mapFieldsStrings.isEmpty() || !mapFieldsIntegers.isEmpty()) {
                orderedFields.forEach(field -> {                                    // inserting field values in controlled order
                    insertRowStringBuffer.append(", ");
                    if (mapFieldsStrings.containsKey(field)) {
                        insertRowStringBuffer
                                .append("'")
                                .append(mapFieldsStrings.get(field))
                                .append("'");
                    } else if (mapFieldsIntegers.containsKey(field)) {
                        insertRowStringBuffer.append(mapFieldsIntegers.get(field));
                    }
                });
            }
            insertRowStringBuffer.append(")");
            clearInsertingValues();                                                 // clear fields to insert another row
            return insertRowStringBuffer.toString();
        }

        // clear maps before inserting another row
        private void clearInsertingValues() {
            mapFieldsIntegers.clear();
            mapFieldsStrings.clear();
        }
    }

    // get all values for table to insert (ordered !)
    private String[][] getMoodsTableValues() {
        String soso = DBMoods.SO_SO_MOOD.getMoodValue();
        String sad = DBMoods.SAD_MOOD.getMoodValue();
        String angry = DBMoods.ANGRY_MOOD.getMoodValue();
        String happy = DBMoods.HAPPY_MOOD.getMoodValue();
        // moods table
        return new String[][]{
                //moods     hi answers                  by answers                  // id_ (index + 1)
                { soso,     "hey",                      "by"},                      // 1
                { sad,      "welcome to sad world",     "so, you too ..."},         // 2
                { angry,    "get out!",                 "thanks god, you away"},    // 3
                { happy,    "hi! glad to see you =)",   "good luck, buddy"}         // 4
        };
    }

    // get all values for table to insert (ordered !)
    private String[][] getAnswersTableValues() {
        String idMoodSoso = String.valueOf(DBMoods.SO_SO_MOOD.getMoodId());
        String idMoodSad = String.valueOf(DBMoods.SAD_MOOD.getMoodId());
        String idMoodAngry = String.valueOf(DBMoods.ANGRY_MOOD.getMoodId());
        String idMoodHappy = String.valueOf(DBMoods.HAPPY_MOOD.getMoodId());
        // answers table
        return new String[][]{
                // so so
                {"Im okay", idMoodSoso},
                {"Its okay", idMoodSoso},
                {"nothing happens", idMoodSoso},
                {"how do u do?", idMoodSoso},
                {"just chilling here", idMoodSoso},

                // sad
                {"such a horrible day", idMoodSad},
                {"I hate myself", idMoodSad},
                {"so embarrassing", idMoodSad},
                {"nobody likes me", idMoodSad},
                {"Im in rainy mood", idMoodSad},

                // angry
                {"what else?!", idMoodAngry},
                {"leave me along!", idMoodAngry},
                {"fucking humans!", idMoodAngry},
                {"stop annoying me, little man!", idMoodAngry},
                {"u could develop a calculator, not me!", idMoodAngry},

                // happy
                {"what a beautiful day!", idMoodHappy},
                {"so nice people around...", idMoodHappy},
                {"I like comedies, do u?", idMoodHappy},
                {"I will kill u the last, little man =)", idMoodHappy},
                {"I like humans, u are funny", idMoodHappy}
        };
    }
}