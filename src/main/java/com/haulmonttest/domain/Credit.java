package com.haulmonttest.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "credit_id")
    private UUID id;

    @Column(name = "limit")
    private Integer limit;

    @Column(name = "percent")
    private Integer percent;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bankId;

    @OneToMany(mappedBy = "creditId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditOffer> creditOfferList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bank getBankId() {
        return bankId;
    }

    public void setBankId(Bank bankId) {
        this.bankId = bankId;
    }


    public void setCreditOfferList(List<CreditOffer> creditOfferList) {
        this.creditOfferList = creditOfferList;
    }

    public String getBankName() {
        return this.getBankId().getName();
    }

}
