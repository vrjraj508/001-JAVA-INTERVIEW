package com.basics.string01basics;

import org.w3c.dom.ls.LSOutput;

class StringBuilder01Needs{
    static void main() {
        long start = System.nanoTime();
        String result = "";

        for(int i=0;i<10000;i++){
            result +=i;
        }

        long stringTIme = System.nanoTime() - start;

        start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<10000;i++){
            sb.append(i);
        }
        String sbResult = sb.toString();
        long sbTime = System.nanoTime() - start;

        System.out.println("String time : "+stringTIme);
        System.out.println("String Builder Time: "+sbTime);
        System.out.println("Speed Ratio : "+(stringTIme/sbTime)+ "x faster");

        System.out.println(sbResult);
    }
}

public class String01Concepts {

    static void literalResuse(){
        String a = "java";
        String b = "java";
        String c = "ja"+"va";

        System.out.println(a==b);
        System.out.println(a==c);
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
    }

    static void newStringByPassingPools(){
        String pool = "java";
        String heap = new String("java");

        System.out.println(pool ==heap);
        System.out.println(pool.equals(heap));
        System.out.println(System.identityHashCode(pool));
        System.out.println(System.identityHashCode(heap));
    }

    static void runtimeConcatHeap(){
        String x = "ja";
        String y = "va";
        String runtime = x+y;

        String fx = "ja";
        String fy = "va";
        String folded = fx+fy;

        String  pool = "java";
        System.out.println(pool == runtime);
        System.out.println(pool == folded);
    }

    static void allCreationWays(){
        String s1 = "java";
        String s2 = new String("java");
        String s3 = String.valueOf(42);
        String s4 = Integer.toString(42);
        char [] chars = {'j','a','v','a'};
        String s5  = new String(chars);

        byte [] bytes = {106,97,118,97};
        String s6 = new String(bytes);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);

    }



    static void main() {

        allCreationWays();

    }
}
