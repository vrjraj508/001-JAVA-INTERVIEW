package com.basics.dsatopics;

import java.util.Arrays;

class Array01Basics{
    public static int [] getConcatenations(int [] nums){
        int n = nums.length;
        int [] result = new int[2*n];

        for(int i=0;i<n;i++){
            result[i] = nums[i];
            result[i+n] = nums[i];
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getConcatenations(new int[]{1,2,3,4})));
    }
}

public class Arrays01Basics {
}
