package com.learning.basics;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.OptionalLong;

class Optional01Basics{
    public static void main(String[] args) {
        Optional<String> name001 = Optional.of("Viraj");
        Optional<String> name002 = Optional.of("Java");
        Optional<String> name003 = Optional.empty();

        String nullVal = null;

        System.out.println(name001);
        System.out.println(name002);
        System.out.println(name003);
        System.out.println(Optional.ofNullable(nullVal));
        System.out.println(Optional.of(nullVal));

    }
}

class Optional01IsPresent{
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Viraj");

        if(name.isPresent()){
            System.out.println("Name exists");
        }

        Optional<String> empty = Optional.empty();

        if(empty.isPresent()){
            System.out.println("Name exists");
        }else{
            System.out.println("name doesnt exists");
        }
    }
}

class Optional01Get{
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Viraj");

        String value = name.get();
        Optional<String> nameEmpty = Optional.empty();

        String result = nameEmpty.orElse("Unknown");

        System.out.println(result);

        System.out.println(value);
    }
}

class Optional01orElse{
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Viraj");

        String result = name.orElseGet(
                ()-> "Unknown"
        );

        System.out.println(result);
    }
}

class Optional01Evalauation{

    static String createDefault(){
        System.out.println("Creating Default ...");
        return "Unknown";
    }

    public static void main(String[] args) {

        Optional<String> name = Optional.of("Viraj");

        System.out.println("Using orElse");

        String result1 = name.orElse(createDefault());
        System.out.println(result1);

        System.out.println("Using orElseGet");

        String result2 = name.orElseGet(
                ()-> createDefault()
        );

        System.out.println(result2);

    }
}

class Optional01IfPresent{
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Viraj");

        name.ifPresent(
                value -> System.out.println("Name"+value)
        );
    }
}

class Optionla01IfOrElse{
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Viraj");

        name.ifPresentOrElse(
                value ->
                        System.out.println(value),
                ()->
                        System.out.println("Name not found")
        );
    }
}

class Optional01OrElseThrow{
    public static void main(String[] args) {
        Optional<String> name = Optional.empty();

        String result = name.orElseThrow();

        System.out.println(result);
    }
}

class Optional01CustomException{
    public static void main(String[] args) {
        Optional<String> name = Optional.empty();

        String result = name.orElseThrow(
                ()-> new IllegalArgumentException("Name not found")
        );

        System.out.println(result);
    }
}

//Enterprise Examples
class User{
    private Long id;
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}


public class Java01Basics {
}
