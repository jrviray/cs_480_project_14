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
