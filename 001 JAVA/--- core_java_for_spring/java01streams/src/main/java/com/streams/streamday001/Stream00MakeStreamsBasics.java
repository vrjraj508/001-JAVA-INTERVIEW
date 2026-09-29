package com.streams.streamday001;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Stream00MakeStreamsBasics {
    public static void main(String[] args) {

        //Creating stream from a list
        List<String> names = List.of("Viraj","Alice","Bob","Shreya","Shiva","Rakesh");
        names.stream().map(String::toUpperCase)
                .forEach(System.out::println);

        //Creating Stream from a set
        Set<Integer> numSet = new HashSet<>(Arrays.asList(5,3,7,8,2,9,10,2,1));
        numSet.stream()
                .map(n->n*2)
                .sorted()
                .forEach(System.out::println);

        //Creating stream from an array
        String [] fruits = {"Apple","Orange","Cherry","Date","Mango","Pineapple"};
        Arrays.stream(fruits)
                .map(s->s.length())
                .forEach(c->System.out.print(c+" "));

        System.out.println();
        Stream.of(10,20,30,40,50,60,70,80,90,100)
                .map(n->n*2)
                .forEach(n->System.out.print(n+" "));
        System.out.println();

       Stream<String> streamEmtpy = Stream.empty();
        System.out.println(streamEmtpy.count());

        //Creating a stream from a builder
        List<String> utilStreamNames = Utils.giveNames();

        Stream.Builder<String> builder = Stream.builder();

        utilStreamNames.forEach(builder::add);

        Stream<String> myStreamNames = builder.build();
        myStreamNames.forEach(n->System.out.print(n+" "));

        System.out.println();
        //Transform a list and build a new stream
        List<String> newString = Utils.giveNames();

        Stream.Builder<String> stringBuilder = Stream.builder();

        newString.stream()
                .map(String::toUpperCase)
                .forEach(stringBuilder::add);

        Stream<String> newStringStream = stringBuilder.build();

        newStringStream
                .limit(10)
                .forEach(c->System.out.print(c+" "));

        //






    }
}
