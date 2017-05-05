//System.out. LLC
//
package graph;
import java.util.Stack;
public class DfsGraph extends Graph {

	java.util.ArrayList<Integer> path = new java.util.ArrayList<Integer>();
    Stack<Integer> poop = new Stack<Integer>();
    Stack<Integer> stack = new Stack<Integer>();
	public DfsGraph(String name) throws java.io.IOException {
		super(name);
	}

	//public DfsGraph(String name, int order, int size, boolean directed, boolean weighted) {
//		super(name, order, size, directed, weighted);
//	}

	public void dfs(int v)
	{
		markVertex(v);
		//path.add(v);
        stack.push(v);
		for (int w : getNeighbors(v))
		{
            if(!vertexMarked(w))
            {
                dfs(w);
                //path.add(w);
                //stack.push(w);
            }
           stack.push(w);
            
        }
	}
    
    public Stack<Integer> dfs2(int e)
    {
        
        stack.push(e);
        markVertex(e);
        poop.push(e);
        
        while(!stack.isEmpty())
        {
            int v = stack.pop();
            System.out.println("Popped: " + v);
            
                for(int w:getNeighbors(v))
                {
                    if(!vertexMarked(w))
                    {
                    stack.push(w);
                        poop.push(w);
                        System.out.println("Pushed : " + w);
                        markVertex(w);
                    }
                }
        }
        
        return poop;
    }
    
    public Stack<Integer> getStack()
    {return poop;}
    
   	public java.util.ArrayList<Integer> getPath()
	{return path;}



}