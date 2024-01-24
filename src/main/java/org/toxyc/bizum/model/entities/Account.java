package org.toxyc.bizum.model.entities;

public class Account {
    private String accountNum;
    private Double money;
    
    public Account(String accountNum, Double money) {
        this.accountNum     = accountNum;
        this.money          = money;
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
}
