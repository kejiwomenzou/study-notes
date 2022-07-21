package com.github.kejiwomenzou.nice_code;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

public class LinkedList {


    /**
     * 递归的形式对链表逆序打印
     * @param head
     * @return
     */
    public static void invertedOrderPrintList(ListNode head) {
        if (head == null) {
            return;
        }
        invertedOrderPrintList(head.next);
        System.out.println(head.val);
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(0);
        ListNode l5 = new ListNode(5);
        ListNode l4 = new ListNode(4);
        ListNode l3 = new ListNode(3);
        ListNode l2 = new ListNode(2);
        ListNode l1 = new ListNode(1);

        head.next = l1;
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        invertedOrderPrintList(head);
    }
}
