package com.alpha.algo.bst.common;

import com.alpha.algo.bst.Tree;

import java.util.Comparator;

/**
 * Created by chenwen on 17/8/17.
 * 普通二叉查找树, 不允许出现相同元素的情况
 */
public class CommonBinarySearchTreeExt<T> implements Tree<T>{
    //根节点
    private TreeNode<T> root;

    //比较器
    private final Comparator<T> comparator;

    public CommonBinarySearchTreeExt(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    //树节点
    final static class TreeNode<T> {
        T item;

        TreeNode<T> left;

        TreeNode<T> right;

        int size;

        public TreeNode(T item, TreeNode<T> left, TreeNode<T> right, int size) {
            this.item = item;
            this.left = left;
            this.right = right;
            this.size = size;
        }
    }


    /**
     * 查找是否包含该元素
     * @param item 待查找元素
     * @return 如果存在返回true, 否则返回false
     */
    public boolean find(T item) {
        return item != null && find(root, item) != null;
    }

    /**
     * 递归查找
     * @param node 子树根节点
     * @param item 待查找元素
     * @return
     */
    private TreeNode<T> find(TreeNode<T> node, T item){
        if (node == null){
            return null;
        }
        int compare = comparator.compare(node.item, item);
        if (compare == 0){
            return node;
        }else if (compare > 0){
            return find(node.left, item);
        }else {
            return find(node.right, item);
        }
    }

    /**
     * 添加新元素
     * @param item 待添加节点
     * @return 如果添加成功返回true, 否则返回false
     */
    public boolean add(T item){
        return item != null && (root = add(root, item)) != null;
    }

    /**
     * 递归插入
     * @param node 子树根节点
     * @param item 待添加节点
     * @return
     */
    private TreeNode<T> add(TreeNode<T> node, T item){
        if (node == null){
            return newNode(item, null, null, 1);
        }else {
            int compare = comparator.compare(node.item, item);
            if (compare == 0){
                node.item = item;
            }else if (compare > 0){
                node.left = add(node.left, item);
            }else {
                node.right = add(node.right, item);
            }
            node.size = sizeOf(node.left) + sizeOf(node.right) + 1;
            return node;
        }
    }

    /**
     * 统计树大小
     * @param node 根节点
     * @return
     */
    private int sizeOf(TreeNode<T> node){
        if (node == null){
            return 0;
        }else {
            return node.size;
        }
    }

    /**
     * 删除指定元素
     * @param item 元素
     * @return 删除成功返回true, 否则返回false
     */
    public boolean delete(T item){
        root = delete(root, item);
        return true;
    }

    /**
     * 删除指定节点
     * @param node 待删除节点
     * @return 是否删除成功
     */
    public TreeNode<T> delete(TreeNode<T> node, T item){
        if (node == null){
            return null;
        }

        int compare = comparator.compare(node.item, item);

        if (compare > 0){
            node.left = delete(node.left, item);
        }else if (compare < 0){
            node.right = delete(node.right, item);
        }else {
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            else {
                TreeNode<T> minNode = findMin(node.right);
                node.item = minNode.item;
                node.right = delete(node.right, minNode.item);
            }
        }

        node.size = sizeOf(node.left) + sizeOf(node.right) + 1;

        return node;
    }

    /**
     * 获取树大小
     * @return
     */
    public int size() {
        return root == null ? 0 : root.size;
    }

    /**
     * 查找最大的节点
     * @param node 根节点
     * @return
     */
    private TreeNode<T> findMax(TreeNode<T> node){
        if (node.right == null){
            return node;
        }
        return findMax(node.right);
    }


    /**
     * 查找最小的节点
     * @param node 根节点
     * @return
     */
    private TreeNode<T> findMin(TreeNode<T> node){
        if (node.left == null){
            return node;
        }
        return findMin(node.left);
    }

    public void show(){
        show(root);
    }

    private void show(TreeNode<T> node){
        if (node == null){
            return;
        }

        show(node.left);

        System.out.println(node.item);

        show(node.right);
    }


    /**
     * 新建节点
     * @param item 元素
     * @param left 左子树
     * @param right 右子树
     * @param size 子节点树
     * @return
     */
    private TreeNode<T> newNode(T item, TreeNode<T> left, TreeNode<T> right, int size){
        return new TreeNode<T>(item, left, right, size);
    }
}
