package com.alpha.algo.bst.banlance23;

import com.alpha.algo.bst.Tree;

import java.util.Comparator;

/**
 * Created by chenwen on 17/8/17.
 * 平衡查找树之2-3树
 */
public class Balance23Tree<T> implements Tree<T> {
    //根节点
    private TreeNode root;

    //
    private TreeNode balanceNode;

    //节点个数
    private int size;

    private int deep;

    //比较器
    private final Comparator<T> comparator;

    public Balance23Tree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    final class TreeNode{
        T lItem;

        T rItem;
        
        T extItem;

        TreeNode left;

        TreeNode mid;

        TreeNode right;
        
        TreeNode ext;

        TreeNode parent;

        boolean is3Node;

        public TreeNode(T lItem, T rItem, TreeNode left, TreeNode mid,
                        TreeNode right, boolean is3Node, TreeNode parent) {
            this.lItem = lItem;
            this.rItem = rItem;
            this.left = left;
            this.mid = mid;
            this.right = right;
            this.is3Node = is3Node;
            this.parent = parent;
            if (this.left != null) {
                this.left.parent = this;
            }
            if (this.mid != null) {
                this.mid.parent = this;
            }
            if (this.right != null){
                this.right.parent = this;
            }
        }
        
        public void addExtItem(TreeNode node2){
            if (comparator.compare(node2.lItem, rItem) > 0){
                extItem = node2.lItem;
                right = node2.left;
                ext = node2.right;
            }else if (comparator.compare(node2.lItem, lItem) < 0){
                extItem = rItem;
                ext = right;

                rItem = lItem;
                right = mid;

                lItem = node2.lItem;
                left = node2.left;
                mid = node2.right;
            }else {
                extItem = rItem;
                ext = right;

                rItem = node2.lItem;
                right = node2.right;
                mid = node2.left;
            }

            if (node2.left != null){
                node2.left.parent = this;
            }

            if (node2.right != null){
                node2.right.parent = this;
            }
        }

        public void split(){
            TreeNode leftNode = newNode(lItem, null, left, null, mid, false, this);
            TreeNode rightNode = newNode(extItem, null, right, null, ext, false, this);

            this.left = leftNode;

            this.right = rightNode;

            this.lItem = this.rItem;

            this.is3Node = false;

            this.ext = null;

            this.extItem = null;

            this.rItem = null;

            this.mid = null;
        }
    }



    public boolean delete(T item) {
        return false;
    }

    public boolean find(T item) {
        return find(root, item);
    }

    private boolean find(TreeNode node, T item){
        if (node == null){
            return false;
        }

        int lcom = comparator.compare(node.lItem, item);

        if (lcom == 0){
            return true;
        }

        if (node.is3Node){
            if (lcom > 0){
                return find(node.left, item);
            }else {
                int rcom = comparator.compare(node.rItem, item);

                if (rcom == 0){
                    return true;
                }

                if (rcom > 0){
                    return find(node.mid, item);
                }else {
                    return find(node.right, item);
                }
            }
        }else {
            if (lcom > 0){
                return find(node.left, item);
            }else {
                return find(node.right, item);
            }
        }
    }

    public boolean add(T item) {
        boolean result = item != null && (root = add(null, root, item)) != null;
        if (balanceNode != null){
            balance(balanceNode, newNode(item, null, null, null, null, false, balanceNode));
            balanceNode = null;
        }
        return result;
    }


    private TreeNode add(TreeNode parent, TreeNode node, T item){
        if (node == null){
            ++size;
            ++deep;
            return newNode(item, null, null, null, null, false, parent);
        }

        int lcom = comparator.compare(node.lItem, item);

        if (lcom == 0) {
            node.rItem = item;
        }else {
            //如果为叶子节点
            if (isLeaf(node)) {
                if (node.is3Node) {
                    balanceNode = node;
                } else {
                    if (lcom > 0) {
                        node.rItem = node.lItem;
                        node.lItem = item;
                    } else {
                        node.rItem = item;
                    }
                    node.is3Node = true;
                }
                ++size;
            } else {
                //如果为非叶子节点
                if (node.is3Node) {
                    if (lcom > 0) {
                        node.left = add(node, node.left, item);
                    } else {
                        int rcom = comparator.compare(node.rItem, item);

                        if (rcom == 0) {
                            node.rItem = item;
                        }else if (rcom > 0) {
                            node.mid = add(node, node.mid, item);
                        } else {
                            node.right = add(node, node.right, item);
                        }
                    }
                } else {
                    if (lcom > 0) {
                        node.left = add(node, node.left, item);
                    } else {
                        node.right = add(node, node.right, item);
                    }
                }
            }
        }
        return node;
    }

    private void balance(TreeNode node, TreeNode item){
        if (node.is3Node){

            node.addExtItem(item);

            node.split();

            if (node.parent == null){
                root = node;
                ++deep;
            }else {
                balance(node.parent, node);
            }
        }else {
            int lcom = comparator.compare(node.lItem, item.lItem);

            if (lcom > 0){
                node.rItem = node.lItem;

                node.lItem = item.lItem;

                node.left = item.left;

                node.mid = item.right;
            }else {
                node.rItem = item.lItem;

                node.mid = item.left;

                node.right = item.right;
            }

            node.is3Node = true;

            if (item.left != null){
                item.left.parent = node;
            }
            if (item.right != null){
                item.right.parent = node;
            }
        }
    }

    private T getMin(T a, T b, T c){
        T temp = comparator.compare(a, b) > 0 ? b : a;
        return comparator.compare(temp, c) > 0 ? c : temp;
    }

    private T getMax(T a, T b, T c){
        T temp = comparator.compare(a, b) < 0 ? b : a;
        return comparator.compare(temp, c) < 0 ? c : temp;
    }

    private boolean isLeaf(TreeNode node){
        return node.left == null && node.right == null && node.mid == null;
    }

    public int size() {
        return size(root);
    }

    private int size(TreeNode node) {
        if (node == null){
            return 0;
        }
        return node.is3Node ? 2 + size(node.left) + size(node.mid) + size(node.right) : 1 + size(node.left) + size(node.right);
    }

    public void show() {

    }

    private TreeNode newNode(T lItem, T rItem, TreeNode left, TreeNode mid, TreeNode right, boolean is3Node, TreeNode parent){
        return new TreeNode(lItem, rItem, left, mid, right, is3Node, parent);
    }
}
