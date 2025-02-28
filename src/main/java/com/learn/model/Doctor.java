package com.learn.model;

public class Doctor extends User {
    private String specialty;
    private String phone;
    private String address;

    public Doctor() {}

    public Doctor(String fullName, String email, String password, String specialty, String phone, String address) {
        super(fullName, email, password, "doctor"); // Définir le rôle automatiquement
        this.specialty = specialty;
        this.phone = phone;
        this.address = address;
    }

    public Doctor(int id, String fullName, String email, String password, String specialty, String phone, String address) {
        super(id, fullName, email, password, "doctor");
        this.specialty = specialty;
        this.phone = phone;
        this.address = address;
    }

    // Getters et Setters
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
