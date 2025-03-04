package com.learn.servlet;

import com.learn.dao.DoctorDao;
import com.learn.dao.PatientDao;
import com.learn.dao.UserDao;
import com.learn.model.Doctor;
import com.learn.model.Patient;
import com.learn.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;


public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;
    private DoctorDao doctorDao;
    private PatientDao patientDao;

    @Override
    public void init() throws ServletException {
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        userDao = new UserDao(connection);
        doctorDao = new DoctorDao(connection);
        patientDao = new PatientDao(connection);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (action) {
            case "register":
                inscrireUtilisateur(request, response);
                break;
            case "login":
                authentifierUtilisateur(request, response);
                break;
            case "logout":
                deconnexionUtilisateur(request, response);
                break;
            default:
                response.sendRedirect("index.jsp");
        }
    }

    private void inscrireUtilisateur(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (userDao.utilisateurExiste(email)) {
            request.setAttribute("error", "Cet email est déjà utilisé !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if ("doctor".equals(role)) {
            String specialty = request.getParameter("specialty");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            Doctor doctor = new Doctor(fullName, email, hashedPassword, specialty, phone, address);
            if (doctorDao.ajouterMedecin(doctor)) {
                response.sendRedirect("login.jsp?success=Inscription réussie !");
            } else {
                request.setAttribute("error", "Erreur lors de l'inscription.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else if ("patient".equals(role)) {
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            Patient patient = new Patient(fullName, email, hashedPassword, phone, address);
            if (patientDao.ajouterPatient(patient)) {
                response.sendRedirect("login.jsp?success=Inscription réussie !");
            } else {
                request.setAttribute("error", "Erreur lors de l'inscription.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Rôle invalide !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void authentifierUtilisateur(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDao.getUserByEmail(email);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if ("doctor".equals(user.getRole())) {
                response.sendRedirect("doctorDashboard.jsp");
            } else if ("patient".equals(user.getRole())) {
                response.sendRedirect("patientDashboard.jsp");
            }
        } else {
            request.setAttribute("error", "Email ou mot de passe incorrect !");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void deconnexionUtilisateur(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}
