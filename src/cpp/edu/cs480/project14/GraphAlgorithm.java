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
    
    public static ArrayList<Integer> Dijkstras(double[][] graph, int source) {
        double[] dist = new double[graph.length];
        ArrayList<Integer> pred = new ArrayList<>(graph.length);
        boolean[] visited = new boolean[graph.length];
        
        for(int i = 0; i < dist.length; i++) {
            dist[i] = Double.MAX_VALUE;
            pred.add(-1);
        }
        dist[source] = 0;
        
        
        for(int i = 0; i < dist.length; i++) {
            int next = minVertex(dist, visited);
            if(next == -1) {
                break;
            } else {
                visited[next] = true;
            }
            
            
            ArrayList<Integer> n = findNeighbors(graph, next);
            for(int j = 0; j < n.size(); j++) {
                int v = n.get(j);
                double d = dist[next] + graph[next][v];
                if(dist[v] > d) {
                    dist[v] = d;
                    pred.set(v, next);
                }
            }
        }
        System.out.println(pred);
        return pred;
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
