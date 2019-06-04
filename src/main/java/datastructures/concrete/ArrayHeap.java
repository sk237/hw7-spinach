package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    private IDictionary<T, Integer> indices;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(256);
        this.size = 0;
        this.indices = new ChainedHashDictionary<>();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        int parent = (index - 1) / NUM_CHILDREN;
        if (index != parent && heap[parent].compareTo(heap[index]) > 0) {
            this.swap(index, parent);
            this.percolateUp(parent);

        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        int child = NUM_CHILDREN*index;
        int minIndex = index;
        for (int i = 1; i <= NUM_CHILDREN && child + i < size; i++) {
            if (heap[minIndex].compareTo(heap[child + i]) > 0) {
                minIndex = child + i;
            }
        }
        if (index != minIndex) {
            this.swap(index, minIndex);
            this.percolateDown(minIndex);
        }
    }
    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */

    private void percolate(int index) {
        percolateUp(index);
        percolateDown(index);
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        indices.put(heap[a], b);
        indices.put(heap[b], a);
        T temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T output = heap[0];
        heap[0] = heap[--size];
        percolateDown(0);
        indices.remove(output);
        return output;
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (indices.containsKey(item)) {
            throw new InvalidElementException();
        }
        if (heap.length == size) {
            T[] temp = makeArrayOfT(size * 4);
            System.arraycopy(heap, 0, temp, 0, size);
            heap = temp;
        }
        heap[size] = item;
        indices.put(item, size);
        percolateUp(size);
        size++;
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(item)) {
            throw new InvalidElementException();

        }
        int index = indices.get(item);
        swap(index, --size);
        indices.remove(item);
        percolate(index);
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (contains(newItem) || !contains(oldItem)) {
            throw new InvalidElementException();
        }
        heap[indices.get(oldItem)] = newItem;
        indices.put(newItem, indices.remove(oldItem));
        percolate(indices.get(newItem));

    }


    @Override
    public int size() {
        return size;
    }
}
