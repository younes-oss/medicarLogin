package com.learn.model;

public class Patient extends User {
    private String phone;
    private String address;

    public Patient() {}

    public Patient(String fullName, String email, String password, String phone, String address) {
        super(fullName, email, password, "patient"); // Définir le rôle automatiquement
        this.phone = phone;
        this.address = address;
    }

    public Patient(int id, String fullName, String email, String password, String phone, String address) {
        super(id, fullName, email, password, "patient");
        this.phone = phone;
        this.address = address;
    }

    // Getters et Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
