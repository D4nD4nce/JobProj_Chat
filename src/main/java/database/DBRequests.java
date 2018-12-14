package database;

import static database.DBGeneral.*;

public class DBRequests {

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
     * get all answers with ID of chosen type (mood) which have id > min_id
     * result is limited
     * table name - const
     * fields names - const
     */
    public static String getAnswersOfChosenType(int id_type, int min_id, int limit) {
        return "SELECT " + ANSWERS_FIELD + " FROM " + ANSWERS_TABLE_NAME +
                " WHERE " + ID_MOODS_FIELD + " = " + id_type +
                " AND id_ > " + min_id +
                " ORDER BY " + ID_FIELD +
                " LIMIT" + limit;
    }

    /*
     * get the least row id from all which have chosen type
     * table name - const
     * fields names - const
     */
    public static String getSmallestIdInType(int id_type) {
        return "SELECT MIN(" + ID_FIELD + ")" + " FROM " + ANSWERS_TABLE_NAME +
                " WHERE " + ID_MOODS_FIELD + " = " + id_type;
    }

    // counting rows in chosen table
//    public static int getRowsCount(String tableName) {
//        String methodName = "getRowsCount";
//        if (tableName == null || tableName.isEmpty()) {
//            logger.printLogWarning("wrong parameter value", methodName);
//            return 0;
//        }
//        String sql = "SELECT COUNT(" + ID_FIELD +") FROM" + tableName;
//        ResultSet resultSet = executeQuery(sql);
//        int rowsCount = 0;
//        try {
//            rowsCount = resultSet.getInt(1);                        // get result from field with chosen index
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return rowsCount;
//    }
}
