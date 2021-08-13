package com.haulmonttest.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "bank_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany
    private List<Client> clientList;

    @OneToMany
    private List<Credit> creditList;

    public Bank() {
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
