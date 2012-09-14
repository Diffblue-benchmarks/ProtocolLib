package com.comphenix.protocol.injector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An implicitly sorted array list that preserves insertion order and maintains duplicates.
 * 
 * Note that only the {@link insertSorted} method will update the list correctly,
 * @param <T> - type of the sorted list.
 */
class SortedCopyOnWriteArray<T> implements Iterable<T> {
	// Prevent reordering
	private volatile List<T> list;
	
	public SortedCopyOnWriteArray() {
		this(new ArrayList<T>());
	}
	
	public SortedCopyOnWriteArray(List<T> wrapped) {
		this.list = wrapped;
	}
	
	/**
	 * Inserts the given element in the proper location.
	 * @param value - element to insert.
	 */
    @SuppressWarnings("unchecked")
	public synchronized void insertSorted(T value) {
    	
    	// We use NULL as a special marker, so we don't allow it
    	if (value == null)
    		throw new IllegalArgumentException("value cannot be NULL");
    	
    	List<T> copy = new ArrayList<T>();
        Comparable<T> compare = (Comparable<T>) value;
        
        for (T element : list) {
        	// If the value is now greater than the current element, it should be placed right before it
        	if (value != null && compare.compareTo(element) < 0) {
        		copy.add(value);
        		value = null;
        	}
        	copy.add(element);
        }
        
        // Don't forget to add it
        if (value != null)
        	copy.add(value);
        
        list = copy;
    }
    
    public T get(int index) {
    	return list.get(index);
    }
    
    /**
     * Retrieve the size of the list.
     * @return Size of the list.
     */
    public int size() {
    	return list.size();
    }

    /**
     * Retrieves an iterator over the elements in the given list.
     */
	public Iterator<T> iterator() {
		return list.iterator();
	}
}