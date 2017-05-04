package cpp.edu.cs480.project14;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by wxy03 on 4/24/2017.
 */
public class Animator {

    private Pane canvas;

    private Vertex[] vertexTable;

    private Edge[][] edgeTable;

    private double offsetX;
    private double offsetY;

    private Label outputLabel;

    private Hyperlink cancelButton;

    private int sourceChoice;

    private int destChoice;


    /**
     * This is a default constructor
     * @param mainPane
     */
    public Animator(Pane mainPane, HBox outputBox)
    {
        canvas=mainPane;
        outputLabel = new Label("");
        cancelButton = new Hyperlink("cancel");
        outputBox.getChildren().addAll(outputLabel,cancelButton);
        outputBox.setAlignment(Pos.BASELINE_LEFT);
        cancelButton.setVisible(false);
        cancelButton.setOnMouseClicked(event -> {cancelSelection();});
        sourceChoice = -1;
        destChoice = -1;
        vertexTable = new Vertex[2];
        edgeTable = new Edge[2][2];
    }

    /**
     * This method is used to create a new vertex on the canvas
     */
    public void createVertex()
    {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Add New Vertex");
        inputDialog.setContentText("Enter the name of the new vertex:");
        inputDialog.setHeaderText(null);


        Optional<String> name = inputDialog.showAndWait();
        if(name.isPresent())
        {
            String content = name.get();
            int ID;
            for(ID=0;ID<vertexTable.length;ID++)
            {
                if(vertexTable[ID]==null)
                    break;
            }

            if(ID==vertexTable.length-1)    //expand the vertex table and edge table
            {
                vertexTable = Arrays.copyOf(vertexTable, vertexTable.length * 2);
                Edge[][] newEdgeTable = new Edge[vertexTable.length * 2][vertexTable.length * 2];
                for (int i = 0; i < edgeTable.length; i++) {
                    for (int j = 0; j < edgeTable.length; j++) {
                        newEdgeTable[i][j] = edgeTable[i][j];
                    }
                }
                edgeTable = newEdgeTable;
            }
                //set the vertex to be able to drag around the canvas
                Vertex newVertex = new Vertex(ID,content);
                vertexTable[ID] = newVertex;
                attachListener(newVertex);
                drawOnCanvas(newVertex);
        }

    }


    private boolean isDragging = false;
    /**
     * This method is used to attach a drag listener to a vertex
     * @param vertex
     */
    private void attachListener(Vertex vertex)
    {


        vertex.setOnMousePressed(e->{offsetX = e.getSceneX() -vertex.getX();
            offsetY = e.getSceneY() - vertex.getY();});
        vertex.setOnMouseDragged(e -> {
            isDragging = true;
            double newX = e.getSceneX() - offsetX;
            double newY = e.getSceneY() - offsetY;
            if(newY>Vertex.RADIUS && newY<canvas.getBoundsInLocal().getHeight()-Vertex.RADIUS)
                vertex.setY(newY);
            if(newX>Vertex.RADIUS && newX<canvas.getBoundsInLocal().getWidth()-Vertex.RADIUS)
                vertex.setX(newX);
        });

        vertex.setOnMouseReleased(event -> {
            if (isDragging != true) {
                if (sourceChoice == -1) {
                    sourceChoice = vertex.getID();
                    vertex.highLightCircle();
                    outputControl_select_source();
                } else if (sourceChoice != -1 && destChoice == -1) {
                    if (vertex.getID() != sourceChoice) {
                        vertex.highLightCircle();
                        destChoice = vertex.getID();
                        outputControl_select_dest();
                    }
                }
            }
            isDragging = false;
        });
    }


    public void addEdge()
    {
        if(sourceChoice==-1 && destChoice == -1)
            outputControl_no_source_dest();
        else if(destChoice == -1)
            outputControl_no_dest();
        else
        {
            Pair<Double,Boolean> result = getAddEdgeInfo();
            if(result!=null)
            {
                changeEdge(sourceChoice,destChoice,result.getKey());
                if(!result.getValue())
                    changeEdge(destChoice,sourceChoice,result.getKey());
                cancelSelection();
            }
        }
    }

    public void deleteEdge()
    {
        if(sourceChoice==-1 && destChoice == -1)
            outputControl_no_source_dest();
        else if(destChoice == -1)
            outputControl_no_dest();
        else
        {
            Edge selectedEdge = edgeTable[sourceChoice][destChoice];
            deleteFromCanvas(selectedEdge);
            edgeTable[sourceChoice][destChoice] = null;
            selectedEdge = edgeTable[destChoice][sourceChoice];
            deleteFromCanvas(selectedEdge);
            edgeTable[destChoice][sourceChoice] = null;
            cancelSelection();
        }
    }



