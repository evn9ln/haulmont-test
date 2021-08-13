package com.haulmonttest.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "credit_offer")
public class CreditOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "credit_offer_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client clientId;

    @PreRemove
    public void removeCredit() {
        clientId.getCreditOfferList().remove(this);
    }

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit creditId;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bankId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "months_to_pay")
    private Integer months;

    @Column(name = "balance")
    private Integer balance;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public Credit getCreditId() {
        return creditId;
    }

    public void setCreditId(Credit creditId) {
        this.creditId = creditId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Bank getBankId() {
        return bankId;
    }

    public void setBankId(Bank bankId) {
        this.bankId = bankId;
    }

    public UUID getClient() {
        return clientId.getId();
    }

    public String getClientName() {
        return clientId.getName();
    }

    public String getBankName() {
        return bankId.getName();
    }

    public String getCreditType() {
        return creditId.getType();
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
