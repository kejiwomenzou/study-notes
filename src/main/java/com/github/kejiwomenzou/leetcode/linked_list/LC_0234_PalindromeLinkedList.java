package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * https://leetcode.cn/problems/palindrome-linked-list/
 *
 * 234. 回文链表
 * [简单]
 */
public class LC_0234_PalindromeLinkedList {

    private ListNode temp;

    /**
     * 用有限几个变量判断
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
        if (null == head || head.next == null) {
            return true;
        }
        //找到中间节点，后半段链表头节点
        ListNode middle = middleList(head);
        //反转后半段链表
        ListNode reverse = reverseList(middle);
        //对比
        while (reverse != null) {
            if (reverse.val != head.val) {
                return false;
            }
            reverse = reverse.next;
            head = head.next;
        }
        return true;
    }

    //找中间节点,偶数节点的话返回前后一个节点
    private ListNode middleList(ListNode head) {
        if (null == head || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    //反转链表
    private ListNode reverseList(ListNode head) {
        if (null == head || head.next == null) {
            return head;
        }
        ListNode prev = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = prev;
            prev = head;
            head = temp;
        }
        return prev;
    }
    /**
     * 递归方式
     * @param head
     * @return
     */
    public boolean isPalindrome2(ListNode head) {
        temp = head;
        return check(head);
    }

    public boolean check(ListNode head) {
        if (null == head) {
            return true;
        }
        boolean res = check(head.next) && (temp.val == head.val);
        temp = temp.next;
        return res;
    }


    /**
     * 用栈
     */
    public boolean isPalindrome3(ListNode head) {

        Deque<ListNode> deque = new ArrayDeque<>();
        ListNode iter = head;
        while (iter != null) {
            deque.push(iter);
            iter = iter.next;
        }
        while (head != null) {
            if (head.val != deque.pop().val) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

}
