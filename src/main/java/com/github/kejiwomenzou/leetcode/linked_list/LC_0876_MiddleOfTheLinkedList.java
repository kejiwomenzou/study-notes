package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/middle-of-the-linked-list/
 *
 * 876. 链表的中间结点
 */
public class LC_0876_MiddleOfTheLinkedList {

    public ListNode middleNode(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
