package com.basics.dsa;

/**
 *  nums = [2,7,11,15]
 *  target = 9
 * */
 class Arr001TwoSum {

    public static int[] twoSum(int [] nums, int target){
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j <nums.length;j++){
                if(nums[i]+nums[j] == target){
                    return new int[] {i,j};
                }
            }
        }

        return new int[]{-1};
    }

    public static void showArray(int arr[]){
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
    }

    public static void main(String[] args) {
        int arr[] = {2,7,11,15};


       showArray(twoSum(arr,26));


    }



}
