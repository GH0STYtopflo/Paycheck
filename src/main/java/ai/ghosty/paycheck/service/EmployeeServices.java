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
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServices {
    public static int create(Employee emp) {
        String sql = "INSERT INTO employees(emp_id,name, last_name, gender, marital_status, children, rent, work_hours, overtime, deduction_hours, loan, date, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emp.getId());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getGender());
            ps.setInt(5, (emp.isMarried()) ? 1 : 0);
            ps.setInt(6, emp.getChildren());
            ps.setInt(7, emp.getIsRent() ? 1 : 0);
            ps.setInt(8, emp.getWorkHours());
            ps.setInt(9, emp.getExtraHours());
            ps.setInt(10, emp.getDeductionHours());
            ps.setBigDecimal(11, emp.getLoan());
            ps.setString(12, emp.getHireDate().toString());
            ps.setInt(13, emp.getPosition().getId());

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
        String sql = "SELECT * FROM employees WHERE emp_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String lastName = rs.getString("last_name");
                    String gender = rs.getString("gender");
                    boolean isMarried = (rs.getInt("marital_status") == 1);
                    int overtime = rs.getInt("overtime");
                    int children = rs.getInt("children");
                    int workHours = rs.getInt("work_hours");
                    int deductionHours = rs.getInt("deduction_hours");
                    BigDecimal loan = rs.getBigDecimal("loan");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    boolean rent = rs.getInt("rent") == 1;
                    int pos_id = rs.getInt("pos_id");
                    Position pos = PositionsServices.getPositionById(pos_id);


                    return new Employee(id, name, lastName, gender,isMarried , overtime, children, workHours, deductionHours, loan, rent, date, pos);
                }
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to fetch employee: " + e.getMessage());
        }
        return null;
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("emp_id");
                String name = rs.getString("name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                boolean isMarried = (rs.getInt("marital_status") == 1);
                int overtime = rs.getInt("overtime");
                int children = rs.getInt("children");
                int workHours = rs.getInt("work_hours");
                int deductionHours = rs.getInt("deduction_hours");
                BigDecimal loan = rs.getBigDecimal("loan");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                boolean rent = rs.getInt("rent") == 1;
                int pos_id = rs.getInt("pos_id");
                Position pos = PositionsServices.getPositionById(pos_id);

                employees.add(new Employee(id, name, lastName, gender, isMarried, overtime, children, workHours, deductionHours, loan, rent, date, pos));
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to fetch employees: " + e.getMessage());
        }
        return employees;
    }

    public static void updateEmpState(Employee emp) {
        String sql = "UPDATE employees SET children = ?, rent = ?, marital_status = ?,work_hours = ?, overtime = ?, deduction_hours = ?," +
                " loan = ?, pos_id = ?, WHERE emp_id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, emp.getChildren());
            pstmt.setInt(2, emp.getIsRent() ? 1 : 0);
            pstmt.setInt(3, emp.isMarried() ? 1 : 0);
            pstmt.setInt(4, emp.getWorkHours());
            pstmt.setInt(5, emp.getExtraHours());
            pstmt.setInt(6, emp.getDeductionHours());
            pstmt.setBigDecimal(7, emp.getLoan());
            pstmt.setInt(8, emp.getPosition().getId());
            pstmt.setInt(9, emp.getId());

            pstmt.execute();
        }
        catch (SQLException e) {
            System.err.println("[error] failed to update employee status: " + e.getMessage());
        }
    }

    public static void deleteEmployee(int id) {
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "DELETE FROM employees WHERE emp_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.err.println("[info] deleted user successfully");
        }
        catch (SQLException e) {
            System.err.println("[error] failed to delete employee: " + e.getMessage());
        }

        UserServices.deleteUser(id);
        RecordsServices.deleteEmpRecords(id);
    }
}
