package org.toxyc.bizum.model.entities;


public class Card {
    private Integer numCard;
    private String name;
    private Integer cvv;
    private String expirationDate;
    private Account account;
    
    public Card(Integer numCard, String name, Integer cvv, String expirationDate, Account account) {
        this.numCard            = numCard;
        this.name               = name;
        this.cvv                = cvv;
        this.expirationDate     = expirationDate;
        this.account            = account;
    }

    public Integer getNumCard() {
        return numCard;
    }
    public void setNumCard(Integer numCard) {
        this.numCard = numCard;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getCVV() {
        return this.cvv;
    }
    public void setCVV(Integer cvv) {
        this.cvv = cvv;
    }

    public String getDueDate() {
        return expirationDate;
    }
    public void setDueDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return String.format("");
    }
}