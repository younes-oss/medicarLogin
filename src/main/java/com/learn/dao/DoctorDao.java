package com.learn.dao;

import com.learn.model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DoctorDao {
    private Connection connection;

    public DoctorDao(Connection connection) {
        this.connection = connection;
    }

    // ✅ Ajouter un médecin
    public boolean ajouterMedecin(Doctor doctor) {
        String sql = "INSERT INTO users (fullName, email, password, role) VALUES (?, ?, ?, 'doctor')";
        String sql2 = "INSERT INTO doctors (user_id, specialty, phone, address) VALUES (LAST_INSERT_ID(), ?, ?, ?)";

        try (PreparedStatement stmt1 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt2 = connection.prepareStatement(sql2)) {

            

            stmt1.setString(1, doctor.getFullName());
            stmt1.setString(2, doctor.getEmail());
            stmt1.setString(3, doctor.getPassword());
            stmt1.executeUpdate();

            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                stmt2.setString(1, doctor.getSpecialty());
                stmt2.setString(2, doctor.getPhone());
                stmt2.setString(3, doctor.getAddress());
                stmt2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du médecin", e);
        }
        return false;
    }

    // ✅ Récupérer un médecin par email
    public Doctor getDoctorByEmail(String email) {
        String sql = "SELECT u.id, u.fullName, u.email, u.password, d.specialty, d.phone, d.address " +
                     "FROM users u JOIN doctors d ON u.id = d.user_id WHERE u.email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Doctor(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("specialty"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du médecin", e);
        }
        return null;
    }

    // ✅ Vérifier si un médecin existe
    public boolean doctorExiste(String email) {
        String sql = "SELECT id FROM users WHERE email = ? AND role = 'doctor'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du médecin", e);
        }
    }

    // ✅ Récupérer tous les médecins
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT u.id, u.fullName, u.email, u.password, d.specialty, d.phone, d.address " +
                     "FROM users u JOIN doctors d ON u.id = d.user_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("specialty"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la liste des médecins", e);
        }
        return doctors;
    }

    // ✅ Supprimer un médecin par ID
    public boolean supprimerDoctor(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du médecin", e);
        }
    }
}
