package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordsServices {

    public static int create(SalaryCalculator calc, int id) {
        String sql = "INSERT INTO records(id, baseincome, overtime, child_allowance, accommodation_allowance, meal_allowance, recreation_allowance, women_extra, tax, loan, insurance, healthcare, social_security, deduction_from_hours, gross, total_deduction, net_income, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            ps.setBigDecimal(2, calc.getBaseIncome());
            ps.setBigDecimal(3, calc.getOt());
            ps.setBigDecimal(4, calc.getChildAllowance());
            ps.setBigDecimal(5, calc.getAccommodationAllowance());
            ps.setBigDecimal(6, calc.getMealAllowance());
            ps.setBigDecimal(7, calc.getRecreationAllowance());
            ps.setBigDecimal(8, calc.getWomenExtra());
            ps.setBigDecimal(9, calc.getTax());
            ps.setBigDecimal(10, calc.getLoanRepay());
            ps.setBigDecimal(11, calc.getInsurance());
            ps.setBigDecimal(12, calc.getHealthcare());
            ps.setBigDecimal(13, calc.getSocialSecurity());
            ps.setBigDecimal(14, calc.getDeductionFromHours());
            ps.setBigDecimal(15, calc.getGrossIncome());
            ps.setBigDecimal(16, calc.getTotalDeductions());
            ps.setBigDecimal(17, calc.getNetIncome());
            ps.setString(18, calc.getDate());

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

    public static ArrayList<SalaryCalculator> getById(int id) {
        String sql = "SELECT * FROM records WHERE id = ? ORDER BY date DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ArrayList<SalaryCalculator> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     list.add(new SalaryCalculator(
                            rs.getBigDecimal("baseincome"),
                            rs.getBigDecimal("overtime"),
                            rs.getBigDecimal("child_allowance"),
                            rs.getBigDecimal("accommodation_allowance"),
                            rs.getBigDecimal("meal_allowance"),
                            rs.getBigDecimal("recreation_allowance"),
                            rs.getBigDecimal("women_extra"),
                            rs.getBigDecimal("tax"),
                            rs.getBigDecimal("loan"),
                            rs.getBigDecimal("insurance"),
                            rs.getBigDecimal("healthcare"),
                            rs.getBigDecimal("social_security"),
                            rs.getBigDecimal("deduction_from_hours"),
                            rs.getBigDecimal("gross"),
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

    public static List<Map<String, Object>> getAll() {
        String sql = "SELECT * FROM records";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("baseincome", rs.getBigDecimal("baseincome"));
                m.put("overtime", rs.getBigDecimal("overtime"));
                m.put("child_allowance", rs.getBigDecimal("child_allowance"));
                m.put("accommodation_allowance", rs.getBigDecimal("accommodation_allowance"));
                m.put("meal_allowance", rs.getBigDecimal("meal_allowance"));
                m.put("recreation_allowance", rs.getBigDecimal("recreation_allowance"));
                m.put("women_extra", rs.getBigDecimal("women_extra"));
                m.put("tax", rs.getBigDecimal("tax"));
                m.put("loan", rs.getBigDecimal("loan"));
                m.put("insurance", rs.getBigDecimal("insurance"));
                m.put("healthcare", rs.getBigDecimal("healthcare"));
                m.put("social_security", rs.getBigDecimal("social_security"));
                m.put("deduction_from_hours", rs.getBigDecimal("deduction_from_hours"));
                m.put("gross", rs.getBigDecimal("gross"));
                m.put("total_deduction", rs.getBigDecimal("total_deduction"));
                m.put("net_income", rs.getBigDecimal("net_income"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to fetch records: " + e.getMessage());
        }
        return list;
    }
}
