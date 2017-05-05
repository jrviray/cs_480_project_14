//System.out. LLC
//Copyright (c) Mandayam A. Srinivas
// Copyright (c) Mandayam A. Srinivas
//
/**
 * Implements the greedy algorithm on weighted graphs. 
 * Applications include Dijkstra's algorithm for 
 * minimum spanning trees and for single source shortest paths.
 * Provides hook method newCost that allows these two
 * algorithms to be implemented in derived classes.
 * Uses custom priority queue with "promote" method to recognize decreased vertex cost.
 */
package cpp.edu.cs480.project14.Backend.graph;
import java.util.ArrayList;
public class GreedyGraph extends Graph {
	private final boolean DEBUG=false;
	private GreedyPriorityQueue q;
	private GreedyPriorityQueue q2;
	private ArrayList<Integer> dfsPath;
	private ArrayList<Integer> bfsPath;

// constructor
	public GreedyGraph(String name) throws java.io.IOException {
		process_header(name);
		add_vertices();
		add_edges();
	}
	
// methods
	protected void add_vertices() {
		if(DEBUG)System.out.println("GreedyGraph.add_vertices");
		vertices=new GreedyVertex[order];
		q=new GreedyPriorityQueue();
		for (int i=0; i<order; i++) {
			vertices[i]=new GreedyVertex(i);
		}
	}
	
/**
 *  Returns vertex of specified index
 *  @return vertex
 */
	public GreedyVertex getVertex(int index) {
		return (GreedyVertex)vertices[index];
	}

/**
 *	Greedy method using priority queue for vertex costs
 *	@param u vertex being visited
 */
	public void greedy(int u) {
		setCost(u,0.0);
		q.add(getVertex(u));
		while (q.size()>0) {
			int v=q.poll().getIndex();
			if (DEBUG) System.out.println("GreedyGraph:visit="+v);
			markVertex(v);
			for (int w:getNeighbors(v)) {
				if (!vertexMarked(w)) {
					if (isFringe(w)) {
						if (newCost(v,w)<costOf(w)) {
							modifyFringe(v, w);
						}
						// end if newCost
					}  
					else addFringe(v,w);
					// end if fringe
				}// end if not marked
			} // end for
		} // end while
		
	}  // end greedy
	public void bfs(int u) {
		unmarkAll();
		setCost(u,0.0);
		q2.add(getVertex(u));
		bfsPath.add(u);
		while (q2.size()>0) {
			int v=q2.poll().getIndex();
			markVertex(v);
			for (int w:getNeighbors(v)) {
				if (!vertexMarked(w)) {
					if (isFringe(w)) {
						bfsPath.add(w);
						if (newCost(v,w)<costOf(w)) {
							modifyFringe(v, w);
						}
					}
					else addFringe(v,w);
				}
			}
		}
	}
	public ArrayList<Integer> getBfsPath()
	{return bfsPath;}
/**
 * Override this method to define formula used to compute new cost of vertex being processed. 
 * Formula depends on problem being solved.
 * @param v vertex being visited
 * @param w vertex being processed
 */
	public double newCost(int v, int w) {
		return 0.0;
	}
	
/**
 * Returns vertex cost
 * @param v vertex
 * @return v's cost
 */
	public double costOf(int v) {
		return getVertex(v).getCost();
	}

/**
 * Returns whether or not edge is selected
 * @param e edge
 * @return true if e is selected
 */
	public boolean isSelected(Edge e) {
		return getEdge(e).isSelected();
	}

/**
 * Returns edge weight
 * @param e edge
 * @return e's weight
 */
	public double weightOf(Edge e) {
		return  getEdge(e).getWeight();
	}

// private methods
	public boolean isFringe(int v) {
		return getVertex(v).isFringe();
	}

	public void setCost(int v, double cost) {
		getVertex(v).setCost(cost);
	}

// Add a new fringe vertex: define its cost & parent, select edge connecting it to tree.
	public final void addFringe(int v, int w) {
	// select edge
		getEdge(v,w).setSelected(true);
	// store vertex info and add vertex to priority queue
		double cost=newCost(v,w);
		if (DEBUG) System.out.println("GreedyGraph:addFringe="+w+"("+cost+")");
		GreedyVertex vertex=getVertex(w);
		vertex.setFringe(true);
		vertex.setParent(v);
		vertex.setCost(cost);
		q.add(vertex);
		if (DEBUG) System.out.println(q);
	}

// Modify existing fringe vertex: decrease its cost and change parent, unselect old edge
// and select new edge connecting it to tree.  Delete & add back to priority queue with new cost.
	public final void modifyFringe(int v, int w)
	{
		if (DEBUG) System.out.println("GreedyGraph:modifyFringe=("+v+","+w+")");	
	//	select new edge
		getEdge(v,w).setSelected(true);
	// unselect old edge
		getEdge(getVertex(w).getParent(),w).setSelected(false);
	// update vertex info and update priority queue
		double cost=newCost(v,w);
		GreedyVertex vertex=getVertex(w);
		vertex.setParent(v);
		vertex.setCost(cost);
		q.promote(vertex);
		if (DEBUG) System.out.println(q);
	}
}	