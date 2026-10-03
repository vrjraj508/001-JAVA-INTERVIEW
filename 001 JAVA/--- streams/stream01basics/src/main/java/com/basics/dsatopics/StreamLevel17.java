package com.basics.dsatopics;

import java.util.List;

class Program01{
    public static void main(String[] args) {
        List<Integer> nums = List.of(1,2,3,4,5,6,7,8,9);
        nums.stream()
                .filter(n->{
                    System.out.println("Filter the data: "+n);
                     return n >2;
                }).count();

        System.out.println("Done");
    }
}

public class StreamLevel17 {

    public static void main(String[] args) {

    }
}
