package com.alpha.algo.bst;

/**
 * Created by chenwen on 17/8/17.
 * 树接口
 */
public interface Tree<T> {
    boolean delete(T item);

    boolean find(T item);

    boolean add(T item);

    int size();

    void show();
}
