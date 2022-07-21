package com.github.kejiwomenzou.leetcode.palindrome_inked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class PalindromeLinkedList {

    private ListNode temp;

    /**
     * 递归方式
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
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


    public boolean isPalindrome2(ListNode head) {

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
