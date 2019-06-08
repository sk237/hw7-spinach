package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;


/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> indices;
    private IDictionary<Integer, T> items;
    // However, feel free to add more fields and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        indices = new ChainedHashDictionary<>();
        items = new ChainedHashDictionary<>();
        pointers = new int[100];
    }

    @Override
    public void makeSet(T item) {
        if (indices.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        if (pointers.length == indices.size() + 1) {
            int[] temp = new int[indices.size()*2];
            System.arraycopy(pointers, 0, temp, 0, pointers.length);
            pointers = temp;
        }
        items.put(indices.size(), item);
        indices.put(item, indices.size());
        pointers[indices.size() - 1] = -1;
    }

    @Override
    public int findSet(T item) {
        if (!indices.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = indices.get(item);
        if (pointers[index] < 0) {
            return index;
        }
        pointers[index] = findSet(items.get(pointers[index]));
        return pointers[index];
    }

    @Override
    public void union(T item1, T item2) {
        if (!indices.containsKey(item1) || !indices.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int item1Parent = findSet(item1);
        int item2Parent = findSet(item2);
        if (item1Parent != item2Parent) {
            if (pointers[item1Parent] == pointers[item2Parent]) {
                pointers[item1Parent] = item2Parent;
                pointers[item2Parent]--;
            } else if (pointers[item1Parent] < pointers[item2Parent]) {
                pointers[item2Parent] = item1Parent;
            } else {
                pointers[item1Parent] = item2Parent;
            }
        }
    }
}
