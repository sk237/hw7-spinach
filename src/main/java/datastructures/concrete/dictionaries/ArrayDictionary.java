package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     * <p>
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(pairs[i].key, key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            if (Objects.equals(pairs[left].key, key)) {
                pairs[left].value = value;
                return;
            } else if (Objects.equals(pairs[right].key, key)) {
                pairs[right].value = value;
                return;
            }
            left++;
            right--;
        }
        Pair<K, V> newPair = new Pair<>(key, value);
        if (size == pairs.length) {
            Pair<K, V>[] temp = makeArrayOfPairs(size*2);
            System.arraycopy(pairs, 0, temp, 0, pairs.length);
            pairs = temp;
        }
        pairs[size] = newPair;
        size++;
    }

    @Override
    public V remove(K key) {
        V output;
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            if (Objects.equals(pairs[left].key, key)) {
                output = pairs[left].value;
                pairs[left] = pairs[size - 1];
                size--;
                return output;
            } else if (Objects.equals(pairs[right].key, key)) {
                output = pairs[right].value;
                pairs[right] = pairs[size - 1];
                size--;
                return output;
            }
            left++;
            right--;
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            if (Objects.equals(pairs[left].key, key)) {
                return true;
            } else if (Objects.equals(pairs[right].key, key)) {
                return true;
            }
            left++;
            right--;
        }
        return false;
    }

    //recommended part
    @Override
    public V getOrDefault(K key, V defaultValue){
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            if (Objects.equals(pairs[left].key, key)) {
                return pairs[left].value;
            } else if (Objects.equals(pairs[right].key, key)) {
                return pairs[right].value;
            }
            left++;
            right--;
        }
        return defaultValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {

        private Pair<K, V>[] pairs;
        private int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.size = size;
            this.pairs = pairs;

        }

        @Override
        public boolean hasNext() {

            return size !=0;
        }

        @Override
        public KVPair<K, V> next() {
            if (size == 0) {
                throw new NoSuchElementException();
            }
            size--;
            return new KVPair<>(pairs[size].key, pairs[size].value);
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
