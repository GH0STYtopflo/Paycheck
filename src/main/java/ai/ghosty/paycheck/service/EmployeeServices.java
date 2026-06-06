package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import ai.ghosty.paycheck.model.Employee;
import ai.ghosty.paycheck.model.Position;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeeServices {
    public static int create(Employee emp) {
        String sql = "INSERT INTO employees(id,name, last_name, gender, children, rent, work_hours, overtime, deduction_hours, loan, date, pos_name, pos_base_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emp.getId());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getGender());
            ps.setInt(5, emp.getChildren());
            ps.setInt(6, emp.isRent() ? 1 : 0);
            ps.setInt(7, emp.getWorkHours());
            ps.setInt(8, emp.getExtraHours());
            ps.setInt(9, emp.getDeductionHours());
            ps.setBigDecimal(10, emp.getLoan());
            ps.setString(11, emp.getHireDate().toString());
            ps.setString(12, emp.getPosition() != null ? emp.getPosition().getPosName() : null);
            ps.setBigDecimal(13, emp.getPosition() != null ? emp.getPosition().getBaseSalaryPerHour() : null);

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to create employee: " + e.getMessage());
        }

        return -1;
    }

    public static Employee getById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String lastName = rs.getString("last_name");
                    String gender = rs.getString("gender");
                    int overtime = rs.getInt("overtime");
                    int children = rs.getInt("children");
                    int workHours = rs.getInt("work_hours");
                    int deductionHours = rs.getInt("deduction_hours");
                    BigDecimal loan = rs.getBigDecimal("loan");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    boolean rent = rs.getInt("rent") == 1;
                    String posName = rs.getString("pos_name");
                    BigDecimal posBase = rs.getBigDecimal("pos_base_salary");
                    Position pos = new Position(posName != null ? posName : "NaP", posBase != null ? posBase : BigDecimal.ZERO);
                    return new Employee(id, name, lastName, gender, overtime, children, workHours, deductionHours, loan, rent, date, pos);
                }
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to fetch employee: " + e.getMessage());
        }
        return null;
    }

    public static void updateUserState(Employee emp) {
        String sql = "UPDATE employees SET children = ?, rent = ?, work_hours = ?, overtime = ?, deduction_hours = ?," +
                " loan = ?, pos_name = ?, pos_base_salary = ? WHERE id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, emp.getChildren());
            pstmt.setInt(2, emp.isRent() ? 1 : 0);
            pstmt.setInt(3, emp.getWorkHours());
            pstmt.setInt(4, emp.getExtraHours());
            pstmt.setInt(5, emp.getDeductionHours());
            pstmt.setBigDecimal(6, emp.getLoan());
            pstmt.setString(7, emp.getPosition() != null ? emp.getPosition().getPosName() : null);
            pstmt.setBigDecimal(8, emp.getPosition() != null ? emp.getPosition().getBaseSalaryPerHour() : null);
            pstmt.setInt(9, emp.getId());

            pstmt.execute();
        }
        catch (SQLException e) {
            System.err.println("[error] failed to update employee status: " + e.getMessage());
        }
    }
}
