package database;

import helpers.LogHelper;

import java.sql.*;

import static database.DBGeneral.*;

public class DBConnection {
    private final LogHelper logger = new LogHelper(DBConnection.class.getName());
    private Connection currentConnection;

    // open new connection to DB
    public void openConnection() {
        try {
            currentConnection = DriverManager.getConnection(DATABASE_DRIVER + DATABASE_PATH,
                    DATABASE_USER,
                    DATABASE_PASSWORD);
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    // close current DB connection
    public void closeConnection() {
        try{
            if (currentConnection != null){
                currentConnection.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Statement getStatement() throws SQLException {
        return currentConnection.createStatement();
    }

    public void execute(String sqlString) {
        try {
            Statement statement = getStatement();
            if(statement == null) {
                logger.printLogWarning("nullable statement", "execute");
                return;
            }
            statement.execute(sqlString);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sqlString) {
        ResultSet resultSet = null;
        try {
            Statement statement = getStatement();
            if(statement == null) {
                logger.printLogWarning("nullable statement", "executeQuery");
                return null;
            }
            resultSet = statement.executeQuery(sqlString);                      // using for creating tables or inserting/updating values
            resultSet.first();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // counting rows in chosen table
    public int getRowsCount(String tableName) {
        String methodName = "getRowsCount";
        if (tableName == null || tableName.isEmpty()) {
            logger.printLogWarning("wrong parameter value", methodName);
            return 0;
        }
        String sql = "SELECT COUNT(" + ID_FIELD +") FROM" + tableName;
        ResultSet resultSet = executeQuery(sql);
        int rowsCount = 0;
        try {
            rowsCount = resultSet.getInt(1);                        // get result from field with chosen index
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsCount;
    }

    // count all rows with ID of chosen mood
    public int getCountOfChosenMoodRows(DBMoods mood) {
        String methodName = "getCountOfChosenMoodRows";
        if (mood == null) {
            logger.printLogWarning("wrong parameter value", methodName);
            return 0;
        }
        String sql = "SELECT COUNT(" + ID_FIELD +")" +
                " FROM " + ANSWERS_TABLE_NAME +
                " WHERE " + ID_MOODS_FIELD + " = " + mood.getMoodId();
        ResultSet resultSet = executeQuery(sql);
        int rowsMoodCount = 0;
        try {
            rowsMoodCount = resultSet.getInt(1);                    // get result from field with chosen index
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsMoodCount;
    }
}
