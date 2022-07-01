package com.github.kejiwomenzou.leetcode.lc061_rotate_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/rotate-list/
 */
public class RotateList {

    /**
     * 思路： 将链表倒数K个节点向右移动， 将节点收集起来， k位置将链表分成两部分，先收集后面部分，再收集链表前一部分
     */
    public ListNode rotateRight(ListNode head, int k) {

        if (null == head || k <= 0) {
            return head;
        }
        int count = 0;
        ListNode p = head;
        List<ListNode> list = new ArrayList<>();
        while (p != null) {
            list.add(p);
            p = p.next;
            count++;
        }
        if (k % count == 0) {
            return head;
        }
        ListNode virtual = new ListNode(Integer.MIN_VALUE);
        p = virtual;
        int index = count - k % count;
        for (int i = index; i < list.size(); i++) {
            p.next = list.get(i);
            p = p.next;
        }
        for (int i = 0; i < index; i++) {
            p.next = list.get(i);
            p = p.next;
        }
        p.next = null;
        return virtual.next;
    }

    /**
     * 让链表成环
     */
    public ListNode rotateRight2(ListNode head, int k) {
        if (null == head || head.next == null || k <= 0) {
            return head;
        }
        //让链表成环
        ListNode p = head;
        int count = 1;
        while (p.next != null) {
            p = p.next;
            count++;
        }
        p.next = head;
        //从头数count - k % count数量
        int start  = count - k % count;
        //head位置从1计数
        while (--start > 0) {
            head = head.next;
        }
        ListNode ret = head.next;
        head.next = null;
        return ret;
    }
}
