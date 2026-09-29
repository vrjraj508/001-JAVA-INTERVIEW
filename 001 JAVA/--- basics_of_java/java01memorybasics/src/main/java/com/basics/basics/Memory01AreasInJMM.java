package com.basics.basics;

public class Memory01AreasInJMM {
    static int instanceCreated = 0;

    private final String orderId;

    private double amount;


    Memory01AreasInJMM(String orderId,double amount){
        this.orderId = orderId;
        this.amount = amount;
        instanceCreated++;
    }

    static int stackDepthTest(int depth){
        return stackDepthTest(depth+1);
    }
    static void demonstrateAreas(){

    }

}
