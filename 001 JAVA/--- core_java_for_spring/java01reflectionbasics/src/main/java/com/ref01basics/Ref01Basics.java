package com.ref01basics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Ref01Basics {
    static class BankAccount{
        private String accountNumber;
        private double balance;
        private final String owner;

        public BankAccount(String accountNumber, String owner, double initialBalance){

            this.accountNumber = accountNumber;
            this.balance = initialBalance;
            this.owner = owner;
        }

        private double getBalance(){
            return balance;
        }
        private void debit(double amount){
            balance -=amount;
        }

        @Override
        public String toString(){
            return "Bank Account:: ["+accountNumber+"] owner is::"+owner;
        }
    }

    static void main() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> c1 = BankAccount.class;

        Class<?> c2 = Class.forName("com.ref01basics.Ref01Basics$BankAccount");

        BankAccount account = new BankAccount("ACC001","Alice",1000.0);
        Class <?> c3 = account.getClass();

        System.out.println(c1 == c2);
        System.out.println(c2 ==c3);


        //Fields
        separator("Fields in Reflection");
        for(Field f:c1.getDeclaredFields()){
            System.out.printf("%-10s %-8s %s%n",
                    Modifier.toString(f.getModifiers()),
                    f.getType().getSimpleName(),
                    f.getName()
                    );
        }

        //Methods
        separator("Methods in Reflection");
        for(Method m: c1.getDeclaredMethods()){
            System.out.printf("%-10s %-8s %s(%s) %n",
                    Modifier.toString(m.getModifiers()),
                    m.getReturnType().getSimpleName(),
                    m.getName(),
                    Arrays.stream(m.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")));

        //Reading the private field value in the Reflection API
            Field fBalance = c1.getDeclaredField("balance");
            fBalance.setAccessible(true);

            double balance = (double) fBalance.get(account);

           Field fOwner  = c1.getDeclaredField("owner");
           fOwner.setAccessible(true);
           String owner = (String) fOwner.get(account);

            System.out.println(balance);
            System.out.println(owner);

        //Invoking a private method at runtime
        Method debitMethod = c1.getDeclaredMethod()




        }

    }

    public static void separator(String sectionName){
        System.out.println();
        System.out.println("---------------------"+sectionName+"---------------------");
        System.out.println();

    }
}
