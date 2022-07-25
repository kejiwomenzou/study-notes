package com.github.kejiwomenzou.tips.algo;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

public class Code01_MiddleListNode {

    /**
     * 找链表的中间节点： 偶数节点时，返回中间前一个节点还是后一个节点的判断
     *
     * @param head
     * @return
     */
    public static ListNode middleNode(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;

        /**
         *  链表奇数节点时，slow最终停在中间节点
         *  偶数节点返回两个中间节点的【后】一个节点 因为这样判断slow和fast会多往后跳一下
         */
        while (fast != null && fast.next != null) { //注意这行区别
            slow = slow.next;
            fast = fast.next.next;
        }

        /**
         *  链表奇数节点时，slow最终停在中间节点
         *  偶数节点返回两个中间节点的【前】一个节点
         */
//        while (fast.next != null && fast.next.next != null) { 注意这行区别
//            slow = slow.next;
//            fast = fast.next.next;
//        }
        return slow;
    }

}
