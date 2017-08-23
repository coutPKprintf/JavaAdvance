package com.alpha.algo.bst.common;

import com.alpha.algo.bst.Tree;

import java.util.Comparator;

/**
 * Created by chenwen on 17/8/17.
 * 普通二叉查找树, 不允许出现相同元素的情况
 */
public class CommonBinarySearchTree<T> implements Tree<T>{
    //根节点
    private TreeNode<T> root;

    //节点个数
    private int size;

    //比较器
    private final Comparator<T> comparator;

    public CommonBinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    //树节点
    final static class TreeNode<T> {
        T item;

        TreeNode<T> left;

        TreeNode<T> right;

        TreeNode<T> parent;

        boolean isLeft;

        public TreeNode(T item, TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent, boolean isLeft) {
            this.item = item;
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.isLeft = isLeft;
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
        boolean result = item != null && (root = add(null, root, item, false)) != null;
        if (result){
            ++size;
        }
        return result;
    }

    /**
     * 递归插入
     * @param parent 父节点
     * @param node 子树根节点
     * @param item 待添加节点
     * @return
     */
    private TreeNode<T> add(TreeNode<T> parent, TreeNode<T> node, T item, boolean isLeft){
        if (node == null){
            return newNode(item, null, null, parent, isLeft);
        }else {
            int compare = comparator.compare(node.item, item);
            if (compare == 0){
                node.item = item;
            }else if (compare > 0){
                node.left = add(node, node.left, item, true);
            }else {
                node.right = add(node, node.right, item, false);
            }
            return node;
        }
    }

    /**
     * 删除指定元素
     * @param item 元素
     * @return 删除成功返回true, 否则返回false
     */
    public boolean delete(T item){
        TreeNode<T> node;
        boolean result = (node = find(root, item)) != null && delete(node);
        if (result){
            --size;
        }
        return result;
    }

    /**
     * 删除指定节点
     * @param node 待删除节点
     * @return 是否删除成功
     */
    private boolean delete(TreeNode<T> node){
        //如果该节点是叶子节点
        if (node.left == null && node.right == null){
            //如果删除节点为根节点
            if (node.parent == null){
                root = null;
            }else {
                if (node.isLeft){
                    node.parent.left = null;
                }else {
                    node.parent.right = null;
                }
            }
        }else if (node.left == null || node.right == null){
            //如果左子树为空 或 右子树为空
            if (node.left == null){
                //如果删除节点为根节点
                if (node.parent == null){
                    root = node.right;
                    root.parent = null;
                }else {
                    if (node.isLeft) {
                        node.parent.left = node.right;
                    } else {
                        node.parent.right = node.right;
                    }
                    node.right.parent = node.parent;
                    node.right.isLeft = node.isLeft;
                }
            }else {
                //如果删除节点为根节点
                if (node.parent == null){
                    root = node.left;
                    root.parent = null;
                }else {
                    if (node.isLeft) {
                        node.parent.left = node.left;
                    } else {
                        node.parent.right = node.left;
                    }
                    node.left.parent = node.parent;
                    node.left.isLeft = node.isLeft;
                }
            }
        }else {
            //如果删除节点为根节点
            if (node.parent == null){
                TreeNode<T> minNode = findMin(node.right);
                minNode.left = node.left;
                node.left.parent = minNode;
                root = node.right;
                root.parent = null;
            }else {
                if (node.isLeft){
                    node.parent.left = node.right;
                    node.right.parent = node.parent;
                    node.right.isLeft = true;

                    TreeNode<T> minNode = findMin(node.right);
                    minNode.left = node.left;
                    node.left.parent = minNode;
                }else {
                    node.parent.right = node.right;
                    node.right.parent = node.parent;

                    TreeNode<T> minNode = findMin(node.right);
                    minNode.left = node.left;
                    node.left.parent = minNode;
                }
            }
        }

        node.left = null;
        node.right = null;
        node.parent = null;
        return true;
    }

    /**
     * 获取树大小
     * @return
     */
    public int size() {
        return size;
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
     * @return
     */
    private TreeNode<T> newNode(T item, TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent, boolean isLeft){
        return new TreeNode<T>(item, left, right, parent, isLeft);
    }
}
