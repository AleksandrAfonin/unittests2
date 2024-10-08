package ru.otus.java.professional.unittests2.entity;

import java.math.BigDecimal;

public class Account {

    private long id;
    private BigDecimal amount;

    private Integer type;

    private String number;

    private Long agreementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public static Account copyAccount(Account sourceAccount){
        Account newAccount = new Account();
        newAccount.setId(sourceAccount.getId());
        newAccount.setAmount(sourceAccount.getAmount());
        newAccount.setType(sourceAccount.getType());
        newAccount.setNumber(sourceAccount.getNumber());
        newAccount.setAgreementId(sourceAccount.getAgreementId());
        return newAccount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", type=" + type +
                ", number='" + number + '\'' +
                ", agreementId=" + agreementId +
                "}\n";
    }
}
