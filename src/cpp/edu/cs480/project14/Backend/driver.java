package cpp.edu.cs480.project14.Backend;
import cpp.edu.cs480.project14.Backend.graph.*;
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
public class driver {
    public static void main(String[] args) throws IOException {
        //authenticate the input
        if (args.length != 3 || !Character.isLetter(args[0].charAt(0))) {
            System.out.println("<File>");
            return;
        } else {
            mstWork(args[0]);
            System.out.println("----------");
            sptWork(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            System.out.println("__________");
            dfs(args[0]);
        }
    }
    public static void bfs(String str) throws IOException
    {
        GreedyGraph g = new GreedyGraph(str);
        g.bfs(0);
        ArrayList<Integer> list = g.getBfsPath();
        System.out.println(list);
    }

    public static void dfs(String str) throws IOException
    {
        DfsGraph d = new DfsGraph(str);
        d.dfs(0);
        ArrayList<Integer> path = d.getPath();
        System.out.println("DFS path: " + path);
    }
    //This program follows the sample output provided
    //greedy(int) is a implementation of Dijkstras's algorithm
    public static double mstWork(String str ) throws IOException
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
        return mptCost;

    }


    public static void sptWork(String str, int start , int end) throws IOException
    {
        System.out.println("Start value: " + start);
        System.out.println("End value: " + end);
        SPT s = new SPT(str);
        s.greedy(0);
        System.out.println("SPT edges");
        Edge[] e = s.getEdges();
        double sptCost = 0.0;
        for (int i = 0; i < e.length; i++)
        {
            if (e[i].isSelected()) {
                System.out.println(e[i]);
                sptCost += s.weightOf(e[i]);
            }
        }
        System.out.println(str + " SPT cost=" + sptCost);
        ArrayList<Integer> path = new ArrayList<Integer>();
        while (s.getVertex(start) != s.getVertex(end)) {
            //if the vertex is marked, then it will be added to the ArrayList
            //prevent overlaps
            //end is reassigned as the new parent index after added
            if (s.vertexMarked(end)) {
                path.add(end);
                end = s.getVertex(end).getParent();
            }
        }
        Collections.reverse(path);
        System.out.println("Path from " + start + " to " + end + "=" + path);
        double distance = 0.0;
        for (int i = 0; i < path.size() - 1; i++)
            distance += s.weightOf(new Edge(path.get(i), path.get(i + 1)));
        System.out.println("Distance=" + distance);
    }
}