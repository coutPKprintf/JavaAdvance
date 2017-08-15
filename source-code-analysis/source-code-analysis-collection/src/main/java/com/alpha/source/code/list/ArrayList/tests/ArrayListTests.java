package com.alpha.source.code.list.ArrayList.tests;
import org.joor.Reflect;
import org.junit.Test;
import org.junit.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenwen on 17/8/10. ArrayList 测试用例类
 */
public class ArrayListTests {

    /**
     * 不同的构造器或不同参数的扩容方式
     */
    @Test
    public void arrayListInitialCapacityTest(){
        //当 initialCapacity = 0, 扩容方式
        ArrayList<Integer> list = new ArrayList<Integer>(0);

        list.add(10);

        Assert.assertEquals(1, getCapacity(list));


        // 当 initialCapacity > 0, 扩容方式
        list = new ArrayList<Integer>(200);

        Assert.assertEquals(200,  getCapacity(list));


        //当 使用默认构造器时, 扩容方式
        list = new ArrayList<Integer>();

        list.add(10);

        Assert.assertEquals(10, getCapacity(list));
    }

    /**
     * ArrayList(Collection<? extends E> c) 构造器, 构造方式
     */
    @Test
    public void arrayListConstructCollectionTest(){
        Data data = new Data("陈文", "你好");

        ArrayList<Data> datas = new ArrayList<Data>(10);
        datas.add(data);


        ArrayList<Data> datasCopy = new ArrayList<Data>(datas);
        Data dataCopy = datasCopy.get(0);


        // change data
        data.setValue("好帅");

        // 列表中的对象引用没有变化
        Assert.assertEquals(true, data.equals(dataCopy));
        Assert.assertEquals(true, data == dataCopy);

        // 容量 变为 size
        Assert.assertEquals(10, getCapacity(datas));
        Assert.assertEquals(datas.size(), getCapacity(datasCopy));

        // 引用变化
        Assert.assertEquals(false , datas == datasCopy);

        // 数据没变, 容量变化
        Assert.assertEquals(true , datas.equals(datasCopy));
    }

    /**
     * 1. 不能在foreach或for中修改ArrayList数据 .
     *
     * 2. 如果需要遍历所有数据, 而且需要调用ArrayList的remove或add, 使用迭代器
     *
     * 3. 删除元素, 并不会回收数据的capacity
     *
     * 4. trimToSize能够使capacity = size
     */
    @Test
    public void arrayListForEachTest(){
        final ArrayList<Integer> arrayList = new ArrayList<>(10);

        int i = 0;

        for(; i < 10; ++i){
            arrayList.add(i);
        }

        // 不能在foreach中修改ArrayList数据
        boolean expected = false;
        try {
            for (Integer value : arrayList) {
                arrayList.remove(value);
            }
        }catch (ConcurrentModificationException e){
            expected = true;
        }

        Assert.assertEquals(true, expected);

        // 如果需要遍历所有数据, 而且需要修改ArrayList的remove或add
        expected = true;

        Iterator<Integer> iterator = arrayList.iterator();

        try {
            while (iterator.hasNext()){
                Integer next = iterator.next();
                iterator.remove();
            }
        }catch (ConcurrentModificationException e){
            expected = false;
        }

        Assert.assertEquals(true, expected);


        // 删除元素, 并不会回收数据的capacity
        Assert.assertEquals(10 , getCapacity(arrayList));


        // trimToSize能够使capacity = size
        arrayList.trimToSize();
        Assert.assertEquals(0, getCapacity(arrayList));
    }


    /**
     * 调用subList方法新生成的List相当于原始List的视图, 当对新生成的List操作时, 原始List也会受到影响
     */
    @Test
    public void arrayListSubListTest(){
        final ArrayList<Integer> arrayList = new ArrayList<>(10);

        int i = 0;

        for(; i < 10; ++i){
            arrayList.add(i);
        }

        List<Integer> subList = arrayList.subList(2, 5);

        Assert.assertEquals(10, arrayList.size());

        Assert.assertEquals(3, subList.size());


        // 调用subList方法新生成的List相当于原始List的视图, 当对新生成的List操作时, 原始List也会受到影响
        subList.remove(0);

        Assert.assertEquals(9, arrayList.size());

        Assert.assertEquals(2, subList.size());
    }


    public static class Data{

        private String name;

        private String value;

        public Data(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;

            Data data = (Data) o;

            if (!getName().equals(data.getName())) return false;
            return getValue().equals(data.getValue());

        }

        @Override
        public int hashCode() {
            int result = getName().hashCode();
            result = 31 * result + getValue().hashCode();
            return result;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    private int getCapacity(List list){
        return ((Object[])Reflect.on(list).field("elementData").get()).length;
    }
}
