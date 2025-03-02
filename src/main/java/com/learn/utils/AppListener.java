package com.learn.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class AppListener implements ServletContextListener {

    private static final String URL = "jdbc:mysql://localhost:3306/medicare_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            sce.getServletContext().setAttribute("DBConnection", connection);
            System.out.println("✅ Connexion à la base de données établie !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Erreur de connexion à la base de données");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Connection connection = (Connection) sce.getServletContext().getAttribute("DBConnection");
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connexion à la base de données fermée !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
