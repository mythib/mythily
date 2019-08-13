package com.rabobank.customer.statementsprocessor.domain;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "record")
@XmlAccessorType (XmlAccessType.FIELD)
public class Record {

    @XmlAttribute
    private String reference;

    private String accountNumber;

    private String description;

    private double startBalance;

    private double mutation;

    private double endBalance;

    public Record() {
    }

    public Record(String reference, String accountNumber, String description, double startBalance, double mutation, double endBalance) {
        this.reference = reference;
        this.accountNumber = accountNumber;
        this.description = description;
        this.startBalance = startBalance;
        this.mutation = mutation;
        this.endBalance = endBalance;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(double startBalance) {
        this.startBalance = startBalance;
    }


    public double getMutation() {
        return mutation;
    }

    public void setMutation(double mutation) {
        this.mutation = mutation;
    }



    public double getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(double endBalance) {
        this.endBalance = endBalance;
    }
}
