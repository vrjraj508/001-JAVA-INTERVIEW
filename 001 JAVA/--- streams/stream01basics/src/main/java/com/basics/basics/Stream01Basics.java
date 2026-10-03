package com.basics.basics;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Stream01Set{
    public static void main(String[] args) {
        Set<String> department = Set.of(
                "IT",
                "HR",
                "Finance",
                "Operations",
                "Auditing",
                "Compliance"
        );

        Stream<String> departmentStream = department.stream();

        departmentStream.forEach(System.out::println);
    }
}

class Stream01Array{
    public static void main(String[] args) {
        String [] technologies = {
                "Java",
                "Spring Boot",
                "Kafka",
                "Docker",
                "HTML CSS",
                "VCSS"
        };

        Stream<String> technologyStream = Arrays.stream(technologies);

        technologyStream.forEach(System.out::println);
    }
}

class Stream01Of{
    public static void main(String[] args) {
        Stream<String> services = Stream.of(
                "Payment",
                "Order",
                "Inventory",
                "Notification",
                "Services"
        );

        services.forEach(System.out::println);
    }
}

class Stream01IntegerStream{
    public static void main(String[] args) {
        Stream<Integer> transactionAmounts = Stream.of(
                1200,
                4500,
                800,
                3200,
                6700
        );

        transactionAmounts.forEach(System.out::println);
    }
}

class Stream01EmptyStream{

}

public class Stream01Basics {
    public static void main(String[] args) {
        List<String> employees = List.of(
                "Rahul",
                "Priya",
                "Amit",
                "Neha",
                "Rishkia",
                "Shefali",
                "Sumita"
        );


        Stream<String> services = Stream.of(
                "payment",
                "order",
                "inventory",
                "notification"
        );

        services.forEach(System.out::println);
     }
}
