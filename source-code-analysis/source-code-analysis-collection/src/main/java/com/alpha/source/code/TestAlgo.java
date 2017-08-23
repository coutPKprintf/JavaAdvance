package com.alpha.source.code;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenwen on 17/8/16.
 */
public class TestAlgo {

    public static boolean isMaxOrMin(int a, int b, int c){
        return isMax(a, b, c) || isMin(a, b, c);
    }

    public static boolean isMax(int a, int b, int c){
        return a > b && a > c;
    }

    public static boolean isMin(int a, int b, int c){
        return a < b && a < c;
    }

    public static boolean isBetween(int a, int b, int c){
        return (a > b & a < c) || (a < b && a > c);
    }

    public static boolean has(int[] arr, int left, int right, int value){
        if (arr == null || left >= right || right <= 0){
            return false;
        }

        if (right - left == 1){
            return value == arr[left];
        }

        int mid = (right + left) >> 1;

        if (arr[mid] == value || arr[left] == value || arr[right - 1] == value) {
            return true;
        }

        if (arr[left] == arr[right - 1]){
            return has(arr, left + 1, right - 1, value);
        }

        if (isMaxOrMin(arr[mid], arr[left], arr[right - 1])){
            if (isMax(arr[mid], arr[left], arr[right - 1])){
                if (arr[left] < arr[right - 1]) {
                    if (isBetween(value, arr[mid], arr[right - 1])){
                        return has(arr, mid + 1, right, value);
                    }else {
                        return has(arr, left, mid, value);
                    }
                }else {
                    if (isBetween(value, arr[mid], arr[left])){
                        return has(arr, left, mid, value);
                    }else {
                        return has(arr, mid + 1, right, value);
                    }
                }
            }else {
                if (arr[left] > arr[right - 1]) {
                    if (value < arr[mid]){
                        return has(arr, left ,mid, value);
                    }else {
                        return has(arr, mid + 1, right, value);
                    }
                }else {
                    if (value < arr[mid]){
                        return has(arr, mid + 1, right, value);
                    }else {
                        return has(arr, left, mid, value);
                    }
                }
            }
        }else {
            if (arr[left] < arr[right - 1]) {
                if (arr[mid] > value) {
                    return has(arr, left,mid, value);
                }else {
                    return has(arr, mid + 1, right, value);
                }
            }else {
                if (arr[mid] < value) {
                    return has(arr, left,mid, value);
                }else {
                    return has(arr, mid + 1, right, value);
                }
            }
        }
    }

    public static void main(String[] args) {

//        int a[] = new int[6];
//
//        a[0] = 5;
//        a[1] = 4;
//        a[2] = 3;
//        a[3] = 9;
//        a[4] = 8;
//        a[5] = 7;
//
//        for(int i = 0; i < 20; ++i) {
//            System.out.println(has(a, 0, 6, i));
//        }

        List<Integer> list = new LinkedList<>();

        list.add(10);

        System.out.println(list.get(0));
    }
}
