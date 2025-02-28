package com.learn.servlet;

import com.learn.dao.UserDao;
import com.learn.dao.PatientDao;
import com.learn.dao.DoctorDao;
import com.learn.model.Patient;
import com.learn.model.Doctor;
import com.learn.model.User;
import com.learn.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;
    private PatientDao patientDao;
    private DoctorDao doctorDao;

    @Override
    public void init() throws ServletException {
        Connection connection = DatabaseConnection.getConnection();
        userDao = new UserDao(connection);
        patientDao = new PatientDao(connection);
        doctorDao = new DoctorDao(connection);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role"); // "patient" ou "doctor"
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String specialty = request.getParameter("specialty");

        // Vérifier si l'utilisateur existe déjà
        if (userDao.getUserByEmail(email) != null) {
            request.setAttribute("error", "Cet email est déjà utilisé !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Hasher le mot de passe
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Ajouter l'utilisateur
        User user = new User(fullName, email, hashedPassword, role);
        boolean isRegistered = userDao.ajouterUtilisateur(user);

        if (isRegistered) {
            User newUser = userDao.getUserByEmail(email);
            if (role.equals("patient")) {
                Patient patient = new Patient(newUser.getId(), fullName, email, hashedPassword, phone, address);
                patientDao.ajouterPatient(patient);
            } else if (role.equals("doctor")) {
                Doctor doctor = new Doctor(newUser.getId(), fullName, email, hashedPassword, specialty, phone, address);
                doctorDao.ajouterMedecin(doctor);
            }
            response.sendRedirect("login.jsp"); // Redirection après inscription
        } else {
            request.setAttribute("error", "Une erreur s'est produite !");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
