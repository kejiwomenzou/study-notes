package com.github.kejiwomenzou.leetcode.array;

public class LC_0724_FindPivotIndex {

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
