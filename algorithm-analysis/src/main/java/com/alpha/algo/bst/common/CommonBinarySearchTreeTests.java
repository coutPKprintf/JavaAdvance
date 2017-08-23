package com.alpha.algo.bst.common;


import com.alpha.algo.bst.Tree;
import com.alpha.algo.bst.banlance23.Balance23Tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by chenwen on 17/8/17.
 * 二叉查找树测试
 */
public class CommonBinarySearchTreeTests {

    final static Comparator<Integer> comparator = new Comparator<Integer>() {
        public int compare(Integer o1, Integer o2) {
            if (o1 == null || o2 == null){
                System.out.println("");
                return -1;
            }
            if (o1 > o2){
                return 1;
            }else if (o1 < o2){
                return -1;
            }
            return 0;
        }
    };

    @Test
    public void test(){
        CommonBinarySearchTreeExt<Integer> tree = new CommonBinarySearchTreeExt<Integer>(comparator);

        int numCount = 10000000;

        List<Integer> nums = new ArrayList<Integer>();
        for(int i = 0; i < numCount; ++i){
            nums.add(i);
        }

        Collections.shuffle(nums);

        int root = nums.get(0);

        for(Integer num : nums){
            tree.add(num);
        }

//        tree.show();


        Assert.assertEquals(true, tree.find(root));

        tree.delete(root);

        Assert.assertEquals(false, tree.find(root));

        tree.add(root);

        Collections.shuffle(nums);


        int delCount = 0;

        for(Integer num : nums){
            Assert.assertEquals(true, tree.find(num));


            Assert.assertEquals(tree.size(), nums.size() - delCount);

            tree.delete(num);
            ++delCount;

            Assert.assertEquals(false, tree.find(num));
        }
    }

    @Test
    public void testTime(){

        int numCount = 5000000;
        List<Integer> nums = new ArrayList<Integer>();
        for(int i = 0; i < numCount; ++i){
            nums.add(i);
        }
        Collections.shuffle(nums);

        List<Integer> findNums = new ArrayList<Integer>();
        for(int i = 0; i < numCount * 2; ++i){
            findNums.add(new Random().nextInt( numCount * 5 ));
        }

        new TreeTimeTest(nums, findNums, numCount, new CommonBinarySearchTree<Integer>(comparator)).run();

        new TreeTimeTest(nums, findNums, numCount, new CommonBinarySearchTreeExt<Integer>(comparator)).run();

        new TreeTimeTest(nums, findNums, numCount, new Balance23Tree<Integer>(comparator)).run();
    }

    public static class TreeTimeTest{
        private List<Integer> nums;

        private List<Integer> findNums;

        private int numCount;

        private Tree<Integer> tree;

        public TreeTimeTest(List<Integer> nums, List<Integer> findNums, int numCount, Tree<Integer> tree) {
            this.nums = nums;
            this.findNums = findNums;
            this.numCount = numCount;
            this.tree = tree;
        }

        public void run(){

            for(Integer num : nums){
                tree.add(num);
            }

            long startTime = System.currentTimeMillis();

            for(int i = 0; i < findNums.size(); ++i){
                Assert.assertEquals(findNums.get(i) < numCount, tree.find(findNums.get(i)));
            }

            long endTime = System.currentTimeMillis();


            System.out.println(tree.getClass().getName() + " 消耗时间 " + (endTime - startTime) );
        }
    }

    public static boolean find(List<Integer> nums, Integer find){
        for(Integer num : nums){
            if (comparator.compare(num, find) == 0){
                return true;
            }
        }

        return false;
    }

}
