CREATE DATABASE medicare_login;
USE medicare_login;

-- Table principale pour tous les utilisateurs
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM("doctor", "patient") NOT NULL
);

-- Table des médecins (hérite de users)
CREATE TABLE doctors (
    id INT PRIMARY KEY, -- Même ID que users
    specialty VARCHAR(255) NULL,
    phone VARCHAR(255) NULL,
    address VARCHAR(255) NULL,
    FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table des patients (hérite de users)
CREATE TABLE patients (
    id INT PRIMARY KEY, -- Même ID que users
    phone VARCHAR(255) NULL,
    address VARCHAR(255) NULL,
    FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE
);