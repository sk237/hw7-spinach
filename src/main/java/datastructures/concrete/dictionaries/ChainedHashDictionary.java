package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private final double lambda;
    private int size;
    // You MUST use this field to store the contents of your dictionary.
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this(3.5);
    }

    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.size = 0;
        this.chains = makeArrayOfChains(10000);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */

    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V get(K key) {
        int index = index(key, chains.length);
        if (chains[index] == null) {
            throw   new NoSuchKeyException();
        }
        return chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {

        //resizing if greater than lambda
        if (needResize()){
            int prevSize = chains.length;
            IDictionary<K, V>[] newChains = makeArrayOfChains(prevSize*2);

            for (KVPair<K, V> pair : this) {
                int newIndex = index(pair.getKey(), newChains.length);
                if (newChains[newIndex] == null) {
                    newChains[newIndex] = new ArrayDictionary<>();
                }
                newChains[newIndex].put(pair.getKey(), pair.getValue());
            }
            chains = newChains;
        }

        int index = index(key, chains.length);
        if (chains[index] == null){
            chains[index] = new ArrayDictionary<>();
        }
        else if (chains[index].containsKey(key)){
            size--;
        }
        chains[index].put(key, value);
        size++;
    }

    @Override
    public V remove(K key) {
        int index = index(key, chains.length);
        if (chains[index] == null) {
            throw new NoSuchKeyException();
        }
        size--;
        return chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int index = index(key, chains.length);
        if (chains[index] == null) {
            return false;
        }
        return chains[index].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains, this.size);
    }

    //recommended section
    @Override
    public V getOrDefault(K key, V defaultValue){
        int index = index(key, chains.length);
        if (chains[index] == null){
            return defaultValue;
        }
        return chains[index].getOrDefault(key, defaultValue);
    }

    private int index(K key, int chainsLen) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % chainsLen;
    }

    private boolean needResize(){
        return (((double) size / (double) chains.length) > lambda);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> itr;
        private int index;
        private int size;

        public ChainedIterator(IDictionary<K, V>[] chains, int size) {
            this.chains = chains;
            this.index = 0;
            this.size = size;
            if (size != 0) {
                while (index < chains.length && chains[index] == null) {
                    index++;
                }

                itr = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (size == 0) {
                return false;
            }
            if (!itr.hasNext()) {
                index++;
                while (chains[index] == null || chains[index].isEmpty()) {
                    index++;
                }
                itr = chains[index].iterator();
            }
            return true;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            size--;
            return itr.next();

        }
    }
}
