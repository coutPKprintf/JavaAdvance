# ArrayList

### 1. 类功能说明
<pre>

ArrayList是一个基于数组实现的自动扩容的非线程安全列表, 其包含该以下三个构造函数:

</pre>
<pre>
   1. ArrayList(int initialCapacity):
        创建指定初始化大小的列表, elementData是真正存储数据的数组, elementData.length &gt= size,
         也就是说会提前分配一定大小的内存给elementData, 以便于后面每次插入新数据时, 需要重新扩充内存.
        当 initialCapacity &lt 0 时, 将发生IllegalArgumentException异常.
        当 initialCapacity = 0 时, elementData将赋值为EMPTY_ELEMENTDATA空数组.
        当 initialCapacity &gt 0 时, elementData将会创建一个大小为initialCapacity的数组
</pre>

<pre>
   2. ArrayList():
        默认构造器, elementData将赋值为DEFAULTCAPACITY_EMPTY_ELEMENTDATA默认空数组.
</pre>

<pre>
   3. ArrayList(Collection&lt? extends E&gt c):
        新生成的列表只会拷贝c集合的数据部分, 也就是说通过这种方式构造完之后, 新生成的list的capacity = c.size() &lt= c 的 capacity.
        集合中的数据不会深度拷贝, 只是拷贝原来数据的引用, 也就是说改变c里面某个引用元素的某个属性值, 新生成的list中对应对象的属性值也会变化.
        详情见 测试用例(arrayListConstructCollectionTest)
</pre>

<pre>
注: 你可能会问为什么会出现两个空数据EMPTY_ELEMENTDATA 和 DEFAULTCAPACITY_EMPTY_ELEMENTDATA?

因为elementData为DEFAULTCAPACITY_EMPTY_ELEMENTDATA时, 如果往列表中添加数据, 则elementData初始至少会分配DEFAULT_CAPACITY=10个容量,
并且只有使用ArrayList()构造器, 才会以此方式分配内存.

区别:

当使用new ArrayList(0)创建对象, 添加一个数据时, 其分配的 capacity = 1 .

当使用new ArrayList()创建对象, 添加一个数据时, 其分配的 capacity = DEFAULT_CAPACITY = 10 .

详情见 测试用例(arrayListInitialCapacityTest)
</pre>

### 2. 类UML图

![alt text](ArrayList.png "ArrayList UML")

### 3. 时间复杂度以及空间复杂度分析
<pre>

3.1. 时间复杂度
   3.1.1 get(int index), set(int index, E element), add(E e) 等操作的时间复杂度为 O(1).
    由于ArrayList是基于数组结构, 所以通过下标查询相当数组的下标定位, 而添加新元素是添加到数组的末尾, 
    自然时间复杂度为 O(1).
   3.1.2 add(int index, E element), remove(int index), remove(Object o) 等时间复杂度为 O(n). 
   由于ArrayList是基于数组结构, 所以如果往中间插入或删除数据的话, 如要将插入位置之后的所有数据前移或后移.

3.2. 空间复杂度
   由于是数组结构存储数据, 所以空间复杂度为 O(n), 不过由于ArrayList每次扩容, 都是以 n + n/2 的方式,
    所以空间复杂度可能达到 O(n + n/2). 详情见 源码中 grow(int minCapacity) 这个函数.

</pre>

### 4. 算法分析及关键方法分析

#### 4.1 算法分析
<pre>
	ArrayList是一个基于数据实现无线扩容的列表(最大长度Integer.MAX_VALUE - 8), 在每次添加新元素时，
	为了确保元素个数不超过数组大小，当元素个数size > capacity时， ArrayList会以 n + n/2 的增加容量capacity。
	当每次删除元素时，为了确保元素在数组中的连续性， ArrayList将会把被删除元素之后的所有元素前移一位。
	当通过下标获取元素时，ArrayList通过数组的下标直接定位到指定元素。
</pre>

#### 4.2 关键方法分析

##### 4.2.1 获取子列表

```java
public List<E> subList(int fromIndex, int toIndex)
```

4.2.1.1 方法说明
<pre>
	调用subList方法新生成的List相当于原始List的视图, 当对新生成的List操作时, 原始List也会受到影响.
	详情见测试用例arrayListSubListTest
</pre>

4.2.1.2 源码分析
```java
public List<E> subList(int fromIndex, int toIndex) {
    //检测数组索引是否越界，从fromIndex下标开始， 不包括toIndex下标
    subListRangeCheck(fromIndex, toIndex, size);
    //新生成子列表并非ArrayList对象，而是内部类SubList的对象
    return new SubList(this, 0, fromIndex, toIndex);
}

//此类并不完整，只包含源码中部分函数
private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            //父列表
            this.parent = parent;
            //父列表中开始位置偏移
            this.parentOffset = fromIndex;
            //数组elementData开始位置偏移
            this.offset = offset + fromIndex;
            //数据大小
            this.size = toIndex - fromIndex;
            //修改次数，此字段是ArrayList记录数据是否有修改操作，例如remove，add等
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }
}
```


##### 4.2.2 动态扩容
```java
private void grow(int minCapacity)
```
4.2.2.1 方法说明
<pre>
	动态扩容是ArrayList相对于传统数组的优势，但是我们必须要了解ArrayList的扩容方式，
	这样才能使我们更好的使用ArrayList，更充分的利用内存。ArrayList每次扩容都是一次耗时操作，
	并且会浪费一部分引用空间，所以如果我们在使用ArrayList时，能够估计大概需要的容量大小，
	尽量通过指定大小的方式创建ArrayList，既省空间又省时间。
</pre>

4.2.2.2 源码分析
```java
//暴露外部调用接口，确保列表的最小容量为minCapacity，如果为默认构造器创建的List，则初始时最小容量为DEFAULT_CAPACITY=10
public void ensureCapacity(int minCapacity) {
    int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
        // any size if not default element table
        ? 0
        // larger than default for default empty table. It's already
        // supposed to be at default size.
        : DEFAULT_CAPACITY;

    if (minCapacity > minExpand) {
        ensureExplicitCapacity(minCapacity);
    }
}

//当添加一个新元素时，会调用ensureCapacityInternal(size+1)方法，确保容量大于等于minCapacity，
//再次校验如果为默认构造器创建的List，则初始时最小容量为DEFAULT_CAPACITY=10
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

//只有minCapacity大于当前capacity时，才进行扩容
private void ensureExplicitCapacity(int minCapacity) {
	//修改次数，通过此参数确认列表再进行某个操作过程中，是否被修改过，如果被修改过，将抛出ConcurrentModificationException异常。
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

//真正扩容方法，保证最小容量minCapacity，新容量的计算方式为 oldCapacity + (oldCapacity >> 1) ， 其中oldCapacity为当前capacity大小
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```
### 5. 适应场景
<pre>
	5.1 适应于查询次数远远大于修改次数的场景。
	5.2 适应于元素大小不确定的场景。
	5.3 适应于随机访问,通过下标定位元素的场景。
</pre>

### 6. 同样功能的类对比 或 不同版本实现对比


### 7. 是否有优化方案? 方案的实现.


### 8. 基本测试用例
见[ArrayList测试用例](https://github.com/coutPKprintf/JavaAdvance/blob/master/source-code-analysis/source-code-analysis-collection/src/main/java/com/alpha/source/code/list/ArrayList/tests/ArrayListTests.java)