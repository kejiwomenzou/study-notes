package com.github.kejiwomenzou.leetcode.array;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/count-number-of-nice-subarrays/
 * 1248. 统计「优美子数组」
 */
public class LC_1248_CountNumberOfNiceSubarrays {

    public int numberOfSubarrays(int[] nums, int k) {

        int len = nums.length;
        //key:0~i之间奇数个数和， value:个数
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int sum = 0;
        int count = 0;
        for (int i = 0; i < len; i++) {
            sum += nums[i] & 1;
            if (map.containsKey(sum - k)) {
                count += map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}
