package com.rabobank.customer.statementsprocessor.service;

import com.rabobank.customer.statementsprocessor.domain.FailedTransactions;
import com.rabobank.customer.statementsprocessor.domain.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CsvStatementsService unmarshalls csv file to List of Record objects and validates for failed transactions using ValidationService.
 *
 */
@Service
public class CsvStatementsService {
    Logger logger = LoggerFactory.getLogger(XmlStatementsService.class);

    private static final String CSV_SEPARATOR = ",";
    @Autowired
    ValidationService validationService;

    // CSV file is converted to List of Record objects, validated and FailedTransactions are identified
    public List<FailedTransactions> csvProcessor(MultipartFile multipartFile) {
        List<FailedTransactions> failedTransactionsList = new ArrayList<>();
        try {

            logger.info("CSV Statement Service");

            File convertedFile = convert(multipartFile);
            InputStream inputStream = new FileInputStream(convertedFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            // Skip the header and convert csv to object
            List<Record> statements = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();

            logger.debug("Processing the following " + statements.size() + " records:");
            statements.forEach(record -> {
                logger.debug(record.getReference() + "-" + record.getDescription());
            });

            failedTransactionsList = validationService.validateStatements(statements);

        }catch (IOException ioe) {
            logger.error("Exception while coverting multipart file to File : " + ioe);
        }catch (Exception e){
            logger.error("Exception "+e);
        }finally {
            return failedTransactionsList;
        }
    }

    // Transforms one line of csv elements into a Record object
    private Function<String, Record> mapToItem = (line) -> {
        String[] fields = line.split(CSV_SEPARATOR);
        Record record = new Record();
        record.setReference(fields[0]);
        record.setAccountNumber(fields[1]);
        record.setDescription(fields[2]);
        record.setStartBalance(Double.parseDouble(fields[3]));
        record.setMutation(Double.parseDouble(fields[4]));
        record.setEndBalance(Double.parseDouble(fields[5]));
        return record;
    };

    // This method converts multipart file into normal File
    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
