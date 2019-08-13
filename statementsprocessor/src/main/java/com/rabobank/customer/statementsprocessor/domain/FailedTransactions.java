package com.rabobank.customer.statementsprocessor.domain;

public class FailedTransactions {

    private String transactionReference;
    private String description;
    private String failureReason;

    public FailedTransactions() {
    }

    public FailedTransactions(String transactionReference, String description, String failureReason) {
        this.transactionReference = transactionReference;
        this.description = description;
        this.failureReason = failureReason;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }


}
