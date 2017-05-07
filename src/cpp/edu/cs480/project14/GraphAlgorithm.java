package cpp.edu.cs480.project14;

import javafx.animation.PauseTransition;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by wxy03 on 5/5/2017.
 */
public class GraphAlgorithm {

    public static ArrayList<Integer> BFS(double[][] graph,int source)
    {
        int size = graph.length;
        boolean[] visited = new boolean[size];

        LinkedList<Integer> queue = new LinkedList<>();
        ArrayList<Integer>  path = new ArrayList<>();

        visited[source] = true;
        queue.add(source);

        while(queue.size()!=0) {
            source = queue.poll();
            path.add(source);
            for (int i = 0; i < size; i++) {
                if (graph[source][i] != Double.MAX_VALUE && !visited[i]) {
                    queue.add(i);
                    visited[i] = true;
                }

            }
        }
        return path;
    }

    public static ArrayList<Integer> DFS(double[][] graph,int source)
    {
        int size = graph.length;
        boolean[] visited = new boolean[size];

        ArrayList<Integer> path = new ArrayList<>();
        visited[source]= true;
        DFSUtil(source,visited,graph,path);
        return path;


    }
    
    public static ArrayList<Integer> Dijkstras(double[][] graph, int source, int goal) {
        ArrayList<Integer> prev = new ArrayList<>(graph.length);
        double[] dist = new double[graph.length];
        boolean[] visited = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) {
            dist[i] = Double.MAX_VALUE;
            prev.add(-1);
            visited[i] = false;            
        }
        dist[source] = 0;
        while(!areAllTrue(visited)) {
            int u = minVertex(dist, visited);
            if(u == goal || u == -1) {
                break;
            }
            visited[u] = true;
            ArrayList<Integer> neighbors = findNeighbors(graph, u);
            for (int v : neighbors) {
                if(graph[u][v] == Double.MAX_VALUE) {
                }
                else {
                    double alt = dist[u] + graph[u][v]; 
                    if(alt < dist[v]) {
                        dist[v] = alt;
                        prev.set(v, u);
                    }
                }
                
            }
          
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        return printPath(prev, ret, goal);
    }
    private static ArrayList<Integer> printPath(ArrayList<Integer> parent, ArrayList<Integer> output, int j) {
        if(parent.get(j) != -1) {
            printPath(parent, output, parent.get(j));
            output.add(j);
        }
        return output;
    }
        private static boolean areAllTrue(boolean[] a) {
        for(boolean b: a) {
            if(!b) {
                return false;
            }
        }
        return true;
    }
    private static int minVertex(double[] dist, boolean[] v) {
        double x = Double.MAX_VALUE;
        int y = -1;
        for (int i = 0; i < dist.length; i++) {
            if(!v[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }
        }
        return y;
    }
    

    private static void DFSUtil(int thisVertexID,boolean[] visited,double[][] graph,ArrayList<Integer> path)
    {
        visited[thisVertexID] = true;
        path.add(thisVertexID);
        ArrayList neighbor = findNeighbors(graph,thisVertexID);
        for(int i=0;i<neighbor.size();i++)
        {
            int destVertexID = (Integer)neighbor.get(i);
            if(visited[destVertexID]==false)
            {
                DFSUtil(destVertexID,visited,graph,path);
                path.add(thisVertexID);
            }
        }

    }

    private static ArrayList<Integer> findNeighbors(double[][] graph,int source)
    {
        ArrayList<Integer> neighbor = new ArrayList<>();
        for(int i = 0;i< graph.length;i++)
        {
            if(graph[source][i]!=Double.MAX_VALUE)
            {
                neighbor.add(i);
            }
        }
        return neighbor;
    }
    
    
}
