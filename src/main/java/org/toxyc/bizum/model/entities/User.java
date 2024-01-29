package org.toxyc.bizum.model.entities;

import java.util.List;
import java.util.Map;

public class User {
    private Integer id;
    private String username;
    private Email email;
    private byte[] password;
    private byte[] salt;
    // private List<Asignacion> asignacion;

    public User(String username, Email email) {
        this.username   = username;
        this.email      = email;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }
    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}