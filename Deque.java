/* *****************************************************************************
 *  Name: Kartik Kesarwani
 *  Date: 24/04/2020 DD/MM/YYYY
 *  Description: Deque DS API
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item data;
        private Node next;
        private Node prev;
    }

    private int size;
    private Node head;
    private Node tail;
    // private Node itr;

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        handleWrongArgument(item);
        Node node = new Node();
        node.data = item;
        if (isEmpty()) {
            head = node;
            tail = head;
        }
        else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        handleWrongArgument(item);
        Node node = new Node();
        node.data = item;
        if (isEmpty()) {
            head = node;
            tail = head;
        }
        else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            handleNoSuchElement();
        Node tmp = head;
        Item item = tmp.data;
        if (size == 1) {
            head = null;
            tail = null;
            // tmp = null;
        }
        else {
            head = tmp.next;
            head.prev = null;
            tmp.next = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            handleNoSuchElement();
        Node tmp = tail;
        Item item = tmp.data;
        if (size() == 1) {
            head = null;
            tail = null;
            // tmp = null;
        }
        else {
            tail = tmp.prev;
            tail.next = null;
            tmp.prev = null;
        }
        size--;
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node itr;

        public DequeIterator() {
            itr = head;
        }

        public boolean hasNext() {
            if (itr == null)
                return false;
            return true;
        }

        public Item next() {
            Item itm = null;
            if (!hasNext())
                handleNoSuchElement();
            else {
                itm = itr.data;
                itr = itr.next;
            }
            return itm;
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }
    }

    private void handleWrongArgument(Item item) {
        if (item == null)
            throw new IllegalArgumentException("wrong argument");
    }

    private void handleNoSuchElement() {
        throw new NoSuchElementException("no element");
    }

    // unit testing (required)
    public static void main(String[] args) {
        // client code goes here
        System.out.println("Hello there, from Deque!");

        Deque<Integer> dq = new Deque<>();
        System.out.println("Empty : " + dq.isEmpty());
        System.out.println("Size : " + dq.size());
        for (int i = 1; i < 11; i++) {
            dq.addFirst(i);
            // dq.addLast(9999);
            // dq.addLast(i + 100);
        }
        // dq.addFirst(1);
        // dq.addLast(9);
        // dq.addLast(10);
        /* System.out.println("head : " + dq.head.data + ", tail : " + dq.tail.data);
        dq.itr = dq.head;
        while (dq.itr != null) {
            System.out.print(dq.itr.data + "->");
            dq.itr = dq.itr.next;
        }
        System.out.println();
        System.out.println("Size : " + dq.size());
        System.out.println("Empty : " + dq.isEmpty());
        for (int i = 1; i < 4; i++) {
            System.out.print("Remove last : " + dq.removeLast() + ", ");
            System.out.print("head : " + dq.head.data + ", tail : " + dq.tail.data);
        }
        System.out.println();
        System.out.println("head : " + dq.head.data + ", tail : " + dq.tail.data);
        System.out.println();
        for (int i = 1; i < 4; i++) {
            System.out.print("Remove first : " + dq.removeFirst() + ", ");
        }
        System.out.println();
        System.out.println("head : " + dq.head.data + ", tail : " + dq.tail.data);
        // dq.addFirst(null); */

        System.out.println("Size : " + dq.size());
        System.out.println("Empty : " + dq.isEmpty());
        for (Integer integer : dq) {
            System.out.print(integer + ", ");
        }


    }
}
