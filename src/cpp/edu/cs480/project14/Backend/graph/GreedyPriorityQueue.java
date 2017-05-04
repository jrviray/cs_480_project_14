//System.out. LLC
//Copyright (c) Mandayam A. Srinivas
package cpp.edu.cs480.project14.Backend.graph;
import java.util.*;
/**
 * Implements heap-based priority queue of vertices with cost
 * to support Dijkstra's greedy algorithm for weighted graphs.
 *
 * Index array locates vertices in heap array (implemented using java.util.ArrayList)
 * For vertex k, j=index[k] can be used with heap.get(j) to retrieve the vertex.
 * Code based on earlier versions by current author (2004, 2008).
 * Algorithm based on Brassard and Bratley: Algorithmics (1988).
 */

public class GreedyPriorityQueue {
	private static final int CAPACITY = 1001;
	private ArrayList<GreedyVertex> heap;
	private int[] index;
	
// constructors
/**
 *	Default constructor builds priority queue of standard capacity 
 */
	public GreedyPriorityQueue() {
	  this(CAPACITY);
	}
	
/**
 *	Constructor builds priority queue of given capacity
 */
	public GreedyPriorityQueue(int capacity) {
		heap = new ArrayList<GreedyVertex>(capacity);
		index = new int[capacity];
		for (int i=0; i<index.length; i++) index[i]=0;
	}
	
	// public methods
/**
 *	Adds vertex to priority queue
 *	@param v vertex being added
 */
	public void add(GreedyVertex v) {
		heap.add(v);
		index[v.getIndex()]=size()-1;
		percolate(size()-1);
	}
		
/**
 *	Restructure heap when vertex cost decreases
 *	@param v the vertex whose cost decreases
 */
	public void promote(GreedyVertex v) {
		percolate(index[v.getIndex()]);
	}

/**
 *	Restructure heap when vertex cost increases
 *	@param v the vertex whose cost increases
 */
	public void demote(GreedyVertex v) {
		siftDown(index[v.getIndex()]);
	}

/**
 *	Deletes least cost vertex from priority queue
 *	@return least cost vertex
 */
	public GreedyVertex poll() {
		GreedyVertex v=heap.get(0);
		swap(0,size()-1);
		heap.remove(size()-1);
		index[v.getIndex()]=0;
		siftDown(0);
		return v;
	}
	
/**
 *	Returns the number of elements in priority queue
 */
	public int size() {
	  return heap.size();
	}
	
/**
 *	Converts priority queue to string for printing
 */
	public String toString() {
		return Arrays.toString(heap.toArray());
	}

// private methods	
	private void swap(int a, int b) {
		GreedyVertex temp=heap.get(a);
		heap.set(a,heap.get(b));
		heap.set(b,temp);
		index[temp.getIndex()]=b;
		index[heap.get(a).getIndex()]=a;
	}
	
	private int smallerChild(int a) {
		if (size()>2*a+2 && heap.get(2*a+2).getCost()<heap.get(2*a+1).getCost()) return 2*a+2;
		else return 2*a+1;
	}
	
	private void siftDown(int a) {
		int b=a,c;
		while (b<size()/2 && heap.get(smallerChild(b)).getCost()<heap.get(b).getCost()) {
			c=smallerChild(b);
			swap(b,c);
			b=c;
		}
	}
	
	private void percolate(int a) {
		int b=a;
		while (b>0 && heap.get(b).getCost()<heap.get((b-1)/2).getCost()) {
			swap(b,(b-1)/2);
			b=(b-1)/2;
		}
	}
}