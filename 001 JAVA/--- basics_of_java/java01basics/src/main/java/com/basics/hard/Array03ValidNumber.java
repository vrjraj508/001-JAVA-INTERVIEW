package com.basics.hard;

class ValidNumber{
    public static boolean isNumber(String s){
        boolean seenDigit = false;
        boolean seenDot = false;
        boolean seenE = false;

        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);

            if(Character.isDigit(c)){
                seenDigit = true;
            }else if(c=='+' || c=='-'){
                if(i !=0 && s.charAt(i-1) !='e' && s.charAt(i-1) !='E'){
                    return false;
                }
            } else if(c== '.'){
                if(seenDot || seenE){
                    return  false;
                }
                seenDot = true;
            } else if(c== 'e' || c=='E'){
                if(seenE || !seenDigit){
                    return  false;
                }
                seenE = true;
                seenDigit = false;
            }
            else{
                return  false;
            }
        }

        return seenDigit;
    }
}

public class Array03ValidNumber {

    public static void main(String[] args) {
        System.out.println(ValidNumber.isNumber("53.5e93"));
    }
}
