//System.out. LLC
//
import graph.*;
public class DfsGraph extends Graph {

	java.util.ArrayList<Integer> path = new java.util.ArrayList<Integer>();
	public DfsGraph(String name) throws java.io.IOException {
		super(name);
	}

	//public DfsGraph(String name, int order, int size, boolean directed, boolean weighted) {
//		super(name, order, size, directed, weighted);
//	}

	public void dfs(int v)
	{
		markVertex(v);
		path.add(v);
		for (int w : getNeighbors(v))
		{
			if (!vertexMarked(w))
				dfs(w);
		}
	}
	public java.util.ArrayList<Integer> getPath()
	{return path;}



}