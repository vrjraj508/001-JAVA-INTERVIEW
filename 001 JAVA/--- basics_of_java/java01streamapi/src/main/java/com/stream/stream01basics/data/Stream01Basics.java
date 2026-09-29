package com.stream.stream01basics.data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.stream.stream01basics.data.StreamLab;

import javax.xml.crypto.dom.DOMCryptoContext;

public class Stream01Basics {
    public static void main(String[] args) {
       List<StreamLab.Employee> db = StreamLab.DataGenerator.generate().getEmployees();


       List<String> names = db.stream()
               .map(e-> e.getFirstName()+" "+e.getLastName())
               .toList().stream().limit(20).toList();

       long result = db.stream()
               .filter(e->e.getStatus()== StreamLab.EmployeeStatus.ACTIVE)
               .filter(e->e.getSalary() > 100_000)
               .map(e->e.getFirstName()+" "+e.getLastName())
               .toList().stream().count();

        System.out.println(result);

        List<StreamLab.Employee> highPaidEmployee = db.stream()
                .sorted(Comparator.comparing(StreamLab.Employee::getSalary).reversed())
                .limit(20).toList();

        List<StreamLab.Employee> top10Employees = db.stream()
                        .sorted(Comparator.comparing(StreamLab.Employee::getSalary).reversed())
                                .limit(10).toList();

       // System.out.println(top10Employees);
        //System.out.println(highPaidEmployee);

        boolean existsMorethan200k = db.stream().anyMatch(e->e.getSalary() >200_000);
        //System.out.println(existsMorethan200k);

        boolean existsEmpGreaterthan18 = db.stream().allMatch(e->e.getAge() >=18);
        System.out.println(existsEmpGreaterthan18);

        double averageSalary = db.stream()
                .collect(Collectors.averagingDouble(StreamLab.Employee::getSalary));

        double averageAge = db.stream()
                .collect(Collectors.averagingInt(StreamLab.Employee::getAge));

        System.out.println(averageSalary);
        System.out.println(averageAge);


        //Now how to divide the eniter employee table based on various departments
        Map<StreamLab.Department,List<StreamLab.Employee>> employeesByDepartment =
                db.stream().collect(Collectors.groupingBy(
                        StreamLab.Employee::getDepartment
                ));

       // System.out.println(employeesByDepartment);

        //Grouping By and Averaging
        Map<StreamLab.Department, Double> averageSalaryByDepartment = db.stream()
                .collect(
                        Collectors.groupingBy(
                                StreamLab.Employee::getDepartment,
                                Collectors.averagingDouble(
                                        StreamLab.Employee::getSalary
                                )
                        )
                );

        System.out.println(averageSalaryByDepartment);

        //Partioned By
        Map<Boolean,List<StreamLab.Employee>> partionEmpBy =
                db.stream()
                        .collect(Collectors.partitioningBy(
                                e->e.getSalary() >100_000
                        ));

        System.out.println(partionEmpBy);







    }
}
