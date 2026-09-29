package com.basics.java02_beanfactory;

public interface ApplicationContext extends ListableBeanFactory,HierarchicalBeanFactory{
    String getId();
    void refresh();
    void close();
}
