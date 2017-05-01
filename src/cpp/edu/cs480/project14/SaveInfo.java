package cpp.edu.cs480.project14;

import java.io.Serializable;

/**
 * Created by wxy03 on 4/30/2017.
 */
public class SaveInfo implements Serializable {

    SerialVertex[] serialVertexTable;

    double[][] serialEdgeTable;

    public SaveInfo(Vertex[] vertexTable,Edge[][] edgeTable)
    {
        //copy the vertex table
            serialVertexTable = new SerialVertex[vertexTable.length];
            for(int i =0;i<serialVertexTable.length;i++)
            {
                if(vertexTable[i]!=null)
                serialVertexTable[i] = new SerialVertex(vertexTable[i]);
            }

        //copy the edge table
        serialEdgeTable = new double[edgeTable.length][edgeTable.length];
            for(int i =0;i<edgeTable.length;i++)
            {
                for(int j =0;j<edgeTable.length;j++)
                {
                    if(edgeTable[i][j]!=null)
                        serialEdgeTable[i][j] = edgeTable[i][j].getWeight();
                    else
                        serialEdgeTable[i][j] = 0;
                }
            }
    }

    public Vertex[] getVertexTable()
    {
        Vertex[] vertexTable = new Vertex[serialVertexTable.length];
        for(int i=0;i<vertexTable.length;i++)
        {
            if(serialVertexTable[i]!=null)
                vertexTable[i] = serialVertexTable[i].deserialize();
        }
        return vertexTable;
    }

    public Edge[][] getEdgeTable()
    {
        Vertex[] vertexTable = getVertexTable();
        Edge[][] edgeTable = new Edge[serialEdgeTable.length][serialEdgeTable.length];
        for(int i =0;i<edgeTable.length;i++)
        {
            for(int j =0;j<edgeTable.length;j++)
            {
                if(serialEdgeTable[i][j]!=0)
                   edgeTable[i][j] = new Edge(vertexTable[i],vertexTable[j],serialEdgeTable[i][j]);
            }
        }
        return edgeTable;
    }
}
