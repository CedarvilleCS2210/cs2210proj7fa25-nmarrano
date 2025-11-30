package project7;
import java.util.Random;

/**
 * This ArrayHeap class extends ArrayBinaryTree and implements Heap to produce an
 * array-based implementation of a Heap that utilizes dynamic size expansion and
 * efficient sorted addition and removal methods, removing in non-decreasing order.
 * 
 * Title: Project #7
 * @author Noah Marrano
 * @version 1.0
 * File: ArrayHeap.java
 * Created: Nov 2025
 * 
 * Description: This class provides full implmentation of a Heap using an internal array
 * data strucuture, extended from the ArrayBinaryTree class. It uses several of the methods
 * already implemented in the ArrayBinaryTree class to be able to access the proper elements
 * of the binary tree being used to properly implement a heap data structure. This class
 * includes both 1 and 2 parameter constructors that make sure to instantiate their
 * corresponding ArrayBinaryTree objects in the process. It also includes add() and
 * removeRoot() methods for adding and removing to the heap, respectively, as well as a number
 * of private helper methods to accomplish these tasks, including bubbleDown(), bubbleUp(),
 * expand(), swap(), getApKey(), and smallestChild().
 */

public class ArrayHeap extends ArrayBinaryTree implements Heap {

    Comparator heapComp;

    //1-parameter constructor
    public ArrayHeap(Comparator newComp) {
        this (newComp, DEFAULT_SIZE);
    }

    //2-parameter constuctor
    public ArrayHeap(Comparator newComp, int newSize) {
        super (newSize);
        heapComp = newComp;
    }


    /**
     * Public void method that creates an Item object specified in the parameters
     * and adds it to the heap, if possible, in the correct position
     * 
     * @param newKey is the key of the Item to be added
     * @param newElement is the element or data of the Item to be added
     * @throws InvalidObjectException if newKey isn't a comparable object
     */
    public void add(Object newKey, Object newElement) throws InvalidObjectException {
        //Instantiating Item object
        Item newItem = new Item(newKey, newElement);

        //Checking for necessary dynamic expansion
        if (size == btArray.length) {
            expand(btArray.length*2);
        }

        //Instantiating ArrayPosition object
        ArrayPosition newAP = new ArrayPosition(size, newItem);

        btArray[size++] = newAP;
        bubbleUp(newAP);
    }

    /**
     * Public method that returns the Item object that is currently at the root
     * of the heap which has the smallest key value
     * 
     * @return a reference to the root Item with the smallest key
     * @throws EmptyHeapException if heap is empty
     */
    public Object removeRoot() throws EmptyHeapException {
        //Error check for empty heap
        if (isEmpty()) {
            throw new EmptyHeapException("Error: Removing from empty heap");
        }

        //Gets Item from root Position with safe casting
        ArrayPosition resultAP = (ArrayPosition) root();
        Item result = (Item) resultAP.element();

        //Gets last Position in heap and checks if it is also the root
        ArrayPosition lastAP = btArray[size-1];
        boolean lastElem = lastAP == resultAP;

        //Swap if last Position isn't the root
        if (!lastElem) {
            swap(resultAP, lastAP);
        }

        //Null out last Position that has been swapped to root
        btArray[resultAP.getIndex()] = null;
        size--;

        //Bubble down last Position if it isn't the root
        if (!lastElem) {
            bubbleDown(lastAP);
        }
        return result;
    }

    //Places the specified position in the correct place going down the heap
    private void bubbleDown(Position p) {
        //Safe cast
        ArrayPosition curr = (ArrayPosition) p;

        //Swap down while current element isn't external and is greater than the key of smallest child
        while (!isExternal(curr) && heapComp.isGreaterThan(getApKey(curr), 
            getApKey(smallestChild(curr)))) {
            swap(curr, smallestChild(curr));
        }
    }

    //Places the specified position in the correct place going up the heap
    private void bubbleUp(Position p) throws InvalidObjectException {
        //Safe cast
        ArrayPosition curr = (ArrayPosition) p;

        //Swap up while current position isn't root and is less than the key of the parent
        while (curr != root() && heapComp.isLessThan(getApKey(curr), 
            getApKey(parent(curr)))) {
            swap(curr, parent(curr));
        }
    }

    //Performs array copy and expansion to new specified size s
    private void expand(int s) {
        ArrayPosition[] tempArray = new ArrayPosition[s];
        for (int i = 0; i < btArray.length; i++) {
            tempArray[i] = btArray[i];
        }
        btArray = tempArray;
    }
  
    //Swaps the indicies of Position p1 and Position p2
    private void swap(Position p1, Position p2) {
        //Safe casting
        ArrayPosition ap1 = (ArrayPosition) p1;
        ArrayPosition ap2 = (ArrayPosition) p2;

        //Swapping index of ArrayPosition objects
        int tempIndex = ap1.getIndex();
        ap1.setIndex(ap2.getIndex());
        ap2.setIndex(tempIndex);

        //Setting actual indicies to ArrayPosition object's indicies
        btArray[ap1.getIndex()] = ap1;
        btArray[ap2.getIndex()] = ap2;
    }

    //Simplifies returning the key value from a specified Position
    private Object getApKey(Position p) {
        //Safe casting
        ArrayPosition ap = (ArrayPosition) p;
        Item temp = (Item) ap.element();

        return temp.key();
    }

    //Finds smaller of the 1 or 2 children of a specified Position
    private Position smallestChild(Position p) {
        //Safe cast
        ArrayPosition curr = (ArrayPosition) p;

        Position lc = leftChild(curr);
        Position rc = rightChild(curr);

        if (rc == null) { //If no right child, there must be a left child
            return lc;
        } else { //Find smaller of 2 children
            if (heapComp.isLessThan(getApKey(rc), getApKey(lc))) {
                return rc;
            } else {
                return lc;
            }
        }
    }
    
    //Main method tests functionality of heap
    public static void main (String[] args) {
        //Create new IntegerComparator to use for ArrayHeap object
	    Comparator myComp = new IntegerComparator();
        Heap myHeap = new ArrayHeap (myComp, 8); //Set to size 8 to utilize expand()

        //Create new random number generator
        Random random = new Random();

        //Create 10,000 heap elements with random key value and i-loop data value
        for (int i = 0; i < 10000; i++) {
            int newKey = random.nextInt(Integer.MAX_VALUE);
            myHeap.add(newKey, i);
        }


        //Check heap size
        System.out.println(myHeap.size());
        int test = -1;
        boolean success = true;
        while (!myHeap.isEmpty()) {

            //Get each removed item and check that all values are in non-decreasing key order
            Item removedItem = (Item) myHeap.removeRoot();
            if (test <= (Integer)removedItem.key()) {
                test = (Integer)removedItem.key();
            } else { //Indicate test failure
                System.out.println("Failed testing: not in proper order");
                success = false;
            }
        }
        System.out.println("All nodes removed"); //Indicate all nodes removed
        if (success) {
            System.out.println("Testing passed"); //Indicate test pass
        }
    }
}