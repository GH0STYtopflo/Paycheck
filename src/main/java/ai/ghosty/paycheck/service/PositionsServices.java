package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import ai.ghosty.paycheck.logger.LogLevel;
import ai.ghosty.paycheck.logger.Logger;
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
            Logger.log(String.format("created position {%s}", position.getTitle()), LogLevel.INFO);
        }
        catch (SQLException e) {
            Logger.log(String.format("failed to create position {%s}: ", position.getTitle()) + e.getMessage(), LogLevel.WARN);
        }
    }

    public static Position getPositionById(int id) {
        String sql = "SELECT * FROM positions WHERE pos_id = ?";

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Logger.log(String.format("fetched position {%d}", id), LogLevel.INFO);
                return new Position(rs.getInt("pos_id"), rs.getString("title"), rs.getBigDecimal("income_per_hour"));
            }
        }
        catch (SQLException e) {
            Logger.log(String.format("failed to fetch position {%d}: ", id) +  e.getMessage(), LogLevel.WARN);
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

            Logger.log("fetched positions", LogLevel.INFO);
        } catch (SQLException e) {
            Logger.log("failed to fetch positions", LogLevel.WARN);
        }

        return positions;
    }
}
