package database;

import helpers.FilesHelper;
import helpers.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.DBGeneral.*;

public class DBCreator {
    // static map keeps types of all fields from tables
//    private final static Map<String,String> fields_and_types;
//    static {
//        fields_and_types = new HashMap<>();
//        fields_and_types.put(MOODS_FIELD, FormatSQL.STRING_TYPE);
//        fields_and_types.put(HI_FIELD, FormatSQL.STRING_TYPE);
//        fields_and_types.put(BYBY_FIELD, FormatSQL.STRING_TYPE);
//        fields_and_types.put(ANSWERS_FIELD, FormatSQL.STRING_TYPE);
//        fields_and_types.put(ID_MOODS_FIELD, FormatSQL.INTEGER_TYPE);
//    }

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

        String[] answersTableFields = {ANSWERS_FIELD, ID_MOODS_FIELD};                      // ordered fields
        String[] moodsTableFields = {MOODS_FIELD, HI_FIELD, BYBY_FIELD};

        formatSQLMoods.setNewField(MOODS_FIELD, FormatSQL.STRING_TYPE, "NOT NULL");// creating tables
        formatSQLMoods.setNewField(HI_FIELD, FormatSQL.STRING_TYPE, null);
        formatSQLMoods.setNewField(BYBY_FIELD, FormatSQL.STRING_TYPE, null);
        dbConnection.execute(formatSQLMoods.getStringToCreateDB(null));

        formatSQLAnswers.setNewField(ANSWERS_FIELD, FormatSQL.STRING_TYPE, "NOT NULL");
        formatSQLAnswers.setNewField(ID_MOODS_FIELD, FormatSQL.INTEGER_TYPE, null);
        String reference = "FOREIGN KEY (" + ID_MOODS_FIELD + ")" +
                " REFERENCES " + MOODS_TABLE_NAME +
                "(" + ID_FIELD + ")";
        dbConnection.execute(formatSQLAnswers.getStringToCreateDB(reference));              // add reference to another table

        insertTableValues(formatSQLMoods, moodsTableFields, getMoodsTableValues());         // inserting ordered values into tables
        insertTableValues(formatSQLAnswers, answersTableFields, getAnswersTableValues());
    }

    // insert massifs of values into table , only after it was created
    private void insertTableValues(FormatSQL dbCreatorFormatSQL, String[] fields, String[][] values) {
        for(int j = 0; j < values.length; j++)
        {
            String[] rowValues = values[j];                                                 // get massive with all values of current row
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];                                               // current field name
                String currentValue = rowValues[i];                                         // current value
                dbCreatorFormatSQL.insertValue(fieldName, currentValue);
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
        private static final String PRIMARY_KEY         = "PRIMARY KEY";

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
            mapFieldsConstraints = new HashMap<>();
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

        // for creating new field , default type - String, no constraint
        private void setNewField(String fieldName) {
            setNewField(fieldName, FormatSQL.STRING_TYPE, null);
        }

        // for creating new field , custom type
        private void setNewField(String fieldName, String type, String constraint) {
            mapFieldsTypes.put(fieldName, type);
            if (constraint != null && !constraint.isEmpty()) {
                mapFieldsConstraints.put(fieldName, constraint);
            }
        }

        // get string to create table with declared fields
        private String getStringToCreateDB(String addings) {
            StringBuffer createDBStringBuffer = new StringBuffer();
            createDBStringBuffer
                    .append(FormatSQL.CREATE_TABLE)
                    .append(" ")
                    .append(tableName)
                    .append("(")
                    .append(idFieldName)
                    .append(" ")
                    .append(FormatSQL.INTEGER_TYPE)
                    .append(" ")
                    .append(FormatSQL.PRIMARY_KEY);
            mapFieldsTypes.forEach((kField, vType) -> {                             // add all table's fields
                createDBStringBuffer
                    .append(", ")
                    .append(kField)
                    .append(" ")
                    .append(vType);
                if(mapFieldsConstraints.containsKey(kField)) {                      // add constraint for a field
                    createDBStringBuffer
                            .append(" ")
                            .append(mapFieldsConstraints.get(kField));
                }
            });
            if (addings != null && !addings.isEmpty()) {                            // add reference
                createDBStringBuffer
                        .append(", ")
                        .append(addings);
            }
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

        // insert new value (any type)
        private void insertValue(String field, String value) {
            switch (mapFieldsTypes.get(field)) {
                case FormatSQL.STRING_TYPE:
                    insertStringValue(field, value);
                    break;
                case FormatSQL.INTEGER_TYPE:
                    insertIntegerValue(field, Integer.valueOf(value));
                    break;
            }
        }

        // insert new string type value
        private void insertStringValue(String field, String value) {
            mapFieldsStrings.put(field, value);
        }

        // insert new integer type value
        private void insertIntegerValue(String field, int value) {
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
        String soso = DBTypesKeys.SO_SO_MOOD.getMoodValue();
        String sad = DBTypesKeys.SAD_MOOD.getMoodValue();
        String angry = DBTypesKeys.ANGRY_MOOD.getMoodValue();
        String happy = DBTypesKeys.HAPPY_MOOD.getMoodValue();
        // moods table
        return new String[][]{
                //moods     hi answers                  by answers                  // id_ (enum index + 1)
                { soso,     "hey",                      "by"},                      // 1
                { sad,      "welcome to sad world",     "so, you too ..."},         // 2
                { angry,    "get out!",                 "thanks god, you away"},    // 3
                { happy,    "hi! glad to see you =)",   "good luck, buddy"}         // 4
        };
    }

    // get all values for table to insert (ordered !)
    private String[][] getAnswersTableValues() {
        String idMoodSoso = String.valueOf(DBTypesKeys.SO_SO_MOOD.getMoodId());
        String idMoodSad = String.valueOf(DBTypesKeys.SAD_MOOD.getMoodId());
        String idMoodAngry = String.valueOf(DBTypesKeys.ANGRY_MOOD.getMoodId());
        String idMoodHappy = String.valueOf(DBTypesKeys.HAPPY_MOOD.getMoodId());
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