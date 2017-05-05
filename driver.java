import graph.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.lang.Character;
//Tsend-Ayush Batbileg
//CS241
//Project 4 driver file
/*This program computes the minimum spanning tree and the shortest path tree
 using the provided graph class and GreedyGraph
 */
public class driver
{
    private GreedyPriorityQueue bfsPath;
    private ArrayList<Integer> dfsPath;
    private ArrayList<Integer> dPath;
    private double djkDistance;
    private double mstCost;
    private double sptCost;
    private String[] s;
    public GreedyPriorityQueue getBFS()
    {return bfsPath;}
    public ArrayList<Integer> getDFS()
    {return dfsPath;}
    public ArrayList<Integer> getDJK()
    {return dPath;}
    public double getDjDistace()
    {return djkDistance;}
    public double getMSTCost()
    {return mstCost;}
    public double getSPTCost()
    {return sptCost;}
    
        //This program follows the sample output provided
    //greedy(int) is a implementation of Dijkstras's algorithm
    public static void mstWork(String str, int start, int end) throws IOException
    {
        MST m = new MST(str);
        m.greedy(start);
        System.out.println(m.toString());
        System.out.println("MPT edges");
        Edge[] e = m.getEdges();
        mstCost = 0.0;
        s = new String[e.length];
        for(int i = 0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                System.out.println(e[i]);
                if (e[i] != null)
                s[i] = "" + e[i].getU() + "->" + e[i].getV();
                mstCost += m.weightOf(e[i]);
            }
        }
    }
    public static void BFS(String str, int start) throws IOException
    {
        GreedyGraph m = new GreedyGraph(str);
        m.greedy(start);
        bfsPath = m.getBFS();
    }
       public static void DFS(String str, int start) throws IOException
    {
        DfsGraph d = new DfsGraph(str);
        d.dfs(start);
        dfsPath = d.getPath();
    }
    
    public static void sptWork(String str, int start, int end) throws IOException
    {
        SPT s = new SPT(str);
        s.greedy(start);
        Edge[] e = s.getEdges();
        sptCost = 0.0;
        for(int i =0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                System.out.println(e[i]);
                sptCost += s.weightOf(e[i]);
            }
        }
    }
    public static void dijkstras(String str, int start, int end)
    {
        GreedyGraph g = new GreedyGraph(str);
        g.greedy(start);
        dPath = new ArrayList<Integer>();
        while(s.getVertex(start) != s.getVertex(end))
        {
            //if the vertex is marked, then it will be added to the ArrayList
            //prevent overlaps
            //end is reassigned as the new parent index after added
            if(s.vertexMarked(end))
            {
                dPath.add(end);
                end = s.getVertex(end).getParent();
            }
        }
        Collections.reverse(dPath);
        djkDistance = 0.0;
        for(int i = 0; i < path.size() -1; i++)
            djkDistance += s.weightOf(new Edge(path.get(i), path.get(i+1)));
    }
}