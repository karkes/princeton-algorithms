/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 24/04/2020 DD/MM/YYYY
 *  Description: Randomized Queue API
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int front;
    private int rear;
    // private boolean resize;

    // construct an empty randomized queue

    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        front = -1;
        rear = -1;
        // resize = true;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return front == -1;
    }

    // return the number of items on the randomized queue
    public int size() {
        return rear + 1;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("wrong argumemt");
        if (isEmpty())
            front = 0;
        rear++;
        if (q.length == rear) {
            resize(2 * q.length);
            rear = q.length / 2;
        }
        q[rear] = item;
        // size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("no element");
        int randomIdx = StdRandom.uniform(0, rear + 1);
        Item item = q[randomIdx];
        q[randomIdx] = q[rear];
        q[rear] = null;
        rear--;
        if (rear > 2 && rear < q.length / 4) {
            resize(q.length / 2);
        }
        front = rear == -1 ? -1 : front;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("no element");
        int randomIdx = StdRandom.uniform(0, rear + 1);
        return q[randomIdx];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int idx;
        private Item[] randQ;

        public RandomQueueIterator() {
            idx = rear;
            int n = size();
            randQ = (Item[]) new Object[n];
            fillData();
        }

        public void fillData() {
            for (int i = front; i != -1 && i <= rear; i++) {
                randQ[i] = q[i];
            }
        }

        public boolean hasNext() {
            return idx != -1;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no element");
            int i = StdRandom.uniform(0, idx + 1);
            Item itm = randQ[i];
            randQ[i] = randQ[idx];
            randQ[idx] = null;
            idx--;
            return itm;
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int k = 0;
        while (k < q.length && q[k] != null) {
            copy[k] = q[k];
            k++;
        }
        q = copy;
        // System.out.println("Size of Q after resize : " + q.length);
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("Hello there, from RandomizedQueue");

        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        System.out.println("Empty : " + rq.isEmpty());
        System.out.println("Size : " + rq.size());
        System.out.println("front : " + rq.front + " rear : " + rq.rear);
        for (int i = 1; i <= 1; i++) {
            rq.enqueue(i);
        }
        System.out.println("Empty : " + rq.isEmpty());
        System.out.println("Size : " + rq.size());
        System.out.println("front : " + rq.front + " rear : " + rq.rear);
        /* System.out.println("Dequeueing .. ");
        for (int i = 1; i <= 2; i++) {
            System.out.print(rq.dequeue() + "; ");
        }
        System.out.println();
        System.out.println("Empty : " + rq.isEmpty());
        System.out.println("Size : " + rq.size());
        System.out.println("front : " + rq.front + " rear : " + rq.rear);
        System.out.println("Reamining elements in queue .. ");
        for (Object v : (Object[]) rq.q) {
            System.out.print(v + "->");
        } */
        // Iterator<Integer> itr = rq.iterator();

        for (Integer integer : rq) {
            System.out.print(integer + ", ");
        }

    }
}
