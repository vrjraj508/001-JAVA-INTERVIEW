package com.learning.basics;

import javax.print.DocFlavor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

class EagerSingleton{
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton(){
        System.out.println("EagerSingleton Constructed");
    }

    public static EagerSingleton getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println("Before instantiation");
        EagerSingleton s1 = EagerSingleton.getInstance();
        EagerSingleton s2 = EagerSingleton.getInstance();

        System.out.println((s1 == s2));
    }
}

class Singleton01EagerConfigLoader{
    private static final Singleton01EagerConfigLoader INSTANCE = new Singleton01EagerConfigLoader();
    private final Map<String,String> settings;

    private Singleton01EagerConfigLoader(){
        settings = Map.of("env","prod","region","us-east-1");
        System.out.println("Configuration Loaded from dis (Expensive)");
    }

    public String get(String key){
        return settings.get(key);
    }
    public static Singleton01EagerConfigLoader getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args) {
        Singleton01EagerConfigLoader instance01 = new Singleton01EagerConfigLoader();

        System.out.println(instance01.get("env"));
        System.out.println(instance01.get("region"));

    }
}


class MetricsRegistry{
    private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

    private MetricsRegistry(){

    }

    public static MetricsRegistry getInstance(){return INSTANCE; }

    public void increment(String metricName){
        counters.computeIfAbsent(metricName, k-> new LongAdder()).increment();
    }

    public long get(String metricName){
        LongAdder adder = counters.get(metricName);
        return adder == null ? 0 : adder.sum();
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        for(int i=0;i<1000;i++){
            pool.submit(()->
                    MetricsRegistry.getInstance().increment("requests"));
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Total: "+MetricsRegistry.getInstance().get("requests") );

    }
}
public class Single01Basics {


}
