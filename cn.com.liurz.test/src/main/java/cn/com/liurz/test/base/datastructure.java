package cn.com.liurz.test.base;

import java.util.Arrays;

/**
 * 数据结构
 */
public class datastructure {
    public static void main(String[] args) {

        // int srcArray[] = {3,5,11,17,21,23,28,30,32,50,64,78,81,95,101};
        int data[] = {101,85,95,2,4};
        handle(data);
    }

    /**
     * 二分查找普通循环实现
     *
     * @param srcArray 有序数组
     * @param key 查找元素
     * @return
     */
    public static int binSearch(int srcArray[], int key) {
        int mid = srcArray.length / 2;
//        System.out.println("=:"+mid);
        if (key == srcArray[mid]) {
            return mid;
        }

//二分核心逻辑
        int start = 0;
        int end = srcArray.length - 1;
        while (start <= end) {
//            System.out.println(start+"="+end);
            mid = (end - start) / 2 + start;
            if (key < srcArray[mid]) {
                end = mid - 1;
            } else if (key > srcArray[mid]) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 冒泡排序
     * @param arr
     */
    public static void handle(int[] arr){
        for (int i = arr.length; i > 0; i--) {      //外层循环移动游标
            for(int j = 0; j < i && (j+1) < i; j++){    //内层循环遍历游标及之后(或之前)的元素
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    System.out.println("Sorting: " + Arrays.toString(arr));
                }
            }
        }
    }
}
