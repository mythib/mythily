package com.rabobank.customer.statementsprocessor.service;

import com.rabobank.customer.statementsprocessor.domain.FailedTransactions;
import com.rabobank.customer.statementsprocessor.domain.Record;
import com.rabobank.customer.statementsprocessor.domain.Records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * XmlStatementsService unmarshalls csv file to List of Record objects and validates for failed transactions using ValidationService.
 *
 */
@Service
public class XmlStatementsService {

    Logger logger = LoggerFactory.getLogger(XmlStatementsService.class);

    @Autowired
    ValidationService validationService;

    // XML file is converted to List of Record objects, validated and FailedTransactions are identified
    public List<FailedTransactions> xmlProcessor(MultipartFile multipartFile) {
        List<FailedTransactions> failedTransactionsList = null;
        try {

            logger.info("XML Statement Service");

            // Unmarshalling XML file into Java Object
            JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            File convertedFile = convert(multipartFile);
            Records records = (Records) unmarshaller.unmarshal(convertedFile);

            List<Record> statements = records.getRecords();
            logger.debug("Processing the following " + statements.size() + " records:");
            statements.forEach(statement -> {
                logger.debug(statement.getReference() + "-" + statement.getDescription() + "-" + statement.getStartBalance() + "-" + statement.getMutation() + "-" + statement.getEndBalance());
            });

            failedTransactionsList = validationService.validateStatements(statements);

        } catch (JAXBException jaxbe) {
            logger.error("Exception during unmarshalling XML : " + jaxbe);
        } catch (IOException ioe) {
            logger.error("Exception while coverting multipart file to File : " + ioe);
        }finally {
            return failedTransactionsList;
        }
    }

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
