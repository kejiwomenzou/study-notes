package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/reverse-linked-list/
 * 206. 反转链表
 */
public class LC_0206_ReverseLinkedList {

    public ListNode reverseList(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode cur = head;
        //记录转的链表
        ListNode prev = null;
        while (cur != null) {
            //记录下一个遍历节点
            ListNode temp = cur.next;
            //当前节点拼上prev, 头插
            cur.next = prev;
            prev = cur;

            cur = temp;
        }
        return prev;
    }
}
