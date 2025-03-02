package com.learn.servlet;

import com.learn.dao.UserDao;
import com.learn.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        userDao = new UserDao(connection);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Vérifier si l'utilisateur existe
        User user = userDao.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // Créer une session utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirection en fonction du rôle
            if ("doctor".equals(user.getRole())) {
                response.sendRedirect("doctorDashboard.jsp");
            } else if ("patient".equals(user.getRole())) {
                response.sendRedirect("patientDashboard.jsp");
            } else {
                response.sendRedirect("home.jsp");
            }
        } else {
            // Échec de connexion
            request.setAttribute("errorMessage", "Email ou mot de passe incorrect");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
