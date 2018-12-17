package database;

import static database.DBGeneral.*;

public class DBRequests {

    /*
     * all values are static final const
     * table name
     * fields
     */
    public static String createMoodsTable() {
        return "CREATE TABLE " + MOODS_TABLE_NAME + "(" +
                ID_FIELD + " " + DBCreator.INTEGER_TYPE + " PRIMARY KEY, " +
                BYBY_FIELD + " " + DBCreator.STRING_TYPE + ", " +
                HI_FIELD + " " + DBCreator.STRING_TYPE + ", " +
                MOODS_FIELD + " " + DBCreator.STRING_TYPE + " NOT NULL)";
    }

    public static String createRandomAnswersTable() {
        return "CREATE TABLE " + ANSWERS_TABLE_NAME + "(" +
                ID_FIELD + " " + DBCreator.INTEGER_TYPE + " PRIMARY KEY, " +
                ANSWERS_FIELD + " " + DBCreator.STRING_TYPE + ", NOT NULL " +
                ID_MOODS_FIELD + " " + DBCreator.INTEGER_TYPE + ", NOT NULL " +
                " FOREIGN KEY (" + ID_MOODS_FIELD + ") REFERENCES " + MOODS_TABLE_NAME + "(" + ID_FIELD + "))"; // constraint
    }

    /*
     * count rows in chosen table
     * id field name - const
     */
    public static String getRowsCount(String tableName, String id_field) {
        return  "SELECT COUNT(" + ID_FIELD +") FROM" + tableName;
    }

    /*
     * count all rows with ID of chosen type (mood)
     * table name - const
     * fields names - const
     */
    public static String getCountOfChosenTypeRows(int id_type) {
        return "SELECT COUNT(" + ID_FIELD + ")" + " FROM " + ANSWERS_TABLE_NAME +
                " WHERE " + ID_MOODS_FIELD + " = " + id_type;
    }

    /*
     * get bunch of answers : choose type (mood), limit of values and offset (for random bunch)
     * table name - const
     * fields names - const
     */
    public static String getAnswers_typeLimitOffset(int id_type, int limit, int offset) {
        return "SELECT " + ANSWERS_FIELD + " FROM " + ANSWERS_TABLE_NAME +
                " WHERE " + ID_MOODS_FIELD + " = " + id_type +
                " ORDER BY " + ID_FIELD +
                " LIMIT " + limit +
                " OFFSET " + offset;
    }

    // get value from any field/fields of moods table in chosen type row
    // table name = const
    public static String getMoodsTableValue(String field_name, int id_type) {
        return "SELECT " + field_name + " FROM " + MOODS_TABLE_NAME +
                " WHERE " + ID_FIELD + " = " + id_type;
    }
}
