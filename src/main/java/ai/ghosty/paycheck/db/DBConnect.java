package ai.ghosty.paycheck.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private final static String PATH_TO_DB = "jdbc:sqlite:./db/ghst.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(PATH_TO_DB);
        } catch (SQLException e) {
            System.err.println("error: getting database connection failed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
