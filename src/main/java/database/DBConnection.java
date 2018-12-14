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

    public void execute(String sqlString) {
        try {
            Statement statement = currentConnection.createStatement();
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
            Statement statement = currentConnection.createStatement();
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

}
