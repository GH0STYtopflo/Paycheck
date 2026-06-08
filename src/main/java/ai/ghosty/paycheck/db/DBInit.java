package ai.ghosty.paycheck.db;

import ai.ghosty.paycheck.util.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
    public static void init() {
        String sql;
        try (Connection conn = DBConnect.getConnection()){ // create employees table
             try (Statement stmt = conn.createStatement();) {
                 // create positions table
                 sql = "CREATE TABLE IF NOT EXISTS positions (" +
                         "pos_id INTEGER PRIMARY KEY, " +
                         "title TEXT NOT NULL," +
                         "income_per_hour REAL NOT NULL)";

                 try {
                     stmt.execute(sql);
                     System.err.println("[info] created positions table successfully");
                 } catch (SQLException e) {
                     System.err.println("[error] failed to create positions table");
                 }

                 // create employees table
                 sql = "CREATE TABLE IF NOT EXISTS employees (" +
                         "emp_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "pos_id INTEGER NOT NULL," +
                         "gender TEXT NOT NULL," +
                         "marital_status REAL NOT NULL CHECK (marital_status IN (0,1))," +
                         "children INTEGER DEFAULT 0," +
                         "rent INTEGER NOT NULL CHECK (rent IN (0,1))," +
                         "work_hours INTEGER NOT NULL," +
                         "overtime INTEGER DEFAULT 0," +
                         "deduction_hours INTEGER DEFAULT 0," +
                         "loan REAL DEFAULT 0," +
                         "date TEXT NOT NULL," +
                         "FOREIGN KEY (pos_id) REFERENCES positions(pos_id))";

                 try {
                     stmt.execute(sql);
                     System.err.println("[info] created employees table successfully");
                 } catch (SQLException e) {
                     System.err.println("[error] failed to create employees table");
                 }

                 // create users table
                 sql = "CREATE TABLE IF NOT EXISTS users (" +
                         "user_id INTEGER PRIMARY KEY," +
                         "username TEXT UNIQUE," +
                         "pass_hash TEXT NOT NULL," +
                         "role TEXT NOT NULL," +
                         "FOREIGN KEY (user_id) REFERENCES employees(emp_id))";

                 try {
                     stmt.execute(sql);
                     System.err.println("[info] created users table successfully");
                 } catch (SQLException e) {
                     System.err.println("[error] failed to create users table");
                 }

                 // create pay records table
                 sql = "CREATE TABLE IF NOT EXISTS records (" +
                         "rec_id INTEGER NOT NULL," +
                         "baseincome REAL DEFAULT 0," +
                         "overtime REAL DEFAULT 0," +
                         "child_allowance REAL DEFAULT 0," +
                         "accommodation_allowance REAL DEFAULT 0," +
                         "meal_allowance REAL DEFAULT 0," +
                         "recreation_allowance REAL DEFAULT 0," +
                         "women_extra REAL DEFAULT 0," +
                         "tax REAL DEFAULT 0," +
                         "loan REAL DEFAULT 0," +
                         "insurance REAL DEFAULT 0," +
                         "healthcare REAL DEFAULT 0," +
                         "social_security REAL DEFAULT 0," +
                         "deduction_from_hours REAL DEFAULT 0," +
                         "gross REAL DEFAULT 0," +
                         "total_deduction REAL DEFAULT 0," +
                         "net_income REAL DEFAULT 0," +
                         "date TEXT NOT NULL," +
                         "FOREIGN KEY (rec_id) REFERENCES employees(emp_id))";

                 try {
                     stmt.execute(sql);
                     System.err.println("[info] created records table successfully");
                 } catch (SQLException e) {
                     System.err.println("[error] failed to create records table");
                 }
             }
             catch (Exception e) {
                 System.err.println("[error] failed to create statement");
             }



             // admin acc
            sql = "INSERT OR IGNORE INTO users (user_id,username,pass_hash,role) VALUES (?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, 0);
                pstmt.setString(2, "GHOSTY");
                pstmt.setString(3, Encryption.stringToSHA256("ghostywontmiss"));
                pstmt.setString(4, "admin");

                pstmt.execute();
                System.err.println("[info] created admin account successfully");
            }
            catch (SQLException e) {
                System.err.println("[error] failed to create admin account");
            }

        }
        catch (SQLException e) {
             System.err.println("[error] failed to get connection to the database");
        }
         catch (Exception e) {
             System.err.println("[error] something went wrong while initializing database");
        }
    }
}
