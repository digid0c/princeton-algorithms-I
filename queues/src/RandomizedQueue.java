import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    private Item[] queue;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        // initial capacity value is taken from java.util.ArrayList implementation
        queue = (Item[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("It is not possible to add null items.");
        }
        if (n == queue.length) {
            resize(queue.length * 2);
        }
        queue[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("It is not possible to retrieve items from empty queue.");
        }

        int i = StdRandom.uniformInt(n);
        Item item = queue[i];
        queue[i] = queue[--n];
        queue[n] = null;

        if (n > 0 && n == queue.length / 4) {
            resize(queue.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("It is not possible to retrieve items from empty queue.");
        }
        return queue[StdRandom.uniformInt(n)];
    }

    private void resize(int newSize) {
        Item[] queueCopy = (Item[]) new Object[newSize];
        for (int i = 0; i < n; i++) {
            queueCopy[i] = queue[i];
        }
        queue = queueCopy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] queueCopy;
        private int m;

        private RandomizedQueueIterator() {
            queueCopy = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                queueCopy[i] = queue[i];
            }
            StdRandom.shuffle(queueCopy);
        }

        @Override
        public boolean hasNext() {
            return m < n;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate!");
            }
            return queueCopy[m++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);

        System.out.println("Should print 5 elements in random order:");
        Iterator<Integer> fiveElementsIterator = queue.iterator();
        while (fiveElementsIterator.hasNext()) {
            System.out.println(fiveElementsIterator.next());
        }
        System.out.println("Size: " + queue.size());

        System.out.println("Removing: " + queue.dequeue());
        System.out.println("Removing: " + queue.dequeue());
        System.out.println("Sample: " + queue.sample());
        System.out.println("Sample: " + queue.sample());

        System.out.println("Should print 3 elements in random order:");
        Iterator<Integer> threeElementsIterator = queue.iterator();
        while (threeElementsIterator.hasNext()) {
            System.out.println(threeElementsIterator.next());
        }
        System.out.println("Size: " + queue.size());
    }
}
