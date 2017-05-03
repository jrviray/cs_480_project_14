package cpp.edu.cs480.project14;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by wxy03 on 4/24/2017.
 */
public class Animator {

    private Pane canvas;

    private Vertex[] vertexTable;

    private Edge[][] edgeTable;

    private double offsetX;
    private double offsetY;


    /**
     * This is a default constructor
     * @param mainPane
     */
    public Animator(Pane mainPane)
    {
        canvas=mainPane;
        vertexTable = new Vertex[2];
        edgeTable = new Edge[2][2];
    }

    /**
     * This method is used to create a new vertex on the canvas
     * @param context
     */
    public void createVertex( String context)
    {
        int ID;
        for(ID=0;ID<vertexTable.length;ID++)
        {
            if(vertexTable[ID]==null)
                break;
        }

        if(ID==vertexTable.length-1)    //expand the vertex table and edge table
        {
            vertexTable = Arrays.copyOf(vertexTable, vertexTable.length*2);
            Edge[][] newEdgeTable = new Edge[vertexTable.length*2][vertexTable.length*2];
            for(int i=0;i<edgeTable.length;i++)
            {
                for(int j=0;j<edgeTable.length;j++)
                {
                    newEdgeTable[i][j]=edgeTable[i][j];
                }
            }
            edgeTable=newEdgeTable;
        }

        //set the vertex to be able to drag around the canvas
        Vertex newVertex = new Vertex(ID,context);
        vertexTable[ID] = newVertex;
        attachDragListener(newVertex);
        
      
        
        drawOnCanvas(newVertex);
       
        

        System.out.println(newVertex.getXProperty());
        
        
    }

    /**
     * This method is used to attach a drag listener to a vertex
     * @param vertex
     */
    private void attachDragListener(Vertex vertex)
    {
        vertex.setOnMousePressed(e -> {
            offsetX = e.getSceneX() -vertex.getX();
            offsetY = e.getSceneY() - vertex.getY();
        });
        vertex.setOnMouseDragged(e -> {
            double newX = e.getSceneX() - offsetX;
            double newY = e.getSceneY() - offsetY;
            if(newY>Vertex.RADIUS && newY<canvas.getBoundsInLocal().getHeight()-Vertex.RADIUS)
                vertex.setY(newY);
            if(newX>Vertex.RADIUS && newX<canvas.getBoundsInLocal().getWidth()-Vertex.RADIUS)
                vertex.setX(newX);
        });
    }

    
    public void edge() {
    	System.out.println("this is working");
    	Vertex src = vertexTable[0];
    	Vertex des = vertexTable[1];
    	
    	Line line = new Line();
    	
    	line.setStrokeWidth(2);
    	line.setStroke(Color.BLACK);
 	   
 	   line.startXProperty().bind(src.getXProperty());
 	   line.startYProperty().bind(src.getYProperty());
 	   line.endXProperty().bind(des.getXProperty());
 	   line.endYProperty().bind(des.getYProperty());
 	   
 	   canvas.getChildren().add(line);
 	   
 	   
 	  System.out.println("this is finishing");
    	
    }
    

   public void drawEdge(Vertex src, Vertex des) {
	   Line line = new Line();
	   
	   line.startXProperty().bind(src.getXProperty().add(src.translateXProperty()));
	   line.startYProperty().bind(src.getYProperty().add(src.translateYProperty()));
	   line.endXProperty().bind(des.getXProperty().add(des.translateXProperty()));
	   line.endXProperty().bind(des.getYProperty().add(des.translateYProperty()));
   }

    /**
     * This method should be called whenever there is a change on vertices or edges
     */
    private void writeToFile()
    {
        try (PrintWriter writer = new PrintWriter("graph.txt", "UTF-8")) {

            for(int i=0;i<edgeTable.length;i++)
            {
                for(int j=0;j<edgeTable.length;j++)
                {
                    if(edgeTable[i][j] !=null)
                    {
                        //write
                    }
                }
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }



    private void drawOnCanvas(Node... elements)
    {
        canvas.getChildren().addAll(elements);
    }
}
