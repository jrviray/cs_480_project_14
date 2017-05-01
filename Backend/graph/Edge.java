//System.out. LLC
//
package graph;
public class Edge implements Comparable<Edge> {
	protected int u,v;
	protected boolean directed;
	protected boolean weighted;
	protected double weight;
	protected boolean selected;
	
	public Edge(int u, int v) {
		this(u,v,false);
	}

	public Edge(int u, int v, boolean directed) {
		this(u,v,directed,false,0.0);
	}
	
	public Edge(int u, int v, double weight) {
		this(u,v,false,true,weight);
	}
	public Edge(int u, int v, boolean directed, boolean weighted, double weight) {
		if (directed) {
			this.u = u;
			this.v = v;
		}
		else {
			this.u = Math.min(u,v);
			this.v = Math.max(u,v);
		}
		this.directed=directed;
		this.weighted=weighted;
		this.weight=weight;
		this.selected=false;
	}
/**
 *  Is edge directed?
 *  @return true if vertex is directed
 */
	public boolean isDirected() {
		return directed;
	}

/**
 *  Is edge weighted?
 *  @return true if vertex is weighted
 */
	public boolean isWeighted() {
		return weighted;
	}

/**
 *  Is edge selected?
 *  @return true if vertex is selected
 */
	public boolean isSelected() {
		return selected;
	}
	
/**
 *  Select or unselect edge
 *  @param selected true to select edge, false to unselect it
 */
	public void setSelected(boolean selected) {
		this.selected=selected;
	}	

/**
 *  Return head vertex index
 *  @return head index v
 */
	public int getHead() {
		return v;
	}	
		
/**
 *  Return tail vertex index
 *  @return tail index u
 */
	public int getTail() {
		return u;
	}	
		
/**
 *  Return edge weight
 *  @return edge weight
 */
	public double getWeight() {
		return weight;
	}	
		
	public int hashCode() {  // this method and next are *both* needed
		return 99991*u+37*v;  // for HashMap "get" to work correctly
	}  // (default hashCodes don't match when same edge is "created" twice)
		// see Object.hashCode() and Object.equals() documentation
	
/**
 *  Are these two edges the same?
 *  @return true if endpoints match
 */
	public boolean equals(Object obj) {
		Edge that=(Edge)obj;
		return this.u==that.u && this.v==that.v;
	}
	
/**
 *  Comparison is based on lexicographic comparison of endpoint indices
 *  @return negative, zero or positive depending on result of comparison
 */
	public int compareTo(Edge that) {
		if (this.u==that.u) return this.v-that.v;
		return this.u-that.u;
	}
	
/**
 *  Convert edge into string for printing
 */
	public String toString() {
		if (weighted) return("("+u+","+v+","+weight+")");
		else return("("+u+","+v+")");
	}
}
