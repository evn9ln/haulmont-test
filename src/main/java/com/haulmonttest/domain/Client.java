package com.haulmonttest.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "client_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "passport")
    private String passport;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bankId;

    @OneToMany(mappedBy = "clientId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditOffer> creditOfferList;

    public Client() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID client_id) {
        this.id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Bank getBank() {
        return bankId;
    }

    public String getBankName() {
        if (bankId != null) return bankId.getName();
        return "";
    }

    public void setBank(Bank bank) {
        this.bankId = bank;
    }

    public List<CreditOffer> getCreditOfferList() {
        return creditOfferList;
    }

}
