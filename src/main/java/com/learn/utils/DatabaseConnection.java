package com.learn.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medicare_login";
    private static final String USER = "root"; // Remplace par ton utilisateur MySQL
    private static final String PASSWORD = "admin"; // Remplace par ton mot de passe MySQL
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion à la base de données réussie !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Erreur de connexion à la base de données !");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
