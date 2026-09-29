package com.stream.stream00concepts;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Employee{
    private int id;
    String name;
    String department;
    private double salary;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public Employee(int id, String name, String department, double salary){
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;


    }

}

class Emp{
    private String name;
    private String dept;

    public Emp(String name,String dept){
        this.name = name;
        this.dept = dept;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    @Override
    public String toString(){
        return String.format("["+name+"--"+"]");
    }

    public static void main(String[] args) {
        List<Emp> employees = Arrays.asList(
                new Emp("Viraj","IT"),
                new Emp("Haris","IT"),
                new Emp("Ashish","IT"),
                new Emp("Rahul","HR"),
                new Emp("Pooja","Finance"),
                new Emp("Amit","HR"),
                new Emp("Karan","Finance")
        );

       LinkedHashMap<String,List<Emp>> filteredEmp =  employees.stream()
                .collect(Collectors.groupingBy(
                        Emp::getDept,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        System.out.println(filteredEmp);
    }
}

class ReduceCollect{
    public static void main(String[] args) {
        List<Integer> numbers= List.of(10,20,30,40,50,60);

        int sum = numbers.stream()
                .reduce(0,(a,b)->a+b);

        List<Integer> result = numbers.stream()
                        .collect(Collectors.toList());

        System.out.println(result);
        System.out.println(sum);
    }
}

public class Stream01Basics {
}
