package com.basics.star_patterns;

public class Pattern01Basics {

    public static void pat01Square(int n){
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++){
                System.out.print("*"+"  ");
            }
            System.out.println();
        }
    }

    public static void pat02RightTriange(int n){
        for(int i=1;i<n;i++){
            for(int j = 1; j<=i;j++){
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    public static void pat03InvertedTriangle(int n){
        for(int i=n;i >=1;i--){
            for(int j=1;j<=i;j++){
                System.out.print("* ");
            }

            System.out.println();
        }
    }

    public static void pat04PyramidPattern(int n){
        for(int i=1;i<=n;i++){

            for(int j=n;j>=1;j--){
                System.out.print(" ");
            }

            for(int j=1;j<=(2*i-1);j++){
                System.out.print("* ");

            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        pat04PyramidPattern(6);

    }
}
