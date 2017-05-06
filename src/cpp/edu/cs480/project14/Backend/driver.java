package cpp.edu.cs480.project14.Backend;

import cpp.edu.cs480.project14.Backend.graph.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

//Tsend-Ayush Batbileg
//CS241
//Project 4 driver file
/*This program computes the minimum spanning tree and the shortest path tree
 using the provided graph class and GreedyGraph
 */
public class driver
{
    private double djkDistance;
    private double mstCost;
    private double sptCost;
    
    public double getDjDistace()
    {return djkDistance;}
    public double getMSTCost()
    {return mstCost;}
    public double getSPTCost()
    {return sptCost;}

//    public static void main(String[] args) throws  IOException{
//        driver.sptWork("graph0.txt",0,5);
//       //driver.BFS("graph0.txt",0);
//
//    }

    //This program follows the sample output provided
    //greedy(int) is a implementation of Dijkstras's algorithm
    public static Pair<Integer, Integer>[] mstWork(String str, int start, int end) throws IOException
    {
        MST m = new MST(str);
        m.greedy(start);
        Edge[] e = m.getEdges();
        Pair<Integer,Integer>[] result = new Pair[e.length];
        double mstCost = 0.0;
        for(int i = 0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                result[i] = new Pair<>(e[i].getU(),e[i].getV());
                mstCost += m.weightOf(e[i]);
            }
        }
        return result;
    }
    public static ArrayList<Integer> BFS(String str, int start) throws IOException
    {
        GreedyGraph m = new GreedyGraph(str);
        m.greedy(start);
        GreedyPriorityQueue bfsPath = m.getBFS();
        ArrayList<Integer> array = new ArrayList<Integer>();
       while(bfsPath.size() > 0)
        {
            array.add(bfsPath.poll().getIndex());
        }
        return array;
    }
    public static ArrayList<Integer> DFS(String str, int start) throws IOException
    {
        DfsGraph d = new DfsGraph(str);
        d.dfs(start);
        ArrayList<Integer> dfsPath = d.getPath();
        return dfsPath;
    }

    public static Pair<Integer, Integer>[] sptWork(String str, int start, int end) throws IOException
    {
        SPT s = new SPT(str);
        s.greedy(start);
        Edge[] e = s.getEdges();
        double sptCost = 0.0;
        Pair<Integer,Integer>[] result = new Pair[e.length];
        String[] p = new String[e.length];
        for(int i =0; i < e.length; i++)
        {
            if(e[i].isSelected())
            {
                result[i] = new Pair<>(e[i].getU(),e[i].getV());
            }
        }
        return result;
    }
    public static ArrayList<Integer> dijkstras(String str, int start, int end) throws IOException
    {
        GreedyGraph g = new GreedyGraph(str);
        g.greedy(start);
        ArrayList<Integer> dPath = new ArrayList<Integer>();
        while(g.getVertex(start) != g.getVertex(end))
        {
            //if the vertex is marked, then it will be added to the ArrayList
            //prevent overlaps
            //end is reassigned as the new parent index after added
            if(g.vertexMarked(end))
            {
                dPath.add(end);
                end = g.getVertex(end).getParent();
            }
        }
        Collections.reverse(dPath);
        double djkDistance = 0.0;
        for(int i = 0; i < dPath.size() -1; i++)
            djkDistance += g.weightOf(new Edge(dPath.get(i), dPath.get(i+1)));
            System.out.println(djkDistance);
        return dPath;
    }
}