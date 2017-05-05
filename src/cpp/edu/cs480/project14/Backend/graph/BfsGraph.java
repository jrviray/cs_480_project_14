package graph;
import java.util.*;
/**
 * System.out. LLC
 */
public class BfsGraph extends GreedyGraph
{
    public Queue<Integer> qt;
    private ArrayList<Integer> path;
    public BfsGraph(String str) throws java.io.IOException
    {
        super(str);
    }
    public void greedy(int u)
    {
		setCost(u,0.0);
		q2.add(getVertex(u));
		while (q.size()>0)
        {
			int v=q.poll().getIndex();
			if (DEBUG) System.out.println("GreedyGraph:visit="+v);
			markVertex(v);
			for (int w:getNeighbors(v))
            {
				if (!vertexMarked(w))
                {
					if (isFringe(w))
                    {
						if (newCost(v,w)<costOf(w))
							modifyFringe(v,w);
					}
					else addFringe(v,w);
				}
			}
		}

	}
    public ArrayList<Integer> getPath()
    {return path;}
}
