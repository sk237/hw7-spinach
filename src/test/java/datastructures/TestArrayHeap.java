package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import misc.exceptions.EmptyContainerException;
import static org.junit.Assert.fail;

import static org.junit.Assert.assertFalse;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }
    @Test(timeout=SECOND)
    public void testPerclolate() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(2);
        heap.add(4);
        heap.add(8);
        heap.add(7);
        heap.add(9);
        heap.add(10);
        heap.add(11);
        heap.add(12);
        heap.add(13);
        heap.add(1);


        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(4, heap.removeMin());
        assertEquals(7, heap.removeMin());
        assertFalse(heap.isEmpty());
    }
    @Test(timeout=SECOND)
    public void testBasicRemoveMinDecreaseSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        heap.add(1);
        heap.add(2);
        heap.add(5);
        assertEquals(1, heap.removeMin());
        assertEquals(3, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        heap.add(1);
        heap.add(2);
        heap.add(5);
        heap.add(0);
        assertEquals(5, heap.size());
        assertEquals(0, heap.peekMin());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicAddStructureAndSize() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.add("c");
        heap.add("z");
        heap.add("x");
        heap.add("a");
        heap.add("b");
        Comparable<String>[] array = getArray(heap);
        assertEquals(5, heap.size());
        assertEquals("a", heap.peekMin());
        assertEquals("a", array[0]);
        assertEquals("z", array[1]);
        assertEquals("x", array[2]);
        assertEquals("c", array[3]);
        assertEquals("b", array[4]);
        assertFalse(heap.isEmpty());
    }
    @Test(timeout=SECOND)
    public void testBasicAddStructure() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(1);
        heap.add(1000);
        heap.add(500);
        heap.add(700);
        heap.add(1200);
        heap.add(1100);
        heap.add(1300);

        Comparable<Integer>[] array = getArray(heap);
        assertEquals(7, heap.size());
        assertEquals(1, heap.peekMin());
        assertEquals(1, array[0]);
        assertEquals(1000, array[1]);
        assertEquals(500, array[2]);
        assertEquals(700, array[3]);
        assertEquals(1200, array[4]);
        assertEquals(1100, array[5]);
        assertEquals(1300, array[6]);


        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicRemoveStructureAndSize() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.add("c");
        heap.add("z");
        heap.add("x");
        heap.add("a");
        heap.add("b");
        Comparable<String>[] array = getArray(heap);
        assertEquals(5, heap.size());
        heap.remove("a");
        assertEquals("b", heap.peekMin());
        assertEquals("b", array[0]);
        assertEquals("z", array[1]);
        assertEquals("x", array[2]);
        assertEquals("c", array[3]);
        heap.remove("c");

        assertEquals("b", heap.peekMin());
        assertEquals("b", array[0]);
        assertEquals("z", array[1]);
        assertEquals("x", array[2]);

        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testAddSizeMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<10000; i++){
            heap.add(i);
        }
        assertEquals(10000, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testRemoveMinMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<10000; i++){
            heap.add(i);
        }
        assertEquals(10000, heap.size());
        Integer temp;
        for (int i = 0; i<9999; i++){
            temp = heap.removeMin();
            assertFalse(temp.compareTo(heap.peekMin())>0);
        }
        assertEquals(9999, heap.peekMin());
        assertFalse(heap.isEmpty());
    }


    @Test(timeout=SECOND)
    public void testPeekMinEmptyContainerException() {
        IPriorityQueue<String> heap = this.makeInstance();
        try{
            heap.peekMin();
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBound");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveMinEmptyContainerException() {
        IPriorityQueue<String> heap = this.makeInstance();
        try{
            heap.removeMin();
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBound");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testBasicContains() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i);
        }
        assertEquals(true, heap.contains(35));
        assertFalse(heap.contains(100));
    }

    @Test(timeout=SECOND)
    public void testContainsNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.contains(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testBasicRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i);
        }
        heap.remove(35);
        heap.remove(2);
        heap.remove(10);
        assertFalse(heap.contains(35));
        assertFalse(heap.contains(2));
        assertFalse(heap.contains(10));
    }
    @Test (timeout = SECOND)
    public void testRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(0);
        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);
        heap.add(6);
        heap.add(7);
        heap.add(8);
        heap.add(100);

        heap.remove(0);
        assertEquals(1, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testAddRemoveMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<1000; i++){
            Integer value = i;
            heap.add(value);
            heap.remove(value);
        }

        assertEquals(true, heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testRemoveNullException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try{
            heap.remove(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveNonExistingException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i);
        }
        try{
            heap.remove(51);
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testBasicReplace() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i+50);
        }
        heap.replace(50, 0);
        assertEquals(0, heap.peekMin());
    }

    @Test(timeout=SECOND)
    public void testBasicReplaceMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i+50);
        }
        for (int j = 0; j<50; j++){
            heap.replace(j+50, j);
        }
        Comparable<Integer>[] array = getArray(heap);

        for (int i = 0; i<50; i++){
            assertEquals(i, array[i]);
        }

    }

    @Test(timeout=SECOND)
    public void testReplaceDuplicateException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i);
        }
        try{
            heap.replace(51, 52);
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        try{
            heap.replace(52, 30);
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        try{
            heap.replace(45, 30);
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

    }

    @Test(timeout=SECOND)
    public void testReplaceNonExistingException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i<50; i++){
            heap.add(i+50);
        }
        try{
            heap.replace(0, 100);
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testAddNullThrowException() {
        IPriorityQueue<String> heap = this.makeInstance();
        try{
            heap.add(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testAddDuplicatesThrowException() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.add("a");
        try{
            heap.add("a");
            // We didn't throw an exception? Fail now.
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }
    }


    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

}
