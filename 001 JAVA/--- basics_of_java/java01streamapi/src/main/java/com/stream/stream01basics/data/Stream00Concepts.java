package com.stream.stream01basics.data;

import java.util.List;
import java.util.stream.Stream;

public class Stream00Concepts {
    public static void main(String[] args) {
        List<StreamLab.Employee> db = StreamLab.DataGenerator.generate().getEmployees();

    }
}
