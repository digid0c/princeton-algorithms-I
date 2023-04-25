import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {

        private Item item;
        private Node next;
        private Node previous;

        private Node(Item item) {
            this.item = item;
        }
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("It is not possible to add null items.");
        }

        Node oldFirst = first;
        first = new Node(item);
        first.next = oldFirst;
        size++;

        if (oldFirst != null) {
            oldFirst.previous = first;
        } else {
            last = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("It is not possible to add null items.");
        }

        Node oldLast = last;
        last = new Node(item);
        last.previous = oldLast;
        size++;

        if (oldLast != null) {
            oldLast.next = last;
        } else {
            first = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("It is not possible to remove items from empty deque.");
        }

        Node oldFirst = first;
        first = first.next;
        size--;

        if (first != null) {
            first.previous = null;
        } else {
            last = null;
        }
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("It is not possible to remove items from empty deque.");
        }

        Node oldLast = last;
        last = last.previous;
        size--;

        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate!");
            }

            Node node = current;
            current = current.next;
            return node.item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);

        System.out.println("Should print 1 to 5:");
        Iterator<Integer> oneFiveIterator = deque.iterator();
        while (oneFiveIterator.hasNext()) {
            System.out.println(oneFiveIterator.next());
        }
        System.out.println("Size: " + deque.size());

        deque.removeFirst();
        deque.removeLast();

        System.out.println("Should print 2 to 4:");
        Iterator<Integer> twoFourIterator = deque.iterator();
        while (twoFourIterator.hasNext()) {
            System.out.println(twoFourIterator.next());
        }
        System.out.println("Size: " + deque.size());
    }

}
