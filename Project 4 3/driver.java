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
    public static void main(String[] args) throws IOException
    {
        //authenticate the input
        if(args.length != 1 || !Character.isLetter(args[0].charAt(0)))
        {
            System.out.println("<File>");
            return;
        }
        else
        {
            BFS(args[0]);
            DFS(args[0]);
            mstWork(args[0]);
            System.out.println("----------");
            sptWork(args[0]);
        }
    }
    //This program follows the sample output provided
    //greedy(int) is a implementation of Dijkstras's algorithm
    public static void mstWork(String str) throws IOException
    {
        MST m = new MST(str);
        m.greedy(0);
        System.out.println(m.toString());
        System.out.println("MPT edges");
        Edge[] e = m.getEdges();
        double mptCost = 0.0;
        for(int i = 0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                System.out.println(e[i]);
                mptCost += m.weightOf(e[i]);
            }
        }
        System.out.println(str + " MST cost=" + mptCost);
        
    }
    public static void BFS(String str) throws IOException
    {
        GreedyGraph m = new GreedyGraph(str);
        m.greedy(0);
        GreedyPriorityQueue path = m.getBFS();
        System.out.println("BFS path: " );
        System.out.println(path);
    }
    public static void DFS(String str) throws IOException
    {
        DfsGraph d = new DfsGraph(str);
        d.dfs(0);
        ArrayList<Integer> path = d.getPath();
        System.out.println("DFS Path: ");
        System.out.println(path);
    }
    
    public static void sptWork(String str) throws IOException
    {
        SPT s = new SPT(str);
        s.greedy(0);
        System.out.println("SPT edges");
        Edge[] e = s.getEdges();
        double sptCost = 0.0;
        for(int i =0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                System.out.println(e[i]);
                sptCost += s.weightOf(e[i]);
            }
        }
        System.out.println(str + " SPT cost=" +sptCost);
        ArrayList<Integer> path = new ArrayList<Integer>();
        int end = s.getOrder() - 1;
        while(s.getVertex(0) != s.getVertex(end))
        {
            //if the vertex is marked, then it will be added to the ArrayList
            //prevent overlaps
            //end is reassigned as the new parent index after added
            if(s.vertexMarked(end))
            {
                path.add(end);
                end = s.getVertex(end).getParent();
            }
        }
        path.add(0);
        Collections.reverse(path);
        System.out.println("Path from 0 to " + (s.getOrder() - 1) + "=" + path);
        double distance = 0.0;
        for(int i = 0; i < path.size() -1; i++)
            distance += s.weightOf(new Edge(path.get(i), path.get(i+1)));
        System.out.println("Distance=" + distance);
    }
}