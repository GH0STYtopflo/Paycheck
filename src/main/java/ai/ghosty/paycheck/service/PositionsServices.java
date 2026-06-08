package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import ai.ghosty.paycheck.model.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionsServices {
    public static void createPosition(Position position) {
        String sql = "INSERT INTO positions(pos_id, title, income_per_hour) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, position.getId());
            ps.setString(2, position.getTitle());
            ps.setBigDecimal(3, position.getSalaryPerHour());

            ps.execute();
        }
        catch (SQLException e) {
            System.err.println("[error] failed to create position entry: " + e.getMessage());
        }
    }

    public static void updatePosition(Position position) {
        String sql = "UPDATE positions SET title = ?, income_per_hour = ? WHERE pos_id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, position.getTitle());
            ps.setBigDecimal(2, position.getSalaryPerHour());
            ps.setInt(3, position.getId());

            ps.execute();
        }
        catch (SQLException e) {
            System.err.println("[error] failed to update position entry: " + e.getMessage());
        }
    }

    public static Position getPositionById(int id) {
        String sql = "SELECT * FROM positions WHERE pos_id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Position(rs.getInt("pos_id"), rs.getString("title"), rs.getBigDecimal("income_per_hour"));
            }
        }
        catch (SQLException e) {
            System.err.println("[error] failed to get position entry: " + e.getMessage());
        }

        return null;
    }

    public static List<Position> getAllPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM positions";

        try (Connection conn = DBConnect.getConnection() ){
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                positions.add(new Position(rs.getInt("pos_id"), rs.getString("title"), rs.getBigDecimal("income_per_hour")));
            }
        } catch (SQLException e) {
            System.err.println("[error] failed to get all positions: " + e.getMessage());
        }

        return positions;
    }
}
