package com.github.kejiwomenzou.leetcode.linked_list;

import com.github.kejiwomenzou.leetcode.linked_list.common.ListNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LC_0083_RemoveDuplicatesFromSortedList {


    /**
     * 注意审题： 链表已排好序！！
     */
    public ListNode deleteDuplicates(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.next.val == cur.val) {
                //断开节点
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     * set对值进行去重， list顺序收集去重后的节点，再拼接起来
     */
    public ListNode deleteDuplicates1(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        Set<Integer> set = new HashSet<>();
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            if (!set.contains(head.val)) {
                list.add(head);
            }
            set.add(head.val);
            head = head.next;
        }
        ListNode node = list.get(0);
        ListNode ret = node;
        for (int i = 1; i < list.size(); i++) {
            node.next = list.get(i);
            node = node.next;
        }
        node.next = null;
        return ret;
    }

    /**
     * 两指针遍历，一个指向当前节点；一个指向下一节点，往后遍历，有重复则断掉节点
     */
    public ListNode deleteDuplicates2(ListNode head) {

        if (null == head || head.next == null) {
            return head;
        }
        ListNode cur = head;
        //当前所在节点
        while (cur != null) {
            //下一节点， 一直向后遍历
            ListNode iter = cur.next;
            //当前节点的下一节点
            ListNode next = iter;
            while (iter != null) {
                if (iter.val == cur.val) {
                    //下一节点重复，跳过，断开该节点
                    next = iter.next;
                }
                iter = iter.next;
            }
            cur.next = next;
            cur = cur.next;
        }
        return head;
    }
}
