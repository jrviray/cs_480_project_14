package cpp.edu.cs480.project14;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by wxy03 on 5/5/2017.
 */
public class GraphAlgorithm {

    public static ArrayList<Integer> BFS(double[][] graph, int source) {
        int size = graph.length;
        boolean[] visited = new boolean[size];

        LinkedList<Integer> queue = new LinkedList<>();
        ArrayList<Integer> path = new ArrayList<>();

        visited[source] = true;
        queue.add(source);

        while (queue.size() != 0) {
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

    public static ArrayList<Integer> DFS(double[][] graph, int source) {
        int size = graph.length;
        boolean[] visited = new boolean[size];

        ArrayList<Integer> path = new ArrayList<>();
        visited[source] = true;
        DFSUtil(source, visited, graph, path);
        return path;


    }

    public static ArrayList<Integer> GreedyNonOptimal(double[][] graph, int source, int goal) {

        ArrayList<Integer> BFS = BFS(graph,source);
        boolean accessible = false;
        for(int i=0;i<BFS.size();i++)
        {
            if(BFS.get(i)==goal)
                accessible=true;
        }
        if(!accessible)
            throw new IllegalArgumentException("No path found");
        else
        {
            boolean [] visited = new boolean[graph.length];
            for(int i =0; i < graph.length; i++) {
                visited[i] = false;
            }
            ArrayList<Integer> ret = new ArrayList<>(graph.length);
            int temp = source;
            visited[temp]=true;
            ret.add(temp);

            while(temp != goal){
                ArrayList<Integer> neighbors = findUnvisitedNeighbors(graph,visited,temp);
                int u = minVertex(neighbors, graph, temp);

                if(u == -1) {
                    temp=ret.get(ret.size()-2);
                    ret.remove(ret.size()-1);
                }
                else {
                        ret.add(u);
                        visited[u] = true;
                    temp = u;
                }

            }
            return ret;
        }

    }

    private static ArrayList<Integer> findUnvisitedNeighbors(double[][] graph,boolean[] visited,int source)
    {
        ArrayList<Integer> neighbor = new ArrayList<>();
        for(int i = 0;i< graph.length;i++)
        {
            if(graph[source][i]!=Double.MAX_VALUE && visited[i]==false)
            {
                neighbor.add(i);
            }
        }
        return neighbor;
    }








    //returns integer ID of next lowest weighted neighbor, regardless if it can reach the goal.
    public static int minVertex(ArrayList<Integer> a, double[][] graph, int source) {
        int low = -1;
        double y = Integer.MAX_VALUE;
        for(int b : a) {
            if(graph[source][b] < y) {
                low = b;
                y = graph[source][b];
            }
        }
        return low;
    }
    //finds shortest path from goal node to every single other node, returns the distances to that array
    public static double[] DijkstrasDistance(double[][] graph, int source) throws IllegalArgumentException {
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
            if(u == -1) {
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
        return dist;
    }
    //returns previous node array, to find shortest path from every node to previous
    public static ArrayList<Integer> Dijkstras(double[][] graph, int source) throws IllegalArgumentException {
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
            if(u == -1) {
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
        return prev;
    }
    private static ArrayList<Integer> printPath(ArrayList<Integer> parent, ArrayList<Integer> output, int j) {
        if(parent.get(j) != -1) {
            printPath(parent, output, parent.get(j));
            output.add(j);
        }
        return output;
    }
    public static ArrayList<Integer> printPath(ArrayList<Integer> parent, int j) throws IllegalArgumentException{
        ArrayList ret = new ArrayList<Integer>();
        ret = printPath(parent, ret, j);
        if(ret.size() == 0) {
            throw new IllegalArgumentException("No path found");
        }
        return ret;
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
    
     /**
     * This MST using Prim's Algorithms, The graph should be restricted to connected and undirected
     * @param graph
     * @return
     */
    public static Pair<Integer,Integer>[] MST_undirected(double[][] graph) throws IllegalArgumentException{


        checkConnected_undirected(graph);
        int size = graph.length;
        // Array to store constructed MST
        int[] parent = new int[size];

        // Key values used to pick minimum weight edge in cut
        double[] key = new double[size];

        // To represent set of vertices not yet included in MST
        Boolean mstSet[] = new Boolean[size];

        // Initialize all keys as INFINITE
        for (int i = 0; i < size; i++) {
            key[i] = Double.MAX_VALUE;
            mstSet[i] = false;
        }

        // Always include first 1st vertex in MST.
        key[0] = 0;     // Make key 0 so that this vertex is
        // picked as first vertex
        parent[0] = -1; // First node is always root of MST

        // The MST will have V vertices
        for (int count = 0; count < size - 1; count++) {
            // Pick the minimum key vertex from the set of vertices
            // not yet included in MST
            int u = minKey(key, mstSet);

            // Add the picked vertex to the MST Set
            mstSet[u] = true;

            // Update key value and parent index of the adjacent
            // vertices of the picked vertex. Consider only those
            // vertices which are not yet included in MST
            for (int v = 0; v < size; v++)

                // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                if (graph[u][v] != Double.MAX_VALUE && mstSet[v] == false &&
                        graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
        }

        Pair<Integer,Integer>[] path = new Pair[size-1];
        for(int i=1;i<size;i++)
        {
                path[i-1] = new Pair<>(parent[i],i);
        }
        return path;
    }

        static private int minKey(double[] key, Boolean[] mstSet)
        {
            // Initialize min value
            double min = Double.MAX_VALUE;
            int min_index=-1;

            for (int v = 0; v < key.length; v++)
                if (mstSet[v] == false && key[v] < min)
                {
                    min = key[v];
                    min_index = v;
                }

            return min_index;
        }

        static private void checkConnected_undirected(double[][] graph)
        {
            boolean connected = false;

            if( BFS(graph,0).size() == graph.length)
                connected=true;

            for(int i = 0; i<graph.length;i++)
            {
                boolean undirected = false;
                for(int j=0;j<graph.length;j++)
                {
                    if(graph[i][j]==graph[j][i] && graph[i][j]!= Double.MAX_VALUE)
                        undirected=true;
                }
                if((!connected)||(!undirected))
                    throw new IllegalArgumentException("The graph has to be connected and undirected!");
            }
        }
    
    
}
