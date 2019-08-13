package com.rabobank.customer.statementsprocessor.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "records")
@XmlAccessorType (XmlAccessType.FIELD)
public class Records {

    @XmlElement(name = "record")
    private List<Record> records = new ArrayList<>();

    public Records() {
    }

    public Records(List<Record> records) {
        this.records = records;
    }


    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
