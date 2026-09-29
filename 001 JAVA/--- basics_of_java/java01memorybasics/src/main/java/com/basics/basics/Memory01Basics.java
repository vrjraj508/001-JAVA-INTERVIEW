package com.basics.basics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

class Student{
    String name;

    Student(String name){
        this.name = name;
    }
}

class Account{
    int balance;

    Account(int balance){
        this.balance = balance;
    }
}



public class Memory01Basics {

    public static void main(String[] args) {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        System.out.println("----- Get all the details in bytes");
        System.out.println(toMB(heapUsage.getInit()));
        System.out.println(toMB(heapUsage.getMax()));
        System.out.println(toMB(heapUsage.getUsed()));

        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();


    }

    static long toMB(long bytes){
        return bytes == -1? -1 : bytes/(1024*1024);
    }

    static void updateBalance(Account a){
        a.balance +=500;
    }

    static void methodA() {
        int a = 20;
        System.out.println("inside methodA");
        methodB();
    }

    static void methodB() {
        int b = 30;
        System.out.println("Inside methodB");
    }
}
