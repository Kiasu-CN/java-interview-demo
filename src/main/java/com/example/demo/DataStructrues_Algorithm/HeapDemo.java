package com.example.demo.DataStructrues_Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 手写堆 + Top-K问题 + 堆排序
 * 面试高频：⭐⭐ Top-K问题、堆排序、优先队列原理
 */
public class HeapDemo {

    // ======================== 手写最小堆 ========================
    /**
     * 基于数组的最小堆实现
     * 索引关系：父 i → 左子 2i+1, 右子 2i+2
     *         子 i → 父 (i-1)/2
     */
    static class MinHeap {
        private int[] data;
        private int size;
        private static final int DEFAULT_CAPACITY = 16;

        MinHeap() {
            data = new int[DEFAULT_CAPACITY];
            size = 0;
        }

        /** 从数组建堆（Floyd 建堆法，自底向上）O(n) */
        MinHeap(int[] arr) {
            data = Arrays.copyOf(arr, Math.max(arr.length, DEFAULT_CAPACITY));
            size = arr.length;
            // 从最后一个非叶子节点开始下沉
            for (int i = (size - 2) / 2; i >= 0; i--) {
                siftDown(i);
            }
        }

        /** 插入元素：加到末尾，然后上浮 O(log n) */
        void insert(int val) {
            if (size == data.length) {
                data = Arrays.copyOf(data, data.length * 2);
            }
            data[size] = val;
            siftUp(size);
            size++;
        }

        /** 取出最小值：交换头尾，删除尾部，然后下沉 O(log n) */
        int extractMin() {
            if (size == 0) throw new RuntimeException("堆为空");
            int min = data[0];
            data[0] = data[size - 1];
            size--;
            siftDown(0);
            return min;
        }

        int peek() {
            if (size == 0) throw new RuntimeException("堆为空");
            return data[0];
        }

        int size() { return size; }
        boolean isEmpty() { return size == 0; }

        /** 上浮：和父节点比较，小于则交换 */
        private void siftUp(int index) {
            while (index > 0) {
                int parent = (index - 1) / 2;
                if (data[index] >= data[parent]) break;
                swap(index, parent);
                index = parent;
            }
        }

        /** 下沉：和较小的子节点比较，大于则交换 */
        private void siftDown(int index) {
            while (true) {
                int left = 2 * index + 1;
                int right = 2 * index + 2;
                int smallest = index;
                if (left < size && data[left] < data[smallest]) smallest = left;
                if (right < size && data[right] < data[smallest]) smallest = right;
                if (smallest == index) break;
                swap(index, smallest);
                index = smallest;
            }
        }

        private void swap(int i, int j) {
            int temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }

        /** 查看堆数组（前 size 个元素） */
        int[] toArray() {
            return Arrays.copyOf(data, size);
        }
    }

    // ======================== 算法题方法 ========================

    /**
     * 堆排序 ⭐⭐
     * 思路：建最大堆 → 交换堆顶和末尾 → 缩小堆范围 → 重复
     * 时间 O(n log n)，空间 O(1)，不稳定排序
     */
    static void heapSort(int[] arr) {
        int n = arr.length;
        // 建最大堆（从最后一个非叶子节点开始下沉）
        for (int i = (n - 2) / 2; i >= 0; i--) {
            heapifyDown(arr, i, n);
        }
        // 逐个提取堆顶（最大值）放到末尾
        for (int i = n - 1; i > 0; i--) {
            // 交换堆顶和当前末尾
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            // 对缩小的堆进行下沉
            heapifyDown(arr, 0, i);
        }
    }

    /** 最大堆下沉操作 */
    private static void heapifyDown(int[] arr, int index, int heapSize) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;
            if (left < heapSize && arr[left] > arr[largest]) largest = left;
            if (right < heapSize && arr[right] > arr[largest]) largest = right;
            if (largest == index) break;
            int temp = arr[index];
            arr[index] = arr[largest];
            arr[largest] = temp;
            index = largest;
        }
    }

    /**
     * Top-K 最大的K个数（LeetCode 215）⭐⭐⭐
     * 思路：维护大小为 K 的最小堆，比堆顶大的就替换
     * 时间 O(n log k)，空间 O(k)
     */
    static int[] topKLargest(int[] nums, int k) {
        // 最小堆，堆顶是 K 个数中最小的
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        int[] result = new int[minHeap.size()];
        int i = 0;
        for (int val : minHeap) result[i++] = val;
        return result;
    }

    /**
     * 前K个高频元素（LeetCode 347）⭐⭐
     * 思路：HashMap 计频 + 最小堆按频率排序
     */
    static int[] topKFrequent(int[] nums, int k) {
        // 1. 计频
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.merge(num, 1, Integer::sum);
        }
        // 2. 最小堆（按频率排序）
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
            new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (minHeap.size() < k) {
                minHeap.offer(entry);
            } else if (entry.getValue() > minHeap.peek().getValue()) {
                minHeap.poll();
                minHeap.offer(entry);
            }
        }
        // 3. 提取结果
        int[] result = new int[minHeap.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : minHeap) {
            result[i++] = entry.getKey();
        }
        return result;
    }

    // ======================== 测试 main ========================

    public static void main(String[] args) {
        System.out.println("===== 1. 手写最小堆基本操作 =====");
        MinHeap heap = new MinHeap();
        int[] inserts = {5, 3, 8, 1, 2};
        for (int v : inserts) {
            heap.insert(v);
            System.out.println("insert(" + v + ") 堆顶: " + heap.peek());
        }
        System.out.println("堆数组: " + Arrays.toString(heap.toArray()));

        System.out.println("依次取出(升序):");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " ");
        }
        System.out.println();

        System.out.println("\n===== 2. 建堆（Floyd，自底向上）⭐ =====");
        int[] arr4heap = {7, 3, 9, 1, 5, 2, 8};
        System.out.println("原数组: " + Arrays.toString(arr4heap));
        MinHeap heap2 = new MinHeap(arr4heap);
        System.out.println("建堆后: " + Arrays.toString(heap2.toArray()));
        // 堆顶应该是最小值 1
        System.out.println("堆顶(最小): " + heap2.peek()); // 1

        System.out.println("\n===== 3. 堆排序 =====");
        int[] arr4sort = {7, 3, 9, 1, 5, 2, 8};
        System.out.println("排序前: " + Arrays.toString(arr4sort));
        heapSort(arr4sort);
        System.out.println("堆排序后: " + Arrays.toString(arr4sort));

        System.out.println("\n===== 4. Top-K问题（LeetCode 215）⭐⭐⭐ =====");
        int[] nums4topk = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("数组: " + Arrays.toString(nums4topk));
        System.out.println("前" + k + "大的元素: " + Arrays.toString(topKLargest(nums4topk, k)));

        System.out.println("\n===== 5. 前K个高频元素（LeetCode 347）⭐⭐ =====");
        int[] nums4freq = {1, 1, 1, 2, 2, 3};
        System.out.println("数组: " + Arrays.toString(nums4freq));
        System.out.println("前2个高频: " + Arrays.toString(topKFrequent(nums4freq, 2)));
    }
}
