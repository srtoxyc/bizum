package org.toxyc.bizum.model.entities;

import java.util.List;

public class User implements Parseable {
    private Integer id;
    private String username;
    private Email email;
    private List<String> phoneNumbers;
    private byte[] password;
    private byte[] salt;

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

    public List<String> listPhoneNumbers() {
        return phoneNumbers;
    }
    public String getPhoneNumber(int index) {
        return phoneNumbers.get(index);
    }
    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    @Override
    public String toString() {
        return this.toJSON();
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"username\":\"" + username + '\"' +
                ", \"email\":\"" + email + '\"' +
                '}';
    }

    @Override
    public String toXML() {
        return "<user>" +
                "<username>" + username + "</username>" +
                "<email>" + email + "</email>" +
                "</user>";
    }
}