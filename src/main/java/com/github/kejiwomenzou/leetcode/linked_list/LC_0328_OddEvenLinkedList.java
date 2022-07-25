package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/odd-even-linked-list/
 * 328. 奇偶链表
 * 【中等】
 */
public class LC_0328_OddEvenLinkedList {

    public ListNode oddEvenList(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }
        ListNode curr = head;
        //奇数
        ListNode oddDumy = new ListNode();
        ListNode odd = oddDumy;

        //偶数
        ListNode evenDumy = new ListNode();
        ListNode even = evenDumy;
        boolean isEven = false;
        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = null;
            if (isEven) {
                even.next = curr;
                even = even.next;
            } else {
                odd.next = curr;
                odd = odd.next;
            }
            isEven = !isEven;
            curr = temp;
        }
        //奇数为空
        if (oddDumy.next == null) {
            return evenDumy.next;
        }
        //奇连偶
        odd.next = evenDumy.next;
        return oddDumy.next;
    }

    /**
     * 更简洁的代码
     * @param head
     * @return
     */
    public ListNode oddEvenList2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        //记住旧的偶的头
        ListNode oldHead = head.next;

        //第一个节点奇
        ListNode odd = head;
        //第二个节点偶
        ListNode even = head.next;

        while (even != null && even.next != null) {

            //跳着连接
            odd.next = even.next;
            odd = odd.next;

            even.next = odd.next;
            even = even.next;
        }
        //最后奇连旧的偶的头
        odd.next = oldHead;
        return head;
    }
}
