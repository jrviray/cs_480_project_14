import graph.*;
import java.io.*;
//Shortest path tree
//overrides newCost of GreedyGraph
public class SPT extends GreedyGraph
{
    SPT(String s) throws IOException
    {
        super(s);
    }
    public double newCost(int v, int w)
    {
        return costOf(v) + weightOf(new Edge(v, w));
    }
}