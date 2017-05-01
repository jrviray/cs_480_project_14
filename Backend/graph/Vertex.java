//System.out. LLC
//
//Copyright (c) Mandayam A. Srinivas
/**
 *  A vertex is identified by an integer index<p>
 *
 * Class variables:
 * <ul>
 * <li> index: integer identifying vertex
 * <li> marked: is vertex marked?
 * <li> degree: number of neighbors
 * <li> parent: index of parent vertex in tree
 * <li> preNumber: ordinal number assigned during previsit
 * <li> postNumber: ordinal number assigned during postvisit
 * </ul>
 *
 */
package graph;
public class Vertex {
	protected int index;
	protected boolean marked;
	protected int degree;
	protected int parent;
	protected int preNumber;
	protected int postNumber;
	private AdjNode neighbors;

	public Vertex(int index) {
		this.index=index;
		marked=false;
		degree=0;
		parent=0;
		preNumber=0;
		postNumber=0;
		neighbors=null;
	}

/**
 *  Return integer identifying vertex
 *  @return integer identifying vertex
 */
	public int getIndex() {
		return index;
	}

/**
 *  Is vertex marked?
 *  @return true if vertex is marked
 */
	public boolean isMarked() {
		return marked;
	}

/**
 *  Mark or unmark vertex
 *  @param marked true to mark vertex, false to unmark it
 */
	public void setMarked(boolean marked) {
		this.marked=marked;
	}

/**
 *  Return number of neighbors of vertex
 *  @return degree of vertex
 */
	public int getDegree() {
		return degree;
	}

/**
 *  Return vertex's parent
 *  @return parent of vertex
 */
	public int getParent() {
		return parent;
	}
	
/**
 *  Set vertex to be parent of this vertex
 *  @param parent vertex to be parent of this vertex
 */
	public void setParent(int parent) {
		this.parent=parent;
	}

/**
 *  Return vertex preNumber
 *  @return vertex preNumber
 */
	public int getPreNumber() {
		return preNumber;
	}
	
/**
 *  Set preNumber of this vertex
 *  @param num preNumber of this vertex
 */
	public void setPreNumber(int num) {
		this.preNumber=num;
	}

/**
 *  Return vertex postNumber
 *  @return vertex postNumber
 */
	public int getPostNumber() {
		return postNumber;
	}
	
/**
 *  Set postNumber of this vertex
 *  @param num postNumber of this vertex
 */
	public void setPostNumber(int num) {
		this.postNumber=num;
	}

/**
 *  Are these two vertices the same?
 *  @return true if indices match
 */
	public boolean equals(Vertex that) {
		return this.index==that.index;
	}

/**
 *  Add a new vertex to adjacency list
 *  @param v vertex to add to adjacency list
 */
	public void addNeighbor(int v) {
		neighbors=new AdjNode(v, neighbors);
		degree++;
	}
/**
 *  Return list of vertex neighbors
 *  @return array of adjacent vertices
 */
	public int[] getNeighbors() {
		int[] pals = new int[degree];
		int i = 0;
		for (AdjNode p=neighbors;p!=null;p=p.next)
			pals[i++]=p.v;
		return pals;
	}
/**
 *  Convert vertex into string for printing
 */
	public String toString() {
		return "v"+index;
	}
// private class
	private class AdjNode {
		int v;
		AdjNode next;

		AdjNode(int v, AdjNode next) {
			this.v =v;
			this.next = next;
		}
	}	
}
