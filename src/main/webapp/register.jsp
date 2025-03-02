<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #4facfe, #00f2fe);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        input, select, button {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        button {
            background-color: #4facfe;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #00c6ff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Inscription</h2>
        <form action="RegisterServlet" method="post">
            <input type="text" name="fullName" placeholder="Nom complet" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Mot de passe" required>
            <select name="role" required>
                <option value="patient">Patient</option>
                <option value="doctor">Médecin</option>
            </select>
            <input type="text" name="phone" placeholder="Téléphone" required>
            <input type="text" name="address" placeholder="Adresse" required>
            <button type="submit">S'inscrire</button>
        </form>
        <p>Déjà un compte ? <a href="login.jsp">Se connecter</a></p>
    </div>
</body>
</html>
