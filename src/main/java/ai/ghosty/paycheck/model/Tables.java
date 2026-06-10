package ai.ghosty.paycheck.model;

import ai.ghosty.paycheck.db.DBConnect;

import java.sql.*;
import java.util.HashSet;

public enum Tables {
    POSITIONS("positions", "pos_id", new int[]{1000, 10000}),
    USERS("users", "user_id", new int[]{100000, 1000000}),
    EMPLOYEES("employees", "emp_id", new int[]{100000, 1000000}),
    RECORDS("records", "rec_id", new int[]{100000, 1000000});


    public final String tableName;
    public final String idColumnName;
    public final int[] idRange;

    Tables(String tableName, String idColumnName, int[] idRange) {
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.idRange = idRange;
    }

    public HashSet<Integer> getTableIDs() {
        HashSet<Integer> ids = new HashSet<>();

        try (Connection conn = DBConnect.getConnection()) {
            String sql = String.format("SELECT %s FROM %s", this.idColumnName, this.tableName);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ids.add(rs.getInt(this.idColumnName));
            }
        }
        catch (SQLException e) {
            System.err.println("[error] failed to get connection to the database");
        }

        return ids;
    }
}
