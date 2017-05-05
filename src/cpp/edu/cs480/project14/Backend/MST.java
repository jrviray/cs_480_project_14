import graph.*;
import java.io.*;
//Minimum spanning tree
//overrides newCost of GreedyGraph
public class MST extends GreedyGraph
{
    MST(String s) throws IOException
    {
        super(s);
    }
    public double newCost(int v, int w)
    {
        return weightOf(new Edge(v, w));
    }
}