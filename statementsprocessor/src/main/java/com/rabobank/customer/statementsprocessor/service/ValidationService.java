package com.rabobank.customer.statementsprocessor.service;

import com.rabobank.customer.statementsprocessor.domain.FailedTransactions;
import com.rabobank.customer.statementsprocessor.domain.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  ValidationService has the business logic for validating List<Record> for failed transactions.
 *  A transaction can fail if there are duplicate transaction references available in the list or
 *  it can fail if end balance is negative.
 */
@Configuration
public class ValidationService {

    Logger logger = LoggerFactory.getLogger(ValidationService.class);

    private static final String DUPLICATE_TRANSACTION = "Duplicate Transaction";
    private static final String INVALID_BALANCE = "Invalid End Balance";

    public List<FailedTransactions> validateStatements(List<Record> records){
        List<FailedTransactions> failedTransactions = new ArrayList<>();

        // Find records with duplicate reference numbers
        List<Record> duplicateTransactions = records.stream().collect(Collectors.groupingBy(Record::getReference))
                .values().stream().filter(duplicates -> duplicates.size() > 1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        logger.debug("No. of duplicate transactions : "+duplicateTransactions.size());

        // Find records with invalid EndBalance
        List<Record> invalidBalanceTransactions = records.stream().filter(rec -> rec.getEndBalance() < 0)
                .collect(Collectors.toList());

        logger.debug("No. of invalid balance transactions : "+invalidBalanceTransactions.size());

        // Consolidate failed transactions
        logger.debug("Duplicate Transactions : ");
        duplicateTransactions.forEach(transaction -> {
            logger.debug(transaction.getReference() + " - " + transaction.getDescription());
            failedTransactions.add(new FailedTransactions(transaction.getReference(), transaction.getDescription(), DUPLICATE_TRANSACTION));
        });
        logger.debug("Invalid Balance Transactions : ");
        invalidBalanceTransactions.forEach(transaction -> {
            logger.debug(transaction.getReference() + " - " + transaction.getDescription());
            failedTransactions.add(new FailedTransactions(transaction.getReference(), transaction.getDescription(), INVALID_BALANCE));
        });
        logger.debug("Total Failed Transactions : "+failedTransactions.size());
        return failedTransactions;
    }

}
