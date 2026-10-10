package com.basics.basics;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class EmployeeDB{

}

public class Stream01Basics{

    public static EnterprisePayrollLibrary.PayrollDataset init500(){
        EnterprisePayrollLibrary.PayrollDataset dataset = PayrollLibrary.generate(500_000);

        return dataset;
    }

    public static EnterprisePayrollLibrary.PayrollDataset initAll(){
        EnterprisePayrollLibrary.PayrollDataset dataset = PayrollLibrary.generate();

        return dataset;
    }

    public static EnterprisePayrollLibrary.PayrollDataset initBuilder(){
        EnterprisePayrollLibrary.PayrollDataset dataset = PayrollLibrary.builder()
                .employees(100_000)
                .seed(42l)
                .threads(8)
                .referenceDate(LocalDate.now())
                .validate(true)
                .printProgress(true)
                .build();

        return  dataset;
    }

    public static void main(String[] args) {
        EnterprisePayrollLibrary.PayrollDataset dataset = Stream01Basics.init500();
        EnterprisePayrollLibrary.PayrollDataset datasetBuilder = Stream01Basics.initBuilder();

        List<EnterprisePayrollLibrary.Employee> employeesList = datasetBuilder.employees();

        Long countEmployees = employeesList.stream().count();


        employeesList.stream().forEach(s-> System.out.println(s.toString()));





    }
}