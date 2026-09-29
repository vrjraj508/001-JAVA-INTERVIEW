package com.basics.lambda01basics;


import java.net.CacheRequest;

class Pro01_AnnoLambda{
    interface Greeter{
        String greet(String name);
    }

    static void main() {
        Greeter annoClass = new Greeter() {
            @Override
            public String greet(String name) {
                return "Hello there "+name;
            }
        };

        Greeter shorter = (name)->{
            return "Hello there "+name;
        };

        Greeter shorter2 = name ->{
            return "Hello "+name;
        };

         annoClass.greet("Viraj Kumar");

         shorter.greet("Haris Ahmed");
         shorter2.greet("Priyanshu Raj");

        Greeter shortest = name -> "Hello there !"+name;
        shortest.greet("Viraj Kumar");
    }
}


class P002_LambdaSyntax{
    interface NoArgs{
        void run();
    }

    interface OneArg{
        int doubleNum(int n);
    }

    interface TwoArg{
        int add(int a,int b);
    }

    interface ReturnsVoid{
        void print(String s);
    }

    interface Multiline{
        String process(String process);
    }

    static void main() {
        NoArgs noArgs = ()-> System.out.println("No Args Lambda");

        OneArg oneArg = n->n*2;

        TwoArg add = (a,b)-> a+b;

        ReturnsVoid printer = s-> System.out.println("Printing data"+s);

        Multiline multiline = s->{
            String trimmed = s.trim();
            String upper = trimmed.toUpperCase();
            System.out.println(upper);

            return upper;
        };
        multiline.process("This is my multiline process");
    }


}

abstract class Calculable {
    public abstract int add(int a, int b);
    public abstract int subtract(int a, int b);
    public abstract int multiply(int a, int b);
}

class CalculateNum{
    static void main() {
        Calculable operations = new Calculable() {


            @Override
            public int add(int a, int b) {
                return a+b;
            }

            @Override
            public int subtract(int a, int b) {
                return a-b;
            }

            @Override
            public int multiply(int a, int b) {
                return a*b;
            }
        };

        System.out.println(operations.add(34,56));
        System.out.println(operations.subtract(45,12));
        System.out.println(operations.multiply(12,67));


    }
}




public class Lambda01Basics {
}
