package com.streams.stream01basics;

import java.util.List;

public class Stream01Basics {
    public static void main(String[] args) {
        Main.InMemoryDatabase db = new Main.InMemoryDatabase();
        new Main.DataInitializer().initialize(db);
        List<Main.Employee> database = db.employees();

     db.employees().stream()
             .filter(emp -> emp.status()== Main.EmployeeStatus.ACTIVE)
             .forEach(System.out::println );
    }
}
