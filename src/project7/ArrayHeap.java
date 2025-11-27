package project7;

/**
 * Title:        Project #7
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ArrayHeap extends ArrayBinaryTree implements Heap {

    Comparator heapComp;

    public ArrayHeap(Comparator newComp) {
        this (newComp, DEFAULT_SIZE);
    }

    public ArrayHeap(Comparator newComp, int newSize) {
        super (newSize);
        heapComp = newComp;
    }


    public void add(Object newKey, Object newElement) throws InvalidObjectException {
        Item newItem = new Item(newKey, newElement);
        if (size == btArray.length) {
            expand(btArray.length*2);
        }
        ArrayPosition newAP = new ArrayPosition(size-1, newItem);
        bubbleUp(newAP);
        size++;
    }

    public Object removeRoot() throws EmptyHeapException {
        if (isEmpty()) {
            throw new EmptyHeapException("Error: Removing from empty heap");
        }
        ArrayPosition resultAP = (ArrayPosition) root();
        ArrayPosition lastAP = btArray[size-1];
        bubbleDown(lastAP);
        size--;
        Item result = (Item) resultAP.element();
        return result;
    }

    private void bubbleDown(Position p) {
        ArrayPosition curr = (ArrayPosition) p;
        while (!isExternal(curr) && heapComp.isLessThan(getApKey(curr), getApKey(smallestChild(curr)))) {
            swap(curr, smallestChild(curr));
        }
    }

    private void bubbleUp(Position p) throws InvalidObjectException {
        ArrayPosition curr = (ArrayPosition) p;
        while (curr != root() && heapComp.isLessThan(getApKey(curr), getApKey(parent(curr)))) {
            swap(curr, parent(curr));
        }
    }

    private void expand(int s) {
        ArrayPosition[] tempArray = new ArrayPosition[s];
        for (int i = 0; i < btArray.length; i++) {
            tempArray[i] = btArray[i];
        }
        btArray = tempArray;
    }
  
    private void swap(Position p1, Position p2) {
        ArrayPosition ap1 = (ArrayPosition) p1;
        ArrayPosition ap2 = (ArrayPosition) p2;
        int tempIndex = ap1.getIndex();
        ap1.setIndex(ap2.getIndex());
        ap2.setIndex(tempIndex);
        btArray[ap1.getIndex()] = ap1;
        btArray[ap2.getIndex()] = ap2;
    }

    private Object getApKey(Position p) {
        ArrayPosition ap = (ArrayPosition) p;
        Item temp = (Item) ap.element();
        return temp.key();
    }

    private Position smallestChild(Position p) {
        ArrayPosition curr = (ArrayPosition) p;
        Position lc = leftChild(curr);
        Position rc = rightChild(curr);
        if (rc == null) {
            return lc;
        } else {
            if (heapComp.isLessThan(getApKey(rc), getApKey(lc))) {
                return rc;
            } else {
                return lc;
            }
        }
    }
        // you may want to expand main; it is just provided as a sample
    public static void main (String[] args) {
	    Comparator myComp = new IntegerComparator();
        Heap myHeap = new ArrayHeap (myComp, 8);

        myHeap.add(new Integer(14),new Integer(14));
        myHeap.add(new Integer(17),new Integer(17));
        myHeap.add(new Integer(3),new Integer(3));
        myHeap.add(new Integer(2),new Integer(21));
        myHeap.add(new Integer(8),new Integer(8));
        myHeap.add(new Integer(7),new Integer(18));
        myHeap.add(new Integer(1),new Integer(1));
        myHeap.add(new Integer(19),new Integer(11));
        myHeap.add(new Integer(17),new Integer(17));
        myHeap.add(new Integer(25),new Integer(6));

        System.out.println(myHeap.size());
        while (!myHeap.isEmpty()) {

            Item removedItem = (Item) myHeap.removeRoot();
            System.out.print("Key:   " + removedItem.key() + "     ");
            System.out.println("Removed " + removedItem.element());
        }
        System.out.println("All nodes removed");
    }
}
