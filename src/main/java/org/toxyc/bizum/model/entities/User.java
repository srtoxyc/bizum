package org.toxyc.bizum.model.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "User")
public class User implements Parseable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String username;
    @Embedded
    private Email email;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)    
    private List<Phone> phoneNumbers;
    @Column
    private byte[] password;
    @Column
    private byte[] salt;

    public User(String username, Email email) {
        this.username       = username;
        this.email          = email;
        this.phoneNumbers   = new ArrayList<Phone>();
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

    public List<Phone> listPhoneNumbers() {
        return phoneNumbers;
    }
    public Phone getPhoneNumber(int index) {
        return phoneNumbers.get(index);
    }
    public void addPhoneNumber(Phone phoneNumber) {
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

    @Override
    public String toTXT() {
        return String.format("%s%s%s", this.username, Parseable.DELIM, this.email);
    }
}