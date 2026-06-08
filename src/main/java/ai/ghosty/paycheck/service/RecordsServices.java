package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import ai.ghosty.paycheck.model.Record;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordsServices {

    public static int create(Record record) {
        String sql = "INSERT INTO records(rec_id, baseincome, overtime, child_allowance, accommodation_allowance, meal_allowance, recreation_allowance, women_extra, tax, loan, insurance, healthcare, social_security, deduction_from_hours, gross, total_deduction, net_income, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getId());
            ps.setBigDecimal(2, record.getBaseIncome());
            ps.setBigDecimal(3, record.getOtPay());
            ps.setBigDecimal(4, record.getChildAllowance());
            ps.setBigDecimal(5, record.getAccommodationAllowance());
            ps.setBigDecimal(6, record.getMealAllowance());
            ps.setBigDecimal(7, record.getRecreationAllowance());
            ps.setBigDecimal(8, record.getWomensExtra());
            ps.setBigDecimal(9, record.getTax());
            ps.setBigDecimal(10, record.getLoanRepay());
            ps.setBigDecimal(11, record.getInsurance());
            ps.setBigDecimal(12, record.getHealthcare());
            ps.setBigDecimal(13, record.getSocialSecurity());
            ps.setBigDecimal(14, record.getDeductionFromHours());
            ps.setBigDecimal(15, record.getGrossIncome());
            ps.setBigDecimal(16, record.getTotalDeductions());
            ps.setBigDecimal(17, record.getNetIncome());
            ps.setString(18, record.getDate());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to create record: " + e.getMessage());
        }

        return -1;
    }

    public static ArrayList<Record> getById(int id) {
        String sql = "SELECT * FROM records WHERE rec_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ArrayList<Record> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     list.add(new Record(id,
                            rs.getBigDecimal("baseincome"),
                             rs.getBigDecimal("gross"),
                            rs.getBigDecimal("overtime"),
                             rs.getBigDecimal("accommodation_allowance"),
                             rs.getBigDecimal("meal_allowance"),
                             rs.getBigDecimal("recreation_allowance"),
                            rs.getBigDecimal("child_allowance"),
                            rs.getBigDecimal("women_extra"),
                            rs.getBigDecimal("tax"),
                             rs.getBigDecimal("social_security"),
                             rs.getBigDecimal("healthcare"),
                             rs.getBigDecimal("insurance"),
                             rs.getBigDecimal("deduction_from_hours"),
                            rs.getBigDecimal("loan"),
                            rs.getBigDecimal("total_deduction"),
                            rs.getBigDecimal("net_income"),
                            rs.getString("date")
                    ));

                }
                return list;
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to fetch record: " + e.getMessage());
        }
        return null;
    }
}
