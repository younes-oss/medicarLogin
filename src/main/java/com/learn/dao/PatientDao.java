package com.learn.dao;

import com.learn.model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {
    private Connection connection;

    public PatientDao(Connection connection) {
        this.connection = connection;
    }

    // ✅ Ajouter un patient
    public boolean ajouterPatient(Patient patient) {
        String sql = "INSERT INTO users (fullName, email, password, role) VALUES (?, ?, ?, 'patient')";
        String sql2 = "INSERT INTO patients (user_id, phone, address) VALUES (LAST_INSERT_ID(), ?, ?)";

        try (PreparedStatement stmt1 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt2 = connection.prepareStatement(sql2)) {

            stmt1.setString(1, patient.getFullName());
            stmt1.setString(2, patient.getEmail());
            stmt1.setString(3, patient.getPassword());
            stmt1.executeUpdate();

            ResultSet rs = stmt1.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                stmt2.setString(1, patient.getPhone());
                stmt2.setString(2, patient.getAddress());
                stmt2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Récupérer un patient par email
    public Patient getPatientByEmail(String email) {
        String sql = "SELECT u.id, u.fullName, u.email, u.password, p.phone, p.address " +
                     "FROM users u JOIN patients p ON u.id = p.user_id WHERE u.email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Vérifier si un patient existe
    public boolean patientExiste(String email) {
        String sql = "SELECT id FROM users WHERE email = ? AND role = 'patient'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retourne vrai si un résultat est trouvé
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Récupérer tous les patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT u.id, u.fullName, u.email, u.password, p.phone, p.address " +
                     "FROM users u JOIN patients p ON u.id = p.user_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
