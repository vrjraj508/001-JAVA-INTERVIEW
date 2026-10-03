package com.basics.basics.level002;

import java.util.Arrays;

class Program21UpdateElement{
    public static void main(String[] args) {

        int [] arr = {10,20,30,40,50,60};

        int index = 2;
        int newValue = 100;

        arr[index] = newValue;

        for(int value:arr){
            System.out.println(value);
        }
    }
}

class Program22ReplaceAllOccurances{
    public static void main(String[] args) {
        int [] arr = {10,20,30,40,20,10,60,10};
        int target = 10;
        int replacement = 99;

        for(int i=0;i<arr.length;i++){
            if(arr[i] == target){
                arr[i] = replacement;
            }
        }

        for(int val:arr){
            System.out.print(val+" ");
        }
    }
}

class Program23ReplacePositive{
    public static void main(String[] args) {
        int [] arr = {-4,56,-90,-10,12,-3,13,67,43};

        for(int i=0;i<arr.length;i++){
            if(arr[i] > 0){
                arr[i] = 1;
            }else{
                arr[i] = 0;
            }
        }

        for(int val:arr){
            System.out.println(val+" ");
        }
    }
}

class Program25ArrayReverse{
    public static void main(String[] args) {
        int [] arr = {10,20,30,40,50,60};
        int left = 0;
        int right = arr.length -1;
        int temp = 0;
        for(int i=arr.length -1,j=0;i>j;i--,j++){
            temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        System.out.println(Arrays.toString(arr));
    }
}

class Array27SwapTwoElements{
    public static void main(String[] args) {
        int [] arr = {10,20,30,40,50,60,70,80};

        int i=1;
        int j = 4;

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        System.out.println(Arrays.toString(arr));
    }
}

class Array28LeftRotate{
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};

        int first = arr[0];

        for(int i=0;i<arr.length-1;i++){
            arr[i] = arr[i+2];
        }
    }
}

class Array29RighRotate{
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};

        int last = arr[arr.length -1];
        for(int i=arr.length-1;i>0;i--){
            arr[i] = arr[i-1];
        }

        arr[0] = last;

        System.out.println(Arrays.toString(arr));
    }
}

class Array32MoveZerosToEnd{
    

}

public class Array02Operations {

    public static void main(String[] args) {

    }
}
