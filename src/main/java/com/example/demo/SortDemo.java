package com.example.demo;
public class SortDemo {
    public static void main(String[] args) {

        int[] arr = {64, 34, 25, 12, 22, 11, 90};


        System.out.println("选择排序");
        int[] arr1 = arr.clone();
        selectionSort(arr1);
        printArray(arr1);

        //值传递：1.基本数据类型传递变量的副本，副本改变，实参的值不变
        //  2.引用数据类型 传递引用的地址的副本，可以用过该地址改变引用的内容的值，但是不能通过改变地址副本的值改变实参的指向
        System.out.println("\n插入排序");
        int[] arr2 = arr.clone();
        insertSort(arr2);  
        printArray(arr2);


        System.out.println("\n归并排序");
        int[] arr3 = arr.clone();
        mergeSort(arr3);
        printArray(arr3);

        
        System.out.println("\n快速排序");
        int[] arr4 = arr.clone();
        quickSort(arr4, 0, arr4.length - 1);
        printArray(arr4);
    }

    public static void printArray(int[] n){
        for(int i = 0; i < n.length; i++){
            System.out.print(n[i]+ " ");
        }
    }



    /**
     * 选择排序
     * 每次选择数组中第i小的数跟第i个位置的数交换
     */
    public static int[] selectionSort(int[] n) {
        for(int i = 0; i < n.length; i++){
            int minIndex = i;
            for(int j = i; j < n.length; j++){
                if(n[minIndex] > n[j]){
                    minIndex = j;
                }
            }
            int temp = n[i];
            n[i] = n[minIndex];
            n[minIndex] = temp;
        }
        return n;
    }

    
    /**
     * 插入排序
     * 摸一张插到手里已排好的牌中
     */
    public static void insertSort(int[] arr) {
        for(int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] >  key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }



    //归并排序
    /**
     * 归并排序
     * 1.拆分：将数组一分为二，直至只有一个元素
     * 2.合并：左右指针依次指向两边数组，比较左右指针，小的先入数组
     */
    public static void mergeSort(int[] a) {
        int[] temp = new int[a.length];
        mergeSort( a, 0, a.length - 1, temp);
    }

    public static void mergeSort(int[] a, int l, int r, int[] temp) {
        if(l >= r) return;
        int m = l + (r - l) / 2;
        mergeSort(a, l, m, temp);
        mergeSort(a, m + 1, r, temp);
        int i = l, j = m + 1, k = l;
        while(i <= m && j <= r) temp[k++] = a[i] < a[j] ? a[i++] : a[j++];
        while(i <= m) temp[k++] = a[i++];
        while(j <=r) temp[k++] = a[j++];
        for(int p = l; p <= r; p++) a[p] = temp[p];
    }


    /**
     * 快速排序
     * 选一个基准值，小的放左边，大的放右边，然后递归排左右两边
     */
    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int pivotIndex = partition(arr, left, right);

        quickSort(arr, left, pivotIndex);
        quickSort(arr, pivotIndex + 1, right);
    }

    public static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;
        
        for (int j = left; j <= right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, right);
        return i + 1;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    
}
