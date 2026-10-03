package com.design_patterns.design01singleton;

class EagerSingleton{
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton(){

    }

    public static EagerSingleton getInstance(){
        return INSTANCE;
    }
}

class LazySingletonUnsafe{
    private static LazySingletonUnsafe instance;

    private LazySingletonUnsafe() { }

    public static LazySingletonUnsafe getInstance(){
        if(instance == null){
            instance = new LazySingletonUnsafe();
        }
        return instance;
    }
}

class DoubleCheckedSingleton{
    private static volatile  DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton(){
        if(instance !=null){
            throw new IllegalArgumentException("Use getInstance()");
        }
    }

    public static DoubleCheckedSingleton getInstance(){
        if(instance ==null){
            synchronized (DoubleCheckedSingleton.class){
                if(instance ==null){
                    instance = new DoubleCheckedSingleton();
                }
            }
        }

        return instance;
    }
}

class HolderSingleton{
    private HolderSingleton(){ }

    private static final class Holder{
        private static final HolderSingleton instance = new HolderSingleton();
    }

    public static HolderSingleton getInstance(){
         return Holder.instance;
    }
}

public class Singleton01Basics {
}
