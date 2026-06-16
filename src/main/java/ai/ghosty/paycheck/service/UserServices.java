package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.db.DBConnect;
import ai.ghosty.paycheck.logger.LogLevel;
import ai.ghosty.paycheck.logger.Logger;
import ai.ghosty.paycheck.model.Role;
import ai.ghosty.paycheck.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class UserServices {
    public static void createUser(User user) {
        String sql = "INSERT INTO users(user_id, username, pass_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().roleTitle);
            ps.executeUpdate();

            Logger.log(String.format("created user {%s}", user.getUsername()), LogLevel.INFO);
        } catch (SQLException e) {
            Logger.log(String.format("failed to create user {%s}: ", user.getUsername()) + e.getMessage(), LogLevel.WARN);
        }
    }

    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"),
                            rs.getString("pass_hash"), Role.valueOf(rs.getString("role").toUpperCase()));
                }
            }
        } catch (SQLException e) {
            Logger.log(String.format("failed to get user by username {%s}: ", username) + e.getMessage(), LogLevel.WARN);
        }

        return null;
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            Logger.log(String.format("failed to lookup username {%s}",  username), LogLevel.WARN);
        }

        return false;
    }

    public static void deleteUser(int id) {
        try (Connection conn = DBConnect.getConnection()){
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            Logger.log(String.format("deleted user {%d}", id), LogLevel.INFO);
        }
        catch (SQLException e) {
            Logger.log(String.format("failed to delete user {%d}", id), LogLevel.WARN);
        }
    }
}
