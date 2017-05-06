package cpp.edu.cs480.project14;

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


}
