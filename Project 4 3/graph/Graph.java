//System.out. LLC

package graph;
import java.io.*;
import java.util.*;
/**
 *	Implements Graph ADT using adjacency lists.
 * 
 * Class variables:
 * <ul>
 * <li> name: graph data file name
 * <li> order: number of vertices
 * <li> size: number of edges
 * <li> directed: is graph directed?
 * <li> weighted: is graph weighted?
 * <li> vertices: vertex array
 * <li> edges: edge map
 * </ul>
 * 
 */
public class Graph {
	private final boolean DEBUG=false;
	private Scanner in;
	protected String name;
	protected int order, size;
	protected boolean directed;
	protected boolean weighted;
	protected Vertex[] vertices;
	protected TreeMap<Edge,Edge> edges=new TreeMap<>();
	             // key is just a vertex pair, value provides details 

// constructors
/**
 * Default constructor
 */
public Graph() {}

/**
 * Reads file and builds graph
 */
	public Graph(String name) throws IOException { 
		process_header(name);
		add_vertices();
		add_edges();
	}
	
/**
 * Builds empty graph with given characteristics
 */
	public Graph(String name, int order, int size, boolean directed, boolean weighted) {
		this.name=name;
		this.order=order;
		this.size=size;
		this.directed=directed;
		this.weighted=weighted;
		add_vertices();
	}
	
// methods to build graph from file
/**
 * Open file and process header line
 */
	protected void process_header(String name) throws java.io.IOException {
		in=new Scanner(new File(name));
		this.name=name;
		this.order=in.nextInt();
		this.size=in.nextInt();
		this.directed=(in.next().equals("directed"));
		in.nextLine();
		this.weighted=false;		
	}

/**
 * Create vertex array
 */
	protected void add_vertices() {
		if(DEBUG)System.out.println("Graph.add_vertices");
		this.vertices=new Vertex[order];
		for (int i=0; i<order; i++) {
			vertices[i]=new Vertex(i);
		}
	}

/**
 * create edge dictionary and adjacency lists
 */
	protected void add_edges() {
		for (int i=0;i<size;i++) {
			String line=in.nextLine();
			String[] val=(line.trim()).split("\\s+");
			int u=Integer.parseInt(val[0]);
			int v=Integer.parseInt(val[1]);
			if (val.length==2) addEdge(u,v);
			else {
				double weight=Double.parseDouble(val[2]);
				this.weighted=true;
				addEdge(u,v,weight);
			} // end if
		} // end for
	}  // end add_edges

/**
 *  Return graph name
 *  @return graph name
 */
	public String getName() {
		return name;
	}
	
/**
 *  Return number of vertices
 *  @return number of vertices
 */
	public int getOrder() {
		return order;
	}
	
/**
 *  Return number of edges
 *  @return number of edges
 */
	public int getSize() {
		return size;
	}
	
/**
 *  Check if graph is directed or undirected
 *  @return true if graph is directed, false if undirected
 */
	public boolean isDirected() {
		return directed;
	}
	
/**
 *  Check if graph is weighted or unweighted
 *  @return true if graph is weighted, false if unweighted
 */
	public boolean isWeighted() {
		return weighted;
	}
	
/**
 *  Return degree of graph
 *  @return max(degree(v)) 0&le;v&le;order-1
 */
	public int getDegree() {
		int maxDegree=0,currentDegree;
		for (Vertex vertex:vertices) {
			currentDegree=vertex.getDegree();
			if (currentDegree>maxDegree) maxDegree=currentDegree;
		}
		return maxDegree;
	}

/**
 *  Return degree of vertex
 *  @param v vertex
 *  @return number of neighbors of v
 */
	public int getDegree(int v) {
		return vertices[v].getDegree();
	}
	
/**
 *  Return vertex of specified index
 *  @return vertex
 */
	public Vertex getVertex(int index) {
		return vertices[index];
	}

/**
 *  Return list of all edges
 *  @return array of edges
 */
	public Edge[] getEdges() {
		return edges.keySet().toArray(new Edge[0]);
	}
	
/**
 *  Unmark all vertices
 */
	public void unmarkAll() {
		for (int i=0; i<order; i++) {
			vertices[i].setMarked(false);
		}
	}
	
/**
 * Is vertex marked?
 *  @param v vertex
 * @return true if v is marked
 */
	public boolean vertexMarked(int v) {
		return vertices[v].isMarked();
	}
	
/**
 * Mark vertex
 * @param v vertex
 */
	public void markVertex(int v) {
		vertices[v].setMarked(true);
	}
		
/**
 *  Return list of neighbors of vertex
 *  @param v vertex
 *  @return array of neighbors of v
 */
	public int[] getNeighbors(int v) {
		return vertices[v].getNeighbors();
	}
	
/**
 *  Add weighted edge
 *  @param u vertex
 *  @param v vertex
 *  @param weight edge weight
 */
	public void addEdge(int u, int v, double weight) {
		vertices[u].addNeighbor(v);
		if (!directed) vertices[v].addNeighbor(u);
		Edge edge=new Edge(u,v,directed,weighted,weight);
		edges.put(edge,edge);
	}
	
/**
 *  Add unweighted (unit weight) edge
 *  @param u vertex
 *  @param v vertex
 */
	public void addEdge(int u, int v) {
		addEdge(u, v, 1.0);
	}

	/**
	 *  Return information for edge (u,v)
	 *  @return Edge
	 */
	public Edge getEdge(int u, int v) {
		return edges.get(new Edge(u,v,directed));
	}

/**
 *  Return information for edge e
 *  @return Edge
 */
	public Edge getEdge(Edge e) {
		return edges.get(e);
	}
		
/**
 *  Convert graph into string for printing
 */
	public String toString() {
		StringBuffer buf=new StringBuffer("Name="+name+", order="+order+", size="+size+", ");
		buf.append("directed="+directed+", weighted="+weighted+"\n");
		for (int v=0; v<order; v++) buf.append("   "+v+" => "+Arrays.toString(getNeighbors(v))+"\n");
		return buf+"";
	}
		
}	