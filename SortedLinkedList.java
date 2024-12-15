package project3;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an implementation of a sorted doubly-linked list.
 * All elements in the list are maintained in ascending/increasing order
 * based on the natural order of the elements.
 * This list does not allow null elements.
 *
 * @author Joanna Klukowska
 * @author Carsten Kaiser
 *
 * @param <E> the type of elements held in this list
 */
public class SortedLinkedList<E extends Comparable<E>>
    implements Iterable<E> {

    private Node head;
    private Node tail;
    private int size;

    /**
     * Constructs a new empty sorted linked list.
     */
    public SortedLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds the specified element to the list in ascending order.
     *
     * @param element the element to add
     * @return true if the element was added successfully,
     * false otherwise (if element==null)
     */
    public boolean add(E element) {
        //check if parameter is null
        if(element == null){
            return false;
        }

        Node newNode = new Node(element);

        //if list is empty
        if(head == null){
            head = newNode;
            tail = newNode;
        } else {
            Node current = head;
            Node previous = null;

            while(current != null && element.compareTo(current.data) > 0){
                previous = current;
                current = current.next;
            }

            //add to beginning of list
            if(previous == null){
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            } 
            
            //add to end of list
            else if(current == null){
                    previous.next = newNode;
                    newNode.prev = previous;
                    tail = newNode;
                }

            //add to middle of list
            else {
                previous.next = newNode;
                newNode.prev = previous;
                newNode.next = current;
                current.prev = newNode;
            }
        }
        
        size++;
        return true;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns true if the list contains the specified element,
     * false otherwise.
     *
     * @param o the element to search for
     * @return true if the element is in the list,
     * false otherwise
     */
    public boolean contains(Object o) {
        if(o == null){
            return false;
        }

        E element = (E) o;

        Node current = head;
        while(current != null){
            if(current.data.compareTo(element) == 0)
            {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * @param index the index of the element to return
     * @return the element at the specified index
     * @throw IndexOutOfBoundsException  if the index is out of
     * range(index < 0 || index >= size())
     */
    public E get(int index) throws IndexOutOfBoundsException {
        if( index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node current = head;
        int currentIndex = 0;

        while(currentIndex != index){
            current = current.next;
            currentIndex++;
        }

        return current.data;

    }

    /**
     * Returns the index of the first occurrence of the specified element in the list,
     * or -1 if the element is not in the list.
     *
     * @param o the element to search for
     * @return the index of the first occurrence of the element,
     * or -1 if the element is not in the list
     */
    public int indexOf(Object o) {
        if(o == null){
            return -1;
        }
        return nextIndexOf(o, 0);

    }

    /**
     * Returns the index of the first occurrence of the specified element in the list,
     * starting at the specified index, i.e., in the range of indexes
     * index <= i < size(), or -1 if the element is not in the list
     * in the range of indexes index <= i < size().
     *
     * @param o the element to search for
     * @param index the index to start searching from
     * @return the index of the first occurrence of the element, starting at the specified index,
     * or -1 if the element is not found
     */
    public int nextIndexOf(Object o, int index) {
        if(o == null || index < 0 || index >= size){
            return -1;
        }

        Node current = head;
        int currentIndex = 0;

        //move current to starting index
        while(currentIndex != index && current != null){
            current = current.next;
            currentIndex++;
        }

        //from starting index, search for Object o
        while (current != null) {
            if (current.data.equals(o)) {
                return currentIndex; 
            }
            
            current = current.next;
            currentIndex++;
        }

        return -1;
    }

    /**
     * Removes the first occurence of the specified element from the list.
     *
     * @param o the element to remove
     * @return true if the element was removed successfully,
     * false otherwise
     */
    public boolean remove(Object o) {
        if(o == null){
            return false;
        }

        Node current = head;
        
        while(current != null){
            if(current.data.equals(o)){
                //if it is first node
                if (current == head) {
                    head = current.next; 
                    if (head != null) {
                        head.prev = null;
                    } else {
                        tail = null;
                    }
                } 
                //if it is the last node
                else if(current == tail){
                    tail = current.prev; // Update tail to the previous node
                    if (tail != null) {
                        tail.next = null; // Set the new tail's next to null
                    } else {
                        head = null; // If the list is now empty, update head
                    }
                }
                //remove a node in middle
                else{
                    current.prev.next = current.next; 
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;

        }
        return false;

    }

    /**
     * Returns the size of the list.
     *
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in the list.
     *
     * @return an iterator over the elements in the list
     */
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
    * compares the specified object with this list for equality.
    * @param o the object to compare with
    * @return true if the specified object is equal to this list,
    * false otherwise
    */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(this == o){
            return true;
        }
        if(!(o instanceof SortedLinkedList)){
            return false;
        }

        SortedLinkedList<E> other = (SortedLinkedList<E>) o;

        if(this.size != other.size){
            return false;
        }
        
        Node currentThis = this.head;
        Node currentOther = other.head;

        while(currentThis != null){
            if(!currentThis.data.equals(currentOther.data)){
                return false;
            }
            currentThis = currentThis.next;
            currentOther = currentOther.next;
        }

        return true;

    }

    /**
     * Returns a string representation of the list.
     *  The string representation consists of a list of the lists's elements in
     *  ascending order, enclosed in square brackets ("[]").
     *  Adjacent elements are separated by the characters ", " (comma and space).
     *
     * @return a string representation of the list
     */
    public String toString() {
        String list = "[";

        Node current = head;
        while(current != null){
            list += current.data;
            current = current.next;

            if(current != null){
                list += ", ";
            }
        }

        return list + "]";

    }

    /* Inner class to represent nodes of this list.*/
    private class Node implements Comparable<Node> {
        E data;
        Node next;
        Node prev;
        Node(E data) {
            if (data == null ) throw new NullPointerException ("does not allow null");
            this.data = data;
        }
        Node (E data, Node next, Node prev) {
            this(data);
            this.next = next;
            this.prev = prev;
        }
        public int compareTo( Node n ) {
            return this.data.compareTo(n.data);
        }
    }

    /* A basic forward iterator for this list. */
    private class ListIterator implements Iterator<E> {

        Node nextToReturn = head;
        @Override
        public boolean hasNext() {
            return nextToReturn != null;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (nextToReturn == null )
                throw new NoSuchElementException("the end of the list reached");
            E tmp = nextToReturn.data;
            nextToReturn = nextToReturn.next;
            return tmp;
        }

    }
}
