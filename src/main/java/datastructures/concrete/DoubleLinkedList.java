package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }



    @Override
    public void add(T item) {
        Node<T> temp = new Node<>(item);
        if (size == 0) {
            front = temp;
            back = temp;
        } else {
            this.back.next = temp;
            this.back.next.prev = this.back;
            this.back = back.next;
        }
        this.size++;
    }

    @Override
    public T remove() {
        if (front == null){
            throw new EmptyContainerException();
        }
        T output = back.data;
        if (back.prev == null) {
            front = null;
            back = null;
        } else {
            back = back.prev;
            back.next.prev = null;
            back.next = null;
        }
        this.size--;
        return output;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        if (index <= size/2) {
            Node<T> temp = front;
            for (int i = 0; i<index; i++){
                temp = temp.next;
            }
            return temp.data;
        } else {
            Node<T> backward = back;
            for (int i = size-1; i > index; i--) {
                backward = backward.prev;
            }
            return backward.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = new Node<>(item);
        if (index == 0) {
            if (size > 1) {
                temp.next = front.next;
                front.next.prev = temp;
            }
            front = temp;
            if (size == 1) {
                back =temp;
            }
        } else if (index == size - 1) {
            temp.prev = back.prev;
            back.prev.next = temp;
            back = temp;
        } else {
            Node<T> run = front;
            for (int i = 0; i < index - 1; i++) {
                run = run.next;
            }
            temp.next = run.next.next;
            run.next.next.prev = temp;
            run.next = temp;
            temp.prev = run;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = new Node<>(null, item, null);
        if (size == 0) {
            front = temp;
            back = temp;
        } else if (index == size) {
            back.next = temp;
            temp.prev = back;
            back = back.next;
        } else if (index == 0) {
            temp.next = front;
            front.prev = temp;
            front = temp;
        } else if (index  <= size / 2){
            Node<T> run = front;
            for (int i = 0; i < index - 1; i++) {
                run = run.next;
            }
            temp.next = run.next;
            run.next.prev = temp;
            run.next = temp;
            temp.prev = run;
        } else {
            Node<T> run = back;
            for (int i = size; i > index; i--) {
                run = run.prev;
            }
            run.next.prev = temp;
            temp.next = run.next;
            run.next = temp;
            temp.prev = run;
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T output;
        if (size == 1) {
            output = front.data;
            front = null;
            back = null;
        } else if (index == size -1) {
            output = back.data;
            back = back.prev;
            back.next.prev = null;
            back.next = null;
        }else if (index == 0){
            output = front.data;
            front = front.next;
            front.prev.next = null;
            front.prev = null;
        } else if (index <= size/2) {
            Node<T> temp = front;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            output = temp.next.data;
            temp.next = temp.next.next;
            temp.next.prev = temp;
        }
        else {
            Node<T> temp = back;
            for (int i = size -1; i > index; i--) {
                temp = temp.prev;
            }
            output = temp.data;
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
        }
        size--;
        return output;
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(current.data, item)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> forward = front;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(forward.data, other)) {
                return true;
            }
            forward = forward.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T output = current.data;
            current = current.next;
            return output;
        }
    }
}
