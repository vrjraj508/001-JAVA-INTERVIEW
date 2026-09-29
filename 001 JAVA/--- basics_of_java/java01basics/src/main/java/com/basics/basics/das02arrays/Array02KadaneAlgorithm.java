package com.basics.basics.das02arrays;

public class Array02KadaneAlgorithm {

    public static int arr01Kadane( int arr[]){
        //Initializae the first no here as seen here.
        int currSum = arr[0];
        int maxSum = arr[0];

        for(int i=1;i<arr.length;i++){
            currSum = Math.max(arr[i],currSum+arr[i]);

            maxSum = Math.max(maxSum,currSum);
        }

        return maxSum;
    }

    public static void printSubArrays(int [] arr){
        for(int i=0;i<arr.length;i++){
            for(int j = i;j<arr.length;j++){
                for(int k=i;k<j;k++){
                    System.out.print(arr[k]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void arr01SubArraySum_brut(int [] arr){
        int maxSum = Integer.MAX_VALUE;

        for(int st = 0;st< arr.length;st++){
            int currSum = 0;

            for(int end = st;end<arr.length;end++){
                currSum += arr[end];
                maxSum = Math.max(currSum,maxSum);
            }
        }
        System.out.println(maxSum);

    }

    public static boolean removeDuplicates_brut(int []nums){
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;i++){
                if(nums[i] == nums[j]) return true;
            }
        }

        return false;
    }

    public static int removeDuplicate_opt01(int [] nums){
        int k = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[k] !=nums[i]){
                k++;
            }
        }

        return k;
    }


    public static void main(String[] args) {
        int[] arr = {3, -2, 5, -1, 6, -3};
        int [] arr01 = {1,2,3,4,5};

        int [] arrDup001 = {1,1,1,3,3,4,3,2,4,2};
        int [] arrDup002 ={1,0,1,1};

        System.out.println(removeDuplicates_brut(arr01));

    }


}
