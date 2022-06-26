package com.github.kejiwomenzou.leetcode_top.linked_list.lc021_merge_two_sorted_lists;

import com.github.kejiwomenzou.leetcode_top.linked_list.common.ListNode;

/**
 * https://leetcode.cn/problems/merge-two-sorted-lists/
 */
public class MergerTwoSortedLists {

    /**
     * 利用虚拟节点，遍历拼接
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        if (list1 == null || list2 == null) {
            return list1 == null ? list2 : list1;
        }
        //利用虚拟头节点，便于返回合并后的头节点
        ListNode virtualNode = new ListNode(Integer.MIN_VALUE);
        //用于遍历拼接节点
        ListNode help = virtualNode;
        while (list1 != null && list2 != null) {

            ListNode next;
            if (list1.val <= list2.val) {
                next = list1;
                list1 = list1.next;
            } else {
                next = list2;
                list2 = list2.next;
            }
            help.next = next;
            help = help.next;
        }
        //拼接剩下的节点
        help.next = (list1 == null ? list2 : list1);
        return virtualNode.next;
    }

    /**
     * 递归方式1: 开始想的是把虚拟节点带入到递归方法中，去拼接切点， 后面看评论区发现还可以
     * 用更加优雅的方式： mergeTwoLists3
     */
    public ListNode mergeTwoLists2(ListNode list1, ListNode list2) {
        ListNode head = new ListNode(Integer.MIN_VALUE);
        merge(list1, list2, head);
        return head.next;
    }

    private void merge(ListNode list1, ListNode list2, ListNode virtual) {

        if (list1 == null || list2 == null) {
            virtual.next = list1 == null ? list2 : list1;
            return;
        }
        if (list1.val <= list2.val) {
            virtual.next = list1;
            virtual = virtual.next;
            merge(list1.next, list2, virtual);
        } else {
            virtual.next = list2;
            virtual = virtual.next;
            merge(list1, list2.next, virtual);
        }
    }

    /**
     * 用递归时考虑的点：；递归时程序给保存了当前层级栈（或者说上下文），其中包含了本层的入参及变量值等信息
     *      <p> 本级方法含义： 当前方法干了啥， 因为递归一直在调用当前方法，只是参数值和返回值有变化
     *      <p> 递归出口或者说边界条件： 边界条件是怎样的，没出口就stack overflow了
     *      <p> 返回值， 有返回值的话含义怎么样的，因为返回值会返回给上层方法
     */
    public ListNode mergeTwoLists3(ListNode list1, ListNode list2) {

        //返回拼接好的链表
        if (list1 == null || list2 == null) {
            return (list1 == null) ? list2 : list1;
        } else if (list1.val < list2.val) {
            //当前层保留了list1, list1.next指向拼接好的链接
            list1.next = mergeTwoLists3(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists3(list1, list2.next);
            return list2;
        }
    }
}