package org.toxyc.bizum.model.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Account")
public class Account implements Parseable {
    public final static Double INITIAL_MONEY = 0.0;
    
    @Id
    private String accountNum;
    @Column
    private Double money;
    @Column
    private String phoneNumber;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<Card>();

    public Account(String accountNum, Double money, String phoneNumber) {
        this.accountNum     = accountNum;
        this.money          = money;
        this.phoneNumber    = phoneNumber;
    }

    public String getNumAccount() {
        return accountNum;
    }
    public void setNumAccount(String accountNum) {
        this.accountNum = accountNum;
    }

    public Double getMoney() {
        return money;
    }
    public void setMoney(Double money) {
        this.money = money;
    }       
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return this.toJSON();
    }

    public String toStringStrict() {
        return "{" +
                "\"accountNum\":\"" + accountNum + '\"' +
                ", \"phoneNumber\":\"" + phoneNumber + '\"' +
                '}';
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"accountNum\":\"" + accountNum + '\"' +
                ", \"money\":" + money +
                ", \"phoneNumber\":\"" + phoneNumber + '\"' +
                '}';
    }

    @Override
    public String toXML() {
        return "<account>" +
                "<accountNum>" + accountNum + "</accountNum>" +
                "<money>" + money + "</money>" +
                "<phoneNumber>" + phoneNumber + "</phoneNumber>" +
                "</account>";
    }

    @Override
    public String toTXT() {
        return String.format("%s%s%s%s%s", this.accountNum, Parseable.DELIM, this.money, Parseable.DELIM, this.phoneNumber);
    }
}
