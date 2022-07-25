package com.github.kejiwomenzou.algo.sort.nlogn;

import java.util.Arrays;

/**
 * 归并排序: 时间复杂度 O(NlogN) 额外空间复杂度：O(N)
 */
public class MergeSort {

    public static void mergeSort(int[] array) {
        if (null == array || array.length < 2) {
            return;
        }
        process(array, 0, array.length - 1);
    }

    /**
     * 该方法的含义是让数组 L ~ R 范围上有序
     */
    private static void process(int[] array, int L, int R) {
        if (L == R) {
            System.out.println("L = " + L + ", R = " + R);
            return;
        }
        int mid = L + ((R - L) >> 1);
        //让数组左边L~mid位置有序
        process(array, L, mid);
        //让数组右边mid + 1 到R范围上有序
        process(array, mid + 1, R);
        //合并左右两边，让数组array的L ~ R上整体有序
        merge(array, L, mid, R);
    }

    private static void merge(int[]  arr, int L, int mid, int R) {

        //左指针从0开始，右指针从M+1开始
        int left = L;
        int right = mid + 1;
        int[] help = new int[R - L + 1];
        int i = 0;
        while (left <= mid && right <= R) {
            help[i++] = (arr[left] <= arr[right] ? arr[left++] : arr[right++]);
        }
        while (left <= mid) {
            help[i++] = arr[left++];
        }
        while (right <= R) {
            help[i++] = arr[right++];
        }
        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
    }

    public static void main(String[] args) {
        //0 ~ 6
        int[] arr = {4,6,1,3,7,5,9};
        mergeSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
