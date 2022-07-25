package com.github.kejiwomenzou.leetcode;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  https://leetcode.cn/problems/merge-k-sorted-lists/submissions/
 */
public class LC_0023_MergeKSortedLists {

    /**
     * 两两有序链表合并
     */
    public ListNode mergeKLists(ListNode[] lists) {

        if (null == lists || lists.length == 0) {
            return null;
        }
        ListNode ret = lists[0];
        for (int i = 1; i < lists.length; i++) {
            ret = mergeTwoLists(ret, lists[i]);
        }
        return ret;
    }

    /**
     * 合并两个有序链表
     */
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    /**
     * 投机取巧： 把链表值收集起来排序，重新建节点拼成新链接
     * 耗时更短~~ 6ms
     */
    public ListNode mergeKLists2(ListNode[] lists) {

        if (null == lists || lists.length == 0) {
            return null;
        }
        List<Integer> counter = new ArrayList<>();
        for (ListNode list : lists) {
            while (list != null) {
                counter.add(list.val);
                list = list.next;
            }
        }
        if (counter.size() <= 0) {
            return null;
        }
        Collections.sort(counter);
        ListNode first = new ListNode(counter.get(0));
        ListNode head = first;
        for (int i = 1; i < counter.size(); i++) {
            first.next = new ListNode(counter.get(i));
            first = first.next;
        }
        return head;
    }
}
