package com.haulmonttest.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment_timetable")
public class PaymentTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "payment_timetable_id")
    private UUID id;

    @Column(name = "payment_date")
    private Date date;

    @Column(name = "payment_amount")
    private Integer amount;

    @Column(name = "repayment_amount")
    private Integer repaymentAmount;

    @Column(name = "percent_repayment_amount")
    private Integer percentRepaymentAmount;

    @Column(name = "credit_offer_id")
    private UUID creditOfferId;

    public PaymentTimetable() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getPercentRepaymentAmount() {
        return percentRepaymentAmount;
    }

    public void setPercentRepaymentAmount(Integer percentRepaymentAmount) {
        this.percentRepaymentAmount = percentRepaymentAmount;
    }

    public UUID getCreditOfferId() {
        return creditOfferId;
    }

    public void setCreditOfferId(UUID creditOfferId) {
        this.creditOfferId = creditOfferId;
    }
}