    private void changeEdge(int sourceID, int destID,double weight)
    {
        Edge oldEdge = edgeTable[sourceID][destID];
        if(oldEdge!=null)
        {
            deleteFromCanvas(oldEdge);
        }
        Edge newEdge =  new Edge(vertexTable[sourceID],vertexTable[destID],weight);
        edgeTable[sourceID][destID] = newEdge;
        drawOnCanvas(newEdge);
        newEdge.toBack();
    }



    public void deleteVertex()
    {
            if(sourceChoice==-1)
                outputControl_no_soruce();
            else
            {
                Vertex deleteVertex = vertexTable[sourceChoice];
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation");
                confirmation.setHeaderText("");
                confirmation.setContentText("Are you sure to delete vertex "+deleteVertex.getContent()+"?"+
                "\nNote: all the edges related to this vertex will be deleted!");
                Optional<ButtonType> result=confirmation.showAndWait();
                if(result.get()==ButtonType.OK)
                {
                    deleteFromCanvas(vertexTable[sourceChoice]);
                    vertexTable[sourceChoice] = null;
                    for(int i =0; i<edgeTable.length;i++)
                    {
                        Edge edge = edgeTable[i][sourceChoice];
                        deleteFromCanvas(edge);
                        edgeTable[i][sourceChoice] = null;
                        edge = edgeTable[sourceChoice][i];
                        deleteFromCanvas(edge);
                        edgeTable[i][sourceChoice] = null;
                    }
                    cancelSelection();
                }
            }
    }

    /**
     * This is an aiding method for get input from the user to add edge
     * @return
     */
    private Pair<Double,Boolean> getAddEdgeInfo()
    {
        Dialog<Pair<Double,Boolean>> inputDialog = new Dialog<>();
        inputDialog.setTitle("Add New Edge");
        VBox layoutBox = new VBox();
        layoutBox.setSpacing(10);
        layoutBox.setPadding(new Insets(20, 20, 10, 20));

        inputDialog.setHeaderText(null);
        TextField weight = new TextField();
        Label label = new Label("Enter the weight of edge between "+vertexTable[sourceChoice].getContent()+" and "+vertexTable[destChoice].getContent()+":");
        ChoiceBox directedChoice = new ChoiceBox();
        directedChoice.getItems().addAll("directed edge","undirected edge");
        directedChoice.getSelectionModel().selectFirst();
        layoutBox.getChildren().addAll(label,weight,directedChoice);
        inputDialog.getDialogPane().setContent(layoutBox);
        inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        inputDialog.setResultConverter(dialogButton->{
            if(dialogButton == ButtonType.OK)
            {
                Boolean isDirected = directedChoice.getValue().equals("directed edge")? true: false;

                try {
                    Double weight_result = Double.parseDouble(weight.getText());
                    return new Pair<> (weight_result,isDirected);
                }
                catch (NumberFormatException | NullPointerException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("");
                    alert.setContentText("Invalid input!");
                    alert.showAndWait();
                    return null;
                }

            }
            else
                return null;
        });

        Optional<Pair<Double,Boolean>> result = inputDialog.showAndWait();
        return result.isPresent()?result.get():null;
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

    private void outputControl_no_soruce()
    {
            outputLabel.setText("Please select a source vertex by clicking.");
    }

    private void outputControl_no_source_dest()
    {
        outputLabel.setText("Please select a source vertex and a destination vertex by clicking.");
    }

    private void outputControl_no_dest()
    {
        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source, please select a destination vertex by clicking.");
        cancelButton.setVisible(true);
    }

    private void outputControl_select_source()
    {
        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source.");
        cancelButton.setVisible(true);
    }

    private void outputControl_select_dest()
    {
        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source and "+vertexTable[destChoice].getContent() + " as destination.");
        cancelButton.setVisible(true);
    }

    private void cancelSelection()
    {
        outputLabel.setText("");
        if(sourceChoice!=-1) {
            if(vertexTable[sourceChoice]!=null)
            vertexTable[sourceChoice].unhighLightCircle();
        }
        if(destChoice!=-1) {
            if(vertexTable[destChoice]!=null)
            vertexTable[destChoice].unhighLightCircle();
        }

        sourceChoice = -1;
        destChoice = -1;
        cancelButton.setVisible(false);

    }


    private void drawOnCanvas(Node... elements)
    {
        canvas.getChildren().addAll(elements);
    }

    private void deleteFromCanvas(Node... elements) {canvas.getChildren().removeAll(elements);}
}
