package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/sort-list/
 * 148. 排序链表
 */
public class LC_0148_SortList {

    public ListNode sortList(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        ListNode midNode = middleNode(head);
        ListNode rightHead = midNode.next;
        midNode.next = null;

        ListNode left = sortList(head);
        ListNode right = sortList(rightHead);

        return merge(left, right);
    }

    // 合并两个有序链表（21. 合并两个有序链表）
    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode virtual = new ListNode();
        ListNode curr = virtual;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        curr.next = (l1 == null ? l2 : l1);
        return virtual.next;
    }

    //  找到链表中间节点（876. 链表的中间结点）
    private ListNode middleNode(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        ListNode slow = head, fast = head.next.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
