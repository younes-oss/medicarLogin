package com.learn.dao;

import com.learn.model.User;
import java.sql.*;

public class UserDao {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    // ✅ Ajouter un utilisateur (inscription)
    public boolean ajouterUtilisateur(User user) {
        String sql = "INSERT INTO users (fullName, email, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Retourne vrai si l'ajout a réussi
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Récupérer un utilisateur par email (pour la connexion)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si l'utilisateur n'existe pas
    }

    // ✅ Vérifier si un utilisateur existe (éviter les doublons d'email)
    public boolean utilisateurExiste(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retourne vrai si un résultat est trouvé
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
