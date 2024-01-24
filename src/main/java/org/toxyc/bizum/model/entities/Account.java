package org.toxyc.bizum.model.entities;

public class Account {
    private String accountNum;
    private Double money;
    private String phoneNumber;

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
}
