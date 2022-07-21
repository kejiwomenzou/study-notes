package com.github.kejiwomenzou.algo.sort.nlogn;

import java.util.Arrays;

import static com.github.kejiwomenzou.algo.common.Util.swap;

/**
 * 快排前置问题：
 *    问题1：给定一个数组arr，和一个数num，请把小于等于num的数放在数 组的左边，大于num的数放在数组的右边。
 * 要求额外空间复杂度O(1)，时间复杂度O(N)。
 *
 *    问题2：给定一个数组arr和一个数num，请把小于num的数放在数组的左边，等于num的数放在数组的中间，
 * 大于num的数放在数组的右边。要求额外空间复杂度为O(1)，时间复杂度为O(N)。
 */
public class Pre_QuickSort01 {

    /**
     * 问题1解决： 左右指针分别代表小于等于num的左边范围和大于num的右边范围
     * @param arr
     * @param num
     */
    public static void fun1(int[] arr, int num) {
        if (null == arr || arr.length <= 1) {
            return;
        }
        int i = 0;
        int j = arr.length - 1;
        while (i < j) {
            while (arr[i] <= num) {
                i++;
            }
            while (arr[j] > num) {
                j--;
            }
            if (i < j) {
                swap(arr, i ,j);
            }
        }
    }

    /**
     * 问题2：左边小于区域， 中间等于区域，右边大于区域
     *      左边向右推进， 右边向左推进
     *
     *
     * 初始化less=-1，more=arr.length，当前遍历位置为index=0。
     *
     *      如果arr[index]<num，交换arr[index]和arr[++less]的数，然后index++【左边区域向右推进,交换左边区域前一个位置 ， index++】
     *      如果arr[index]>num，交换arr[index]和arr[–more]的数，然后cur不变【右边区域向左推进,交换右边区域前一个位置，index不动】
     *      如果当前位置上的数等于num，less和more均不变，index++
     *      当index==more时，停止比较，返回
     */
    public static void fun2(int[] arr, int num) {
        if (null == arr || arr.length <= 1) {
            return;
        }
        int less = -1;//左边小于等于区域
        int more = arr.length;//右边在于区域
        int index = 0;
        while (index < more) {
            if (arr[index] < num) {
                swap(arr, ++less, index++);
            } else if (arr[index] == num) {
                index++;
            } else {
                swap(arr, --more, index);
            }
            System.out.println("小于区域： " + printSubArray(arr, 0, less) + ", 大于区域：" + printSubArray(arr, more, arr.length - 1));
        }
    }

    private static String printSubArray(int[] arr, int left, int right) {
        if (left > right) {
            return "[]";
        }
        int len = right - left + 1;
        int[] newArr = new int[len];
        System.arraycopy(arr, left, newArr, 0, len);
        return Arrays.toString(newArr);
    }
    public static void main(String[] args) {
        int[] arr = {1, 5, 1, 8, 3, 5, 7, 2};
        fun2(arr, 2);
        System.out.println(Arrays.toString(arr));
    }
}
