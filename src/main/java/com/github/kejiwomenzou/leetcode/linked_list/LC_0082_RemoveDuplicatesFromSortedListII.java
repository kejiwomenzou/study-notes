package com.github.kejiwomenzou.leetcode.lc082_remove_duplicates_from_sorted_list_ii;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.HashMap;
import java.util.Map;

public class RemoveDuplicatesFromSortedListII {

    public ListNode deleteDuplicates(ListNode head) {

        //TODO 用递归解决
        return null;
    }

        /**
         * 一次遍历，记录前一个节点
         * @param head
         * @return
         */
    public ListNode deleteDuplicates2(ListNode head) {
        if (null == head || head.next == null) {
            return head;
        }
        ListNode vir = new ListNode(Integer.MIN_VALUE);
        vir.next = head;

        //保存上一次删除的节点
        ListNode lastDel = null;
        //删除或者未删除节点的前一节点，否则无法返回链表头节点
        ListNode pre = vir;
        ListNode node = vir.next;

        while (node != null) {
            //当前节点与上一次删除的节点相同
            if (lastDel != null && node.val == lastDel.val) {
                lastDel = node;
                node = node.next;
                pre.next = node;
                continue;
            }
            //连续两个节点相同
            if (node.next != null && node.next.val == node.val) {
                lastDel = node;
                node = node.next.next;
                pre.next = node;
                continue;
            }
            //后一节点与当前节点不同
            pre = node;
            node = node.next;
        }
        return vir.next;
    }

    /**
     * 用map: ListNode.val -> count
     */
    public ListNode deleteDuplicates3(ListNode head) {
        if (null == head || head.next == null) {
            return head;
        }
        Map<Integer, Integer> map = new HashMap<>();
        ListNode p = head;
        while (p != null) {
            if (map.containsKey(p.val)) {
                map.put(p.val, map.get(p.val) + 1);
            } else {
                map.put(p.val, 1);
            }
            p = p.next;
        }

        ListNode node = new ListNode(Integer.MIN_VALUE);
        ListNode iter = node;

        while (head != null) {
            int count = map.get(head.val);
            map.put(head.val, -1);
            if (count == 1) {
                ListNode m = head;
                head = head.next;
                m.next = null;
                iter.next = m;
                iter = iter.next;
            } else {
                head = head.next;
            }
        }
        return node.next;
    }
}
