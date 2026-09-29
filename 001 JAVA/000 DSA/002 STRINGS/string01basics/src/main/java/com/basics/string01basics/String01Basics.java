package com.basics.string01basics;

public class String01Basics {

    public static void printChars(String s){
        for(int i=0;i<s.length();i++){
            System.out.println(s.charAt(i));
        }
    }

    public static String reverse(String input){
        return new StringBuilder(input)
                .reverse()
                .toString();
    }

    public static boolean isPalindrome(String input) {
        int left = 0;
        int right = input.length()-1;

        while(left <right){
            if(input.charAt(left) != input.charAt(right)){
                return false;
            }
            left++;
            right--;
        }
        return true;

    }

    public static void characterFrequency(String input){
        int [] frequency = new int[256];

        for(char c: input.toCharArray()){
            frequency[c]++;
        }

        for(int i=0;i<frequency.length;i++){
            if(frequency[i] >0){
                System.out.println((char) i+"="+frequency[i]);
            }
        }
    }

    public static void countVowels(String input){
        int vowels = 0;
        int consonants = 0;

        for(int i=0;i<input.length();i++){

            char ch = Character.toLowerCase(input.charAt(i));

            if(ch >= 'a' && ch <='z'){
                if(ch =='a' ||ch=='e'||ch =='i'|| ch=='o'||ch=='u'){
                    vowels++;
                }else{
                    consonants++;
                }
            }
        }

        System.out.println("Input: "+input );
        System.out.println("Vowels: "+vowels);
        System.out.println("Consonants: "+consonants);
    }

    public static void printDuplicateCharacters(String input){
        int [] frequency= new int[26];

        for(int i=0;i<input.length();i++){
            char ch = input.charAt(i);

            if(ch >= 'a' && ch <= 'z'){
                frequency[ch - 'a']++;
            }
        }

        System.out.println("Input:"+input);
        System.out.println("Duplicate characters");

        boolean findDuplicate = false;

        for(int i=0;i<frequency.length;i++){
            if(frequency[i] > 1){
                char ch = (char) ('a'+i);

                System.out.println(ch+"="+frequency[i]);
                findDuplicate = true;
            }
        }
    }

    public static void findDuplciateCharacters(){
        String str = "This is Viraj and I need to know about this please";
        char [] charArray = str.toLowerCase().toCharArray();

        int [] frequency = new int [26];

        for(int i=0;i<str.length();i++){

            char ch = str.charAt(i);

            if(ch >= 'a' && ch <= 'z'){
                frequency[ch - 'a']++;
                // System.out.println(ch);
            }
        }

        // System.out.println(str);
        boolean findDuplicate = false;

        for(int i=0;i<frequency.length;i++){
            if(frequency[i] >1){
                char ch = (char)('a'+i);
                System.out.println(ch+"="+frequency[i]);
                findDuplicate = true;
            }
        }

        if(!findDuplicate){
            System.out.println("No duplicate characters");
        }
    }

    public static void firstNonRepeatingCharacter(String input){

        int [] frequency = new int[26];
        for(int i=0;i<input.length();i++){
            char ch = input.charAt(i);

            if(ch >='a' && ch <='z'){
                frequency[ch -'a']++;
            }
        }

        char result = '\0';

        for(int i=0;i<input.length();i++){
            char ch = input.charAt(i);

            if(frequency[ch-'a']==1){
                result = ch;
                break;
            }
        }


        if(result !='\0'){
            System.out.println(
                    "First Non Repeating Characters: "+result
            );
        }else{
            System.out.println(
                    "No non repeating characters found"
            );
        }
    }

    public static void removeDuplicateCharacters(String str){
        boolean [] seen = new boolean[256];

        StringBuilder result = new StringBuilder();

        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);

            if(ch >='a' && ch <='z'){
                int index = ch - 'a';
            }
        }

    }


    static void main() {
        firstNonRepeatingCharacter("power is good ");


    }
}
