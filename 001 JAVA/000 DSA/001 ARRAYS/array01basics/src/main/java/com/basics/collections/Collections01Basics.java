package com.basics.collections;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class User {
    private int id;
    private String name;

    public User(int id,String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,name);
    }
}
public class Collections01Basics {
    public static void main(String[] args) {
        User u1 = new User(101,"Viraj");
        User u2 = new User(101,"Viraj");

        System.out.println(u1.hashCode());
        System.out.println(u2.hashCode());

    }
}
