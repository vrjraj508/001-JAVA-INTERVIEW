package com.date.basics;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Date01Basics {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE,1);
        Date newDate = cal.getTime();

        System.out.println( newDate);

        //Worse case setting a date
        Calendar calSetDate = Calendar.getInstance();
        calSetDate.set(Calendar.YEAR,2026);
        calSetDate.set(Calendar.MONTH,Calendar.APRIL);
        calSetDate.set(Calendar.DAY_OF_MONTH,5);

        //MOdern
        LocalDate myDate = LocalDate.of(2025,4,4);
        myDate = myDate.plusDays(1);

        System.out.println(myDate+" "+calSetDate.toString());
    }
}
