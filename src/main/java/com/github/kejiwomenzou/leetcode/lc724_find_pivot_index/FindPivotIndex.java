package com.github.kejiwomenzou.leetcode.lc724_find_pivot_index;

public class FindPivotIndex {

    public int pivotIndex(int[] nums) {

        int sum = 0;
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            sum += nums[i];
        }
        //左边 *2 + 中心下标 = 总和
        int leftSum = 0;
        for (int i = 0; i < len; i++) {
            if (leftSum * 2  + nums[i] == sum) {
                return i;
            }
            leftSum += nums[i];
        }
        return -1;
    }
}
