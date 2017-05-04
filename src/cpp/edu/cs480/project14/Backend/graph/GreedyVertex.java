//System.out. LLC
//Copyright (c) Mandayam A. Srinivas

/**
 * Information associated with greedy vertex:
 *
 * <ul>
 *   <li> cost: current cost of vertex
 *	 <li> fringe: true if a fringe vertex
 * </ul>
 */
package cpp.edu.cs480.project14.Backend.graph;
public class GreedyVertex extends Vertex implements Comparable<GreedyVertex>{
	private double cost;
	private boolean fringe;

//	if cost not specified, assume vertex not connected, that is, it has infinite cost
	public GreedyVertex(int index) {
		this(index, Double.POSITIVE_INFINITY);
	}

	public GreedyVertex(int index, double cost) {
		super(index);
		fringe=false;
		parent=0;
		this.cost=cost;
	}

//	vertices are compared based on cost, with ties broken by vertex index
	public int compareTo(GreedyVertex that) {
		if (this.cost<that.cost) return -1;
		if (this.cost==that.cost) {
			if (this.index<that.index) return -1;
			else return 1;
		}
		return 1;
	}

/**
 *  Return vertex cost
 *  @return vertex cost
 */
	public double getCost() {
		return cost;
	}
	
/**
 *  Set vertex cost
 *  @param cost new value of vertex cost
 */
	public void setCost(double cost) {
		this.cost=cost;
	}
	
/**
 *  Is this a fringe vertex?
 *  @return true if a fringe vertex
 */
	public boolean isFringe() {
		return fringe;
	}
	
/**
 *  Set or inset vertex to be fringe
 *  @param fringe true to make fringe, false to unfringe
 */
	public void setFringe(boolean fringe) {
		this.fringe=fringe;
	}
}
