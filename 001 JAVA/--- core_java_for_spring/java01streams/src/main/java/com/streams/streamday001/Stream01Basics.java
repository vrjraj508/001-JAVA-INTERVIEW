package com.streams.streamday001;

import jdk.jshell.execution.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Stream01Basics {

    public static void main(String [] args) {
        List<String> names= Utils.giveNames();
        List<Integer> numbers = Utils.giveNums();


        List<String> fruits = List.of("Apple","Guava","Pomegranate","Grapes","watermelon","Mango","Pineapple");

        long countFruits = fruits.stream().count();

        System.out.println(countFruits);

        Stream<Double> randoms = Stream.generate(Math::random).limit(5);

        randoms.forEach(c-> System.out.printf("%.1f",c*100));

        Stream<Integer> multiples = Stream.iterate(3,n->n+3).limit(10);
        Stream<Long> doubles = Stream.iterate(2L, c->c*c).limit(10);
        seperator();
        multiples.forEach(c->System.out.print(c+" "));
        seperator();
        doubles.forEach(c-> System.out.print(c+" "));

        //names.stream().limit(10).forEach(System.out::println);

        //Arrays Streams
        List<Integer> nums = Utils.giveNums();
        int arr [] = nums.stream().mapToInt(Integer::intValue)
                .toArray();

        seperator();
        System.out.println(Arrays.toString(arr));

        seperator();

        int [] arrNum = {10,20,30,40,50,60,70,80,90,100};
        Arrays.stream(arrNum).forEach(n->System.out.print(n+" "));

        String [] arrString = Utils.giveNames().toArray(String []::new);



    }

    public static void seperator(){
        System.out.println();
        System.out.println("-".repeat(50));
    }
}
