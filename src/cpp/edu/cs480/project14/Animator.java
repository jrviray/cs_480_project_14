package cpp.edu.cs480.project14;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

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
    
    private boolean isDirected;


    /**
     * This is a default constructor
     * @param mainPane
     */
    public Animator(Pane mainPane, HBox outputBox)
    {
        canvas=mainPane;
        outputLabel = new Label("");
        cancelButton = new Hyperlink("cancel selection");
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
    public Vertex getVertex(int ID) {
        Vertex ret = vertexTable[ID];
        return ret;
    }
    public Edge getEdge(int sourceID, int destID) {
        Edge ret = edgeTable[sourceID][destID];
        return ret;
    }
    private boolean isDragging = false;
    /**
     * This method is used to attach a drag listener and a click to a vertex
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


    /**
     * This method is used to add edge on the graph
     * If the user doesn't select a source and destination vertex,
     * addition will not operate
     */
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

    /**
     * This method is to delete edge from the graph
     */
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
                outputControl_no_source();
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
                        edgeTable[sourceChoice][i] = null;
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
        Alert alert = new Alert(Alert.AlertType.ERROR); // Moved this outside so i can use it else where
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
                isDirected = directedChoice.getValue().equals("directed edge")? true: false;

                try {
                    Double weight_result = Double.parseDouble(weight.getText());
                    if (weight_result < 0.0) { //checking if weight is less than 0, if it is, prompt an error message.
                    	alert.setTitle("Error");
                    	alert.setHeaderText("");
                    	alert.setContentText("A weight cannot be negative!");
                    	alert.showAndWait();
                    	return null;
                    } else  {
                    	return new Pair<> (weight_result,isDirected);
                    }
                   
                }
                catch (NumberFormatException | NullPointerException e)
                {
                   
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
    

    
    private int edgeCount() {
    	int edges = 0;
    	
    	for (int i  = 0; i < edgeTable.length; i++) {
    		for (int j = 0; j <edgeTable.length; j++) {
    			if (edgeTable[i][j] != null) {
    				edges++;
    			}
    		}
    	}
    	
    	return edges;
    	
    }


    /**
     * This method should be called whenever there is a change on vertices or edges
     */
    private void writeToFile()
    {
        try (PrintWriter writer = new PrintWriter("graph.txt", "UTF-8")) {
        	
        	
        	writer.println(vertexTable.length + " " + edgeCount()  + " " + (isDirected ? "directed" : "undirected"));

            for(int i=0;i<edgeTable.length;i++)
            {
                for(int j=0;j<edgeTable.length;j++)
                {
                    if(edgeTable[i][j] !=null)
                    {
                        writer.println(i  + " " + j + " " + edgeTable[i][j]);
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

    private void outputControl_no_source()
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

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source. Select the destination.");

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source. Please select a destination vertex by clicking " +
                "or click a button above to do an operation.");

        cancelButton.setVisible(true);
    }

    private void outputControl_select_dest()
    {

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source and "+vertexTable[destChoice].getContent() + " as destination. Click again to confirm");

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source and "+vertexTable[destChoice].getContent() + " as destination." +
                " Click a button above to do an operation.");

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

    public void saveGraph()
    {

        TextInputDialog save = new TextInputDialog();
        save.setTitle("Save Graph");
        save.setHeaderText(null);
        save.setContentText("Please enter the name you want to save as:");
        Optional<String> saveName = save.showAndWait();
        if(saveName.isPresent())
        {
            try {
                SaveInfo saveGraph = new SaveInfo(vertexTable,edgeTable);
                FileOutputStream fileSave = new FileOutputStream(saveName.get() + ".dat");
                ObjectOutputStream dataSave = new ObjectOutputStream(fileSave);
                dataSave.writeObject(saveGraph);
                dataSave.close();
                fileSave.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void loadGraph()
    {
        List<String> loadableFileName = new ArrayList<>();
        File curDir = new File(".");
        File[] allFile=curDir.listFiles();

        for(File x:allFile)
        {
            if(x.getName().endsWith(".dat"))
            {
                loadableFileName.add(x.getName().replace(".dat",""));
            }
        }

        if(loadableFileName.isEmpty())
        {
            Alert noFileFound = new Alert(Alert.AlertType.INFORMATION);
            noFileFound.setTitle("No saved file found");
            noFileFound.setHeaderText(null);
            noFileFound.setContentText("No saved file found!");
            noFileFound.showAndWait();
        }
        else
        {
            ChoiceDialog<String> load = new ChoiceDialog<>(loadableFileName.get(0),loadableFileName);
            load.setTitle("Load tree");

            load.setHeaderText(null);

            load.setHeaderText("Load");

            load.setContentText("Please select a tree that you want to load:");
            Optional<String> fileName = load.showAndWait();

            if(fileName.isPresent())
            {
                try {
                    //load the treeDataSave data;
                    FileInputStream fileSave = new FileInputStream(fileName.get() + ".dat");
                    ObjectInputStream dataSave = new ObjectInputStream(fileSave);
                    SaveInfo loadGraph = (SaveInfo) dataSave.readObject();
                    dataSave.close();
                    fileSave.close();
                    vertexTable = loadGraph.getVertexTable();
                    edgeTable = loadGraph.getEdgeTable(vertexTable);
                    redrawGraph();

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void redrawGraph()
    {
        canvas.getChildren().clear();
        for(int i=0;i<vertexTable.length;i++)
        {
            if(vertexTable[i]!=null) {
                drawOnCanvas(vertexTable[i]);
                attachListener(vertexTable[i]);
            }
        }
        for(int i=0;i<edgeTable.length;i++)
        {
            for(int j=0;j<vertexTable.length;j++)
            {
                if(edgeTable[i][j]!=null)
                    drawOnCanvas(edgeTable[i][j]);
            }
        }
    }
    private Circle createHighlightCircle()
    {
        Circle highlightCircle = new Circle(Vertex.RADIUS);
        highlightCircle.setFill(new Color(0,0,0,0));
        highlightCircle.setStroke(Vertex.HIGHLIGHT);
        highlightCircle.setStrokeType(StrokeType.OUTSIDE);
        highlightCircle.setStrokeWidth(5);
        return highlightCircle;
    }
//    private SequentialTransition searchAnimation(int[] path)
//    {
//        SequentialTransition mainAnimation = new SequentialTransition();
//        //initialize the highlight circle to the root node
//        Circle highlightCircle = createHighlightCircle();
//        //get the traversal animation
//        SequentialTransition traversalAnimation = highlightTraversal(highlightCircle,traversalStack);
//
//        //remove the highlight circle from the canvas
//        PauseTransition removeCircle = new PauseTransition(Duration.seconds(.5f));
//        removeCircle.setOnFinished(actionEvent->{deleteFromCanvas(highlightCircle);});
//        mainAnimation.getChildren().addAll(traversalAnimation,removeCircle);
//
//        return mainAnimation;
//
//    }
//
//        private SequentialTransition highlightTraversal(Circle circle,Stack<GraphicNode> path)
//    {
//        SequentialTransition mainAnimation = new SequentialTransition();
//        final GraphicNode startingNode=path.pop();
//        PauseTransition drawCircle = new PauseTransition(Duration.ONE);
//        circle.setTranslateX(startingNode.getX());
//        circle.setTranslateY(startingNode.getY());
//        drawCircle.setOnFinished(actionEvent->{drawOnCanvas(circle);});
//        mainAnimation.getChildren().add(drawCircle);
//        while(!path.isEmpty()) {
//
//            final GraphicNode nextNode = path.pop();
//            mainAnimation.getChildren().addAll(new PauseTransition(Duration.seconds(.5f)),
//                    //when reach a node, wait for a while
//                    this.movementTo(circle, nextNode.getX(), nextNode.getY(),null));
//                    //move to the next node
//        }
//        mainAnimation.getChildren().add(new PauseTransition(Duration.seconds(.5f)));
//            return  mainAnimation;
//    }
}
