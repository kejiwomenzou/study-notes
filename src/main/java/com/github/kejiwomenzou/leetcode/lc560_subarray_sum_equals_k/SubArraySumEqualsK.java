package com.github.kejiwomenzou.leetcode.lc560_subarray_sum_equals_k;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/subarray-sum-equals-k/
 *
 * nums[]：原数组， prefixSum[]:前缀和数组
 *
 *  prefixSum[x]=nums[0]+nums[1]+ … +nums[x]
 *  prefixSum[x -1 ]=nums[0]+nums[1]+…+nums[x - 1]
 * => nums[x]=prefixSum[x]−prefixSum[x−1]
 * => nums[i]+ … +nums[j]= prefixSum[j]−prefixSum[i−1], i到j之间连续数组的和
 * => prefixSum[j]−prefixSum[i−1] = k, 题意求等于k
 * =>  prefixSum[j] =  k - prefixSum[i−1]
 * => 利用map记录当前j位置前缀和， 减去k, 能得到i位置前缀和
 * => 所以用map的key来记录之前遍历的i位置前缀和，value为次数， for循环内能通过preSum得到当前j位置前缀, k已知
 */
public class SubArraySumEqualsK {

    /**
     * 前缀和 + hashMap优化
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum(int[] nums, int k) {

        if (null == nums || nums.length <= 0) {
            return 0;
        }
        int len = nums.length;
        int preSum = 0;
        //key: 前缀和sum, value: 前缀和为sum出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 对于下标为 0 的元素，前缀和为 0，个数为 1
        map.put(0, 1);
        int count = 0;
        for (int i = 0; i < len; i++) {
            //计算当前i位置前缀和
            preSum += nums[i];
            //找另一个前缀和x (preSum-k),
            //因为两个前缀和之间的差等于两个数组下标位置之间数求和
            if (map.containsKey(preSum - k)) {
                count += map.get(preSum - k);
            }
            //然后把当前i位置前缀和preSum及其对应次数加1放到map中
            map.put(preSum, map.getOrDefault(preSum, 0) + 1);
        }
        return count;
    }

    /**
     * 前缀和两层遍历
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum2(int[] nums, int k) {

        if (null == nums || nums.length <= 0) {
            return 0;
        }
        int len = nums.length;
        int[] pre = new int[len + 1];
        for (int i = 0 ; i < len; i++) {
            //求前缀和
            pre[i + 1] = pre[i] + nums[i];
        }
        int count = 0;
        //每次外层循环固定i位置
        for (int i = 0; i < len; i++) {
            //内循环从i ~ j位置前缀和差是否等于k, i不动，j递增, 连续数组变长
            for (int j = i; j < len; j++) {

                if (pre[j + 1] - pre[i] == k) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 两层暴力求和
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum3(int[] nums, int k) {

        if (null == nums || nums.length <= 0) {
            return 0;
        }
        int len = nums.length;
        int count = 0;
        int sum = 0;
        for (int i = 0; i < len; i++) {

            for (int j = i; j < len; j++) {
                sum += nums[j];
                if (sum == k) {
                    count++;
                }
            }
            sum = 0;
        }
        return count;
    }
}
