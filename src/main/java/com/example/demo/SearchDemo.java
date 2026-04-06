package com.example.demo;

public class SearchDemo {
 
    public static void main(String[] args) {
        System.out.println("======== 二分查找 =========");
        int[] arr = {7, 23, 79, 81, 103, 127, 131, 147}; 
        int result = binarySearch(arr, 81);
        System.out.println("二分查找: 结果在第" + (result + 1)+ "个位置");
    }

    
    /**
     * 二分查找
     * 1.数组中数据有序
     * 2.每次排除一半的范围
     */
    public static int binarySearch(int[] arr, int number) {
        int left = 0;
        int right = arr.length - 1; 

        while(left <= right) {
            int mid = left + (right - left) / 2; //防止溢出！！！

            if (number == arr[mid]) {
                return mid;
            } else if (number > arr[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }
        return -1;
    }


    /**
     * 插值查找
     * 均匀分布
     * 按比例算 mid，而不是固定取中间
     */
    public static int interpolationSearch(int[] arr, int number) {
        int left = 0;
        int right = arr.length - 1; 

        while(left <= right && number >= arr[left] && number <= arr[right]) {
            if(arr[left] == arr[right]) {
                return arr[left] == number ? left : -1;
            }
            
            int mid = left + (number - arr[left]) * (right - left) / (arr[right] - arr[left]); 

            if (number == arr[mid]) {
                return mid;
            } else if (number > arr[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }
        return -1;
    }
}
