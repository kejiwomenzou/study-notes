package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/reverse-linked-list-ii/
 *
 * 92. 反转链表 II
 * 【中等】
 */
public class LC_0092_ReverseLinkedList_ii {

    public ListNode reverseBetween(ListNode head, int left, int right) {

        if (null == head || head.next == null) {
            return head;
        }
        //防止前一段为空，用哨兵节点
        ListNode dumy = new ListNode(0, head);
        ListNode prev = dumy;

        //前一段：prev为小于left前一段链表
        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }

        //中间一段： left开始到right结束
        ListNode middleStart = prev.next;
        prev.next = null;

        ListNode curr = middleStart;
        for (int i = left; i < right; i++) {
            curr = curr.next;
        }

        //最后一段
        ListNode end = (curr == null ? null : curr.next);
        curr.next = null;

        //反转中间一段链表
        ListNode middle = reverseList(middleStart);

        //前一段连接中间一段链表
        prev.next = middle;
        //中间连尾部链表， middleStart是反转前的头，反转后的尾
        if (middleStart != null) {
            middleStart.next = end;
        }
        return dumy.next;
    }

    private ListNode reverseList(ListNode head) {
        if (null == head || head.next == null) {
            return head;
        }
        ListNode curr = head;
        ListNode prev = null;
        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }
}
