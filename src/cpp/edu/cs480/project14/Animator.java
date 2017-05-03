package cpp.edu.cs480.project14;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Arrays;

/**
 * Created by wxy03 on 4/24/2017.
 */
public class Animator {

    private Pane canvas;

    private Vertex[] vertexTable;

    private Edge[][] edgeTable;

    double offsetX;
    double offsetY;

    public Animator(Pane mainPane)
    {
        canvas=mainPane;
        vertexTable = new Vertex[10];
        edgeTable = new Edge[10][10];
    }

    public void createVertex(int vertexID, String context)
    {
        Vertex newVertex = new Vertex(vertexID,context);
        if(vertexID==vertexTable.length) {
            vertexTable = Arrays.copyOf(vertexTable, vertexTable.length * 2);

            Edge[][] newEdgeTable = new Edge[edgeTable.length*2][edgeTable.length*2];
            for(int i=0;i<edgeTable.length;i++)
            {
                for(int j=0;j<edgeTable.length;j++)
                {
                    newEdgeTable[i][j]=edgeTable[i][j];
                }
            }
        }

        vertexTable[vertexID] = newVertex;
        newVertex.setOnMousePressed(e -> {
            offsetX = e.getSceneX() - newVertex.getX();
            offsetY = e.getSceneY() - newVertex.getY();
        });

        newVertex.setOnMouseDragged(e -> {
            double newX = e.getSceneX() - offsetX;
            double newY = e.getSceneY() - offsetY;
            if(newY>Vertex.RADIUS && newY<canvas.getBoundsInLocal().getHeight()-Vertex.RADIUS)
                newVertex.setY(newY);
            if(newX>Vertex.RADIUS && newX<canvas.getBoundsInLocal().getWidth()-Vertex.RADIUS)
                newVertex.setX(newX);
        });
        drawOnCanvas(newVertex);


    }

    private void drawOnCanvas(Node... elements)
    {
        canvas.getChildren().addAll(elements);
    }
}
