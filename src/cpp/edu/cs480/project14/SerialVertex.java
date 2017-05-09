/**
 * Copyright System.out, LLC
 */
package cpp.edu.cs480.project14;

import java.io.Serializable;

public class SerialVertex implements Serializable{
    private double x;
    private double y;
    private int ID;
    private String content;

    public SerialVertex(Vertex thisVertex)
    {
        ID = thisVertex.getID();
        x=thisVertex.getX();
        y=thisVertex.getY();
        content = thisVertex.getContent();

    }

    public Vertex deserialize()
    {
        return new Vertex(ID,content,x,y);
    }

}
