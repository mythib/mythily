package com.rabobank.customer.statementsprocessor.controller;

import com.rabobank.customer.statementsprocessor.domain.FailedTransactions;
import com.rabobank.customer.statementsprocessor.service.CsvStatementsService;
import com.rabobank.customer.statementsprocessor.service.XmlStatementsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This is the main controller class where all request mappings are done.
 * Based on the Statements Processer the user selects, the request is routed to appropriate Service class
 * like XmlStatementsService, CsvStatementsService and in future if needed say JsonService.
 * The xml file or csv file that comes in the request is passed to the respective service for unmarshalling and validation.
 */

@Controller
@RequestMapping(path = "/customer/statements")
public class StatementsController {

    Logger logger = LoggerFactory.getLogger(StatementsController.class);

    @Autowired
    private XmlStatementsService xmlStatementsService;  // Service to process xml files

    @Autowired
    private CsvStatementsService csvStatementsService;  // Service to process csv files


    /* This is meant for extensibility in future
    @Autowired
    private JsonStatementsService jsonStatementsService;  // Service to process json in future
    */

    @RequestMapping(method = RequestMethod.POST, value = "/xmlprocessor")
    public String xmlProcessor(@RequestPart("file")MultipartFile xmlFile, Model model){
        logger.info("XML Statement Processor");

        // xmlStatementsService processes xml and returns failed transactions
        List<FailedTransactions> failedTransactions = xmlStatementsService.xmlProcessor(xmlFile);
        model.addAttribute("failedTransactions", failedTransactions);
        return "report"; // view that displays failed transactions
    }

    @RequestMapping(method = RequestMethod.POST, value = "/csvprocessor")
    public String csvProcessor(@RequestPart("file")MultipartFile csvFile, Model model){

        logger.info("CSV Statements Processor");
        // csvStatementsService processes csv and returns failed transactions
        List<FailedTransactions> failedTransactions = csvStatementsService.csvProcessor(csvFile);

        model.addAttribute("failedTransactions", failedTransactions);
        return "report";  // view that displays failed transactions
    }

    @RequestMapping(method = RequestMethod.GET, value="/test")
    public String test(){
        return "test";
    }
}
