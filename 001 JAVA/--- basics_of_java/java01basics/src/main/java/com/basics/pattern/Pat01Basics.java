package com.basics.pattern;

public class Pat01Basics {

    public static void print01Rec(){
        int n = 8;

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    public static void print02Tri(){
        int n=5;

        for(int i=1;i<=n;i++){
            for(int j=1;j<=i;j++){
                System.out.print("* ");
            }
            System.out.println();
        }


    }

    public static void print02InvertTri(){
        int n=5;

        for(int i=n;i>=1;i--){
            for(int j=1;j<=i;j++){
                System.out.print("& ");
            }
            System.out.println();
        }
    }



    public static void main(String[] args) {
       print02InvertTri();

    }
}
