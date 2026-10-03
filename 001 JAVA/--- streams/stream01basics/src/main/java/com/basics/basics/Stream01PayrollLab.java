package com.basics.basics;

import java.time.LocalDate;
import java.util.List;

public class Stream01PayrollLab {

    public static void main(String[] args) {
        EnterprisePayrollLibrary.PayrollDataset dataset = PayrollLibrary.generate();

        EnterprisePayrollLibrary.PayrollDataset datasetEmployee = PayrollLibrary.builder()
                .employees(500_000)
                .seed(42)
                .threads(8)
                .referenceDate(LocalDate.now())
                .validate(true)
                .printProgress(true)
                .build();

        List<EnterprisePayrollLibrary.Employee> employees = dataset.employees();

        employees.stream().limit(10).forEach(System.out::println);


    }


    }
