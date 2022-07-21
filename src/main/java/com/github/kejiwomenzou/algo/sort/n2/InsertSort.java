package com.github.kejiwomenzou.algo.sort.n2;

import com.github.kejiwomenzou.algo.common.Util;

import java.util.Arrays;

public class InsertSort {

    public static void insertSort(int[] arr) {
        if (null == arr || arr.length <= 1) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {

            for (int j = i - 1; j >= 0 && arr[j] > arr[j+1]; j--) {
                Util.swap(arr, j, j+1);
            }
            /**
             *             int j = i - 1;
             *             while (j >= 0) {
             *                 if (arr[j] > arr[j + 1]) {
             *                     Util.swap(arr, j, j+1);
             *                 }
             *                 j--;
             *             }
             */
        }
    }

    public static void main(String[] args) {
        int[] arr = {2,345,234,51,3,33,7,85,0};
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
