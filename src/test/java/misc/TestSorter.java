package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.jsoup.helper.Validate.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testSmallInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);

        IList<Integer> top = Sorter.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(1, top.get(0));
    }

    @Test(timeout=SECOND)
    public void testKZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);

        IList<Integer> top = Sorter.topKSort(0, list);
        assertEquals(0, top.size());

    }

    @Test(timeout=SECOND)
    public void testBoundaryCaseK() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(5);
        list.add(3);
        list.add(7);
        list.add(8);
        list.add(11);
        list.add(2);
        list.add(1);
        list.add(51);

        IList<Integer> top = Sorter.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(51, top.get(0));

        IList<Integer> top2 = Sorter.topKSort(8, list);
        assertEquals(1, top2.get(0));
        assertEquals(2, top2.get(1));
        assertEquals(3, top2.get(2));
        assertEquals(5, top2.get(3));
        assertEquals(7, top2.get(4));
        assertEquals(8, top2.get(5));
        assertEquals(11, top2.get(6));
        assertEquals(51, top2.get(7));

    }

    @Test(timeout=SECOND)
    public void testBasicStructure() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(5);
        list.add(3);
        list.add(7);
        list.add(8);
        list.add(11);
        list.add(2);
        list.add(1);
        list.add(51);

        IList<Integer> top = Sorter.topKSort(3, list);
        assertEquals(3, top.size());
        assertEquals(8, top.get(0));
        assertEquals(11, top.get(1));
        assertEquals(51, top.get(2));

    }
    @Test(timeout=SECOND)
    public void testKBiggerThanSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(5);
        list.add(3);
        list.add(7);
        list.add(1);
        list.add(1100);


        IList<Integer> top = Sorter.topKSort(500, list);
        assertEquals(5, top.size());
        assertEquals(1, top.get(0));
        assertEquals(3, top.get(1));
        assertEquals(5, top.get(2));
        assertEquals(7, top.get(3));
        assertEquals(1100, top.get(4));

    }





    @Test(timeout=SECOND)
    public void stressTest() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(1000, list);
        assertEquals(1000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(100000 - 1000 + i, top.get(i));
        }
        IList<Integer> top1 = Sorter.topKSort(10000, list);
        assertEquals(10000, top1.size());
        for (int i = 0; i < top1.size(); i++) {
            assertEquals(100000 - 10000 + i, top1.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testExceptionCase() {
        IList<Integer> list2 = null;
        IList<Integer> list3 = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list3.add(i);
        }


        try{
            IList<Integer> top2 = Sorter.topKSort(5, list2);

            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        try{
            IList<Integer> top3 = Sorter.topKSort(-1, list3);

            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }

    }

}
