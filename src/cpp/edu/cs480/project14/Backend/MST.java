package cpp.edu.cs480.project14.Backend;
import cpp.edu.cs480.project14.Backend.graph.*;

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