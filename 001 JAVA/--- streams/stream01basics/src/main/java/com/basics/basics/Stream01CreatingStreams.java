package com.basics.basics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Program001{
    record Employee(int id,String name,String department,double salary,int experiencedYears){

    }

    record Transaction(
            String id,
            String customer,
            double amount,
            String status
    ){}

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee(1, "Alice",   "Engineering", 120000, 8),
                new Employee(2, "Bob",     "Marketing",    55000, 3),
                new Employee(3, "Charlie", "Engineering",  95000, 6),
                new Employee(4, "Diana",   "HR",           45000, 2),
                new Employee(5, "Eve",     "Engineering",  85000, 5),
                new Employee(6, "Frank",   "Finance",      78000, 4),
                new Employee(7, "Grace",   "Engineering", 110000, 9),
                new Employee(8, "Hank",    "Marketing",    62000, 3)
        );

        record Product(
                String code,
                String name,
                int stock,
                int reorderedLevel
        ){}

        List<Transaction> transactions = List.of(
                new Transaction("TX101", "Rahul", 85000, "SUCCESS"),
                new Transaction("TX102", "Priya", 32000, "SUCCESS"),
                new Transaction("TX103", "Amit", 125000, "SUCCESS"),
                new Transaction("TX104", "Neha", 70000, "FAILED"),
                new Transaction("TX105", "Vikram", 95000, "SUCCESS")
        );

        List<Product> products = List.of(
                new Product("P101", "Laptop", 8, 10),
                new Product("P102", "Monitor", 25, 15),
                new Product("P103", "Keyboard", 5, 10),
                new Product("P104", "Mouse", 30, 20),
                new Product("P105", "Printer", 3, 8)
        );

        products.stream()
                        .filter(p->p.stock() < p.reorderedLevel())
                .filter(p->p.reorderedLevel() <5)
                                .sorted(Comparator.comparingInt(Product::stock))
                                        .map(p-> String.format(
                                                "%s | %-10s | Stock: %d | Reorder: %d",
                                                p.code(),
                                                p.name(),
                                                p.stock(),
                                                p.reorderedLevel()
                                        ))
                                                .forEach(System.out::println);

        products.stream()
                        .filter(p->p.stock() >10)
                                .forEach(System.out::println);
        products.stream()
                        .filter(p->p.reorderedLevel()> 10)
                                .forEach(System.out::println);
        employees.stream()
                .filter(e->e.experiencedYears() >=5)
                .sorted(Comparator.comparingInt(Employee::experiencedYears).reversed())
                .map(e->String.format(
                        "%-10s | %-12s | %d years | %.0f",
                        e.name(),
                        e.department(),
                        e.experiencedYears(),
                        e.salary()
                )).forEach(System.out::println);
    }
}

class Program002{
    record Customer(
            int id,
            String name,
            String city,
            boolean active){

    }

    public static void main(String[] args) {
        List<Customer> customers = List.of(
                new Customer(101, "Vikram", "Delhi", true),
                new Customer(102, "Alice", "Mumbai", true),
                new Customer(103, "Rahul", "Pune", false),
                new Customer(104, "Neha", "Bangalore", true),
                new Customer(105, "Bob", "Chennai", true)
        );

        customers.stream()
                .filter(Customer::active)
                .sorted(Comparator.comparing(Customer::name))
                .map(c-> String.format(
                        "%d || %-10s || %-10s",
                        c.id(),c.name(),c.city()
                ))
                .forEach(System.out::println);



    }
}

class Enterprise01Basics{
    public static void main(String[] args) {
        OrderManagementSystem.InMemoryDatabase database =
                new OrderManagementSystem.InMemoryDatabase();

        OrderManagementSystem.OrderRepository repository =
                new OrderManagementSystem.OrderRepository(database);

        List<OrderManagementSystem.Order> orders =
                repository.findAll();

        orders.stream().filter(
                order-> order.status() == OrderManagementSystem.OrderStatus.DELIVERED
                && order.totalAmount().compareTo(new BigDecimal("100000")) > 0).map(
                        o->String.format("%s %s %s",o.orderId(),o.customer(),o.shippingCity())
        ).forEach(System.out::println);


    }
}



class Enterprise01Payroll{
    public static void main(String[] args) {




    }
}


public class Stream01CreatingStreams {

}


