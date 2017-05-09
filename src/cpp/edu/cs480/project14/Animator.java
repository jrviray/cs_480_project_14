/**
 * Copyright System.out, LLC
 */
package cpp.edu.cs480.project14;

import com.sun.javafx.binding.StringFormatter;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import java.io.*;
import java.util.*;

public class Animator {

    private final String  FILE_NAME= "graph.txt";

    private Pane canvas;


    private Vertex[] vertexTable;

    private Edge[][] edgeTable;

    private double offsetX;
    private double offsetY;

    private Label outputLabel;

    private Hyperlink cancelButton;

    private int sourceChoice;

    private int destChoice;
    
    private Boolean isDirected;



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
            Double result = getAddEdgeInfo();
            if(result!=null)
            {
                changeEdge(sourceChoice,destChoice,result);
                if(!isDirected)
                    changeEdge(destChoice,sourceChoice,result);
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
            if(!isDirected) {
                selectedEdge = edgeTable[destChoice][sourceChoice];
                deleteFromCanvas(selectedEdge);
                edgeTable[destChoice][sourceChoice] = null;
            }
            
            if(isEmpty(edgeTable)) isDirected = null;
            
            cancelSelection();
        }
    }
    
    private boolean isEmpty(Edge[][] table) {
    	boolean empty = true;
    	for (int i  = 0; i < table.length; i++) {
    		for (int j = 0; j < table.length; j++) {
    			if (table[i][j] != null) {
    				empty = false;
    			}
    		}
    	}
    	return empty;
    }



    private void changeEdge(int sourceID, int destID,double weight)
    {
        Edge oldEdge = edgeTable[sourceID][destID];
        if(oldEdge!=null)
        {
            oldEdge.setWeight(weight);
        }
        else {
            Edge newEdge = new Edge(vertexTable[sourceID], vertexTable[destID], weight, isDirected);
            edgeTable[sourceID][destID] = newEdge;
            drawOnCanvas(newEdge);
            newEdge.toBack();
        }
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

                    for(int i=sourceChoice;i<vertexTable.length-1;i++)
                    {
                        vertexTable[i]=vertexTable[i+1];
                        if(getVertex(i)!=null)
                        getVertex(i).setID(i);

                        for(int j = 0;j<vertexTable.length;j++)
                        {
                            edgeTable[i][j] = edgeTable[i+1][j];
                            edgeTable[j][i] = edgeTable[j][i+1];
                        }
                    }
                    cancelSelection();
                }
            }
    }

    /**
     * This is an aiding method for get input from the user to add edge
     * @return
     */
    private Double getAddEdgeInfo()
    {
        Dialog<Double> inputDialog = new Dialog<>();
        Alert alert = new Alert(Alert.AlertType.ERROR); // Moved this outside so i can use it else where
        inputDialog.setTitle("Add New Edge");
        VBox layoutBox = new VBox();
        layoutBox.setSpacing(10);
        layoutBox.setPadding(new Insets(20, 20, 10, 20));

        inputDialog.setHeaderText(null);
        TextField weight = new TextField();
        Label label = new Label("Enter the weight of edge between "+vertexTable[sourceChoice].getContent()+" and "+vertexTable[destChoice].getContent()+":");
        ChoiceBox directedChoice = new ChoiceBox();
        directedChoice.getItems().addAll("directed graph","undirected graph");
        directedChoice.getSelectionModel().selectFirst();

        if(isDirected!=null)
        {
            String indication = isDirected? "a directed" : "an undirected";
            Label directedLabel = new Label("This is "+indication+" graph.");
            layoutBox.getChildren().addAll(label,weight,directedLabel);
        }
        else {
            layoutBox.getChildren().addAll(label, weight, directedChoice);
        }
        inputDialog.getDialogPane().setContent(layoutBox);
        inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        inputDialog.setResultConverter(dialogButton->{
            if(dialogButton == ButtonType.OK)
            {
                if(isDirected==null)
                isDirected = directedChoice.getValue().equals("directed graph")? true: false;

                try {
                    Double weight_result = Double.parseDouble(weight.getText());
                    if (weight_result < 0.0) { //checking if weight is less than 0, if it is, prompt an error message.
                    	alert.setTitle("Error");
                    	alert.setHeaderText("");
                    	alert.setContentText("A weight cannot be negative!");
                    	alert.showAndWait();
                    	return null;
                    } else  {
                    	return weight_result;
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

        Optional<Double> result = inputDialog.showAndWait();
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
    
    private int vertexCount() {
    	int count = 0;
    	for (int i = 0; i < vertexTable.length; i++) {
    		if (vertexTable[i] != null) {
    			count++;
    		}
    	}
    	return count;
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
        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source, please select another destination vertex.");
        cancelButton.setVisible(true);
    }

    private void outputControl_select_source()
    {

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source, you can select another destination vertex " +
                "or click a button above to do an operation.");

        cancelButton.setVisible(true);
    }

    private void outputControl_select_dest()
    {

        outputLabel.setText("You selected "+vertexTable[sourceChoice].getContent()+ " as source and "+vertexTable[destChoice].getContent() + " as destination." +
                " Click a button above to do an operation.");

        cancelButton.setVisible(true);
    }

    private void outputControl_only_select_dest()
    {
        outputLabel.setText("please only se");
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
                SaveInfo saveGraph = new SaveInfo(vertexTable,edgeTable,isDirected);
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
                    this.isDirected = loadGraph.getIsDirected();
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
        cancelSelection();
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
                if(edgeTable[i][j]!=null) {
                    drawOnCanvas(edgeTable[i][j]);
                    edgeTable[i][j].toBack();
                }
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



    public Animation makeAlgorithm(int algType) throws IOException
    {
        switch (algType)
        {
            case Controller.BREADTH_FIRST_SEARCH:
                if(sourceChoice==-1) {
                    outputControl_no_source();
                    return null;
                }
                else
             return BFSAnimation(GraphAlgorithm.BFS(writeToArrayGraph(),sourceChoice));

            case Controller.DEPTH_FIRST_SEARCH:
                if(sourceChoice==-1) {
                    outputControl_no_source();
                    return null;
                }
                else
                    return DFSAnimation(GraphAlgorithm.DFS(writeToArrayGraph(),sourceChoice));

            case Controller.DIJKSTRA_PATH:
                if(sourceChoice==-1 && destChoice==-1)
                {
                    outputControl_no_source();
                    return null;
                }
                else
                {
                    stageTable(GraphAlgorithm.DijkstrasDistance(writeToArrayGraph(),sourceChoice));
                    return dijkstraAnimation(GraphAlgorithm.Dijkstras(writeToArrayGraph(),sourceChoice));

                }

            case Controller.GREEDY_SHORTEST_PATH:
                if(sourceChoice==-1)
                {
                    outputControl_no_source();
                    return null;
                }
                else if (destChoice == -1)
                {
                    outputControl_no_dest();
                    return null;
                }

                else{
                    try{
                        return greedyAnimation(GraphAlgorithm.GreedyNonOptimal(writeToArrayGraph(), sourceChoice, destChoice));
                    } catch(IllegalArgumentException e) {
                        cancelSelection();
                        outputLabel.setText(e.getMessage());
                        return null;
                    }
                    
                }

            case Controller.MINIMUM_SPANNING_TREE:
                    try {
                        return MSTAnimation(GraphAlgorithm.MST_undirected(writeToArrayGraph()));
                    }catch (IllegalArgumentException e)
                    {
                        cancelSelection();
                        outputLabel.setText(e.getMessage());
                        return null;
                    }

            default:
                return null;
        }
    }



    private void lockAllVertex()
    {
        for(int i =0;i<vertexTable.length;i++)
        {
            if(getVertex(i)!=null)
            {
                getVertex(i).setOnMouseDragged(null);
                getVertex(i).setOnMousePressed(null);
                getVertex(i).setOnMouseReleased(null);
            }
        }
    }

    private void unlockAllVertex()
    {

        for(int i =0;i<vertexTable.length;i++)
        {
            if(getVertex(i)!=null)
            {
                attachListener(getVertex(i));
            }
        }
    }


    /**
     * This animation is for BFS
     * @param path
     * @return
     */
    private SequentialTransition BFSAnimation(ArrayList<Integer> path)
    {
        cancelSelection();
        Circle highlightCircle = createHighlightCircle();
        SequentialTransition mainAnimation = new SequentialTransition();

        int thisVertexID = path.get(0);
        Vertex thisVertex = getVertex(thisVertexID);
        thisVertex.highLightCircle();
        highlightCircle.setTranslateX(thisVertex.getX());
        highlightCircle.setTranslateY(thisVertex.getY());
        drawOnCanvas(highlightCircle);

        for(int i = 0; i<path.size();i++)
        {
            thisVertexID = path.get(i);
            SequentialTransition thisVertexTransition = new SequentialTransition();
            thisVertexTransition.getChildren().add(new PauseTransition(Duration.seconds(.5f)));
            for(int j = 0;j<edgeTable.length;j++)
            {
                int destID = j;
                Edge thisEdge = getEdge(thisVertexID,j);
                if(thisEdge!=null)
                {
                    PauseTransition temp_one = new PauseTransition(Duration.seconds(.5f));
                    temp_one.setOnFinished(event -> {thisEdge.highLightEdge();
                    thisEdge.toFront();
                    getVertex(destID).highLightCircle();
                            });
                    PauseTransition temp_two = new PauseTransition(Duration.seconds(1f));
                    temp_two.setOnFinished(event -> thisEdge.unhighLightEdge());
                    thisVertexTransition.getChildren().addAll(temp_one,temp_two);
                }
            }

            if(i!=(path.size()-1))
            {
                Vertex nextVertex = getVertex(path.get(i+1));

            thisVertexTransition.setOnFinished(event -> {
                highlightCircle.setTranslateX(nextVertex.getX());
                highlightCircle.setTranslateY(nextVertex.getY());
                nextVertex.highLightCircle();

            });
            }
            else{
                thisVertexTransition.setOnFinished(event -> {
                    deleteFromCanvas(highlightCircle);
                });
            }
            mainAnimation.getChildren().add(thisVertexTransition);
        }
        return mainAnimation;
    }

    public SequentialTransition DFSAnimation(ArrayList<Integer> path)
    {
        cancelSelection();
        SequentialTransition mainAnimation = new SequentialTransition();
        //initialize the highlight circle to the root node
        Circle highlightCircle = createHighlightCircle();
        //get the traversal animation

        Integer[] pathArray = new Integer[path.size()];
        path.toArray(pathArray);
        SequentialTransition traversalAnimation = highlightTraversal(highlightCircle, pathArray);
        //remove the highlight circle from the canvas
        PauseTransition removeCircle = new PauseTransition(Duration.ONE);
        removeCircle.setOnFinished(actionEvent -> {
            deleteFromCanvas(highlightCircle);
        });
        mainAnimation.getChildren().addAll(traversalAnimation, removeCircle);
        return mainAnimation;
    }

    private SequentialTransition highlightTraversal(Circle circle, Integer[] path)
    {
        SequentialTransition mainAnimation = new SequentialTransition();
        final Vertex startingVertex = getVertex(path[0]);
        startingVertex.highLightCircle();
        PauseTransition drawCircle = new PauseTransition(Duration.ONE);
        
        circle.setTranslateX(startingVertex.getX());
        circle.setTranslateY(startingVertex.getY());
        drawCircle.setOnFinished(actionEvent->{drawOnCanvas(circle);
        lockAllVertex();});
        mainAnimation.getChildren().add(drawCircle);

        for (int i = 1; i < path.length; i++) {
            Vertex nextVertex = getVertex(path[i]);
            mainAnimation.getChildren().addAll(new PauseTransition(Duration.seconds(.5f)),
                    this.movementTo(circle, nextVertex.getX(), nextVertex.getY(), event -> {nextVertex.highLightCircle();})); 
        }
        mainAnimation.getChildren().add(new PauseTransition(Duration.seconds(.5f)));
        mainAnimation.setOnFinished(event->{unlockAllVertex();});
        return mainAnimation;
    }
    
    
    private TranslateTransition movementTo(Circle target, double x, double y, EventHandler<ActionEvent> onFinish)
    {
        TranslateTransition movementTo = new TranslateTransition(Duration.seconds(1),target);
        movementTo.setToX(x);
        movementTo.setToY(y);
        movementTo.setOnFinished(onFinish);
        return movementTo;
    }

    public void clearOutput()
    {
        for(Vertex thisVertex: vertexTable)
        {
            if(thisVertex!=null && thisVertex.getID()!=sourceChoice && thisVertex.getID()!= destChoice)
                thisVertex.unhighLightCircle();
        }
        for(Edge[] iEdge:edgeTable)
        {
            for(Edge jEdge:iEdge)
            {
                if(jEdge!=null)
                    jEdge.unhighLightEdge();
            }
        }
    }

    private PauseTransition MSTAnimation(Pair<Integer,Integer>[] path)
    {

        cancelSelection();
        if(path==null)
            return null;
        else {
            PauseTransition mainAnimation = new PauseTransition(Duration.ONE);
            for (Pair<Integer, Integer> thisEdge : path) {
                if (thisEdge != null) {
                    int sourceID = thisEdge.getKey();
                    int destID = thisEdge.getValue();
                    getEdge(sourceID, destID).highLightEdge();
                    getEdge(sourceID, destID).toFront();
                    getVertex(sourceID).highLightCircle();
                    getVertex(destID).highLightCircle();
                }
            }
            return mainAnimation;
        }
    }

    private PauseTransition greedyAnimation(ArrayList<Integer> path)
    {


        if(path==null) {
            cancelSelection();
            return null;
        }
        else {

            PauseTransition mainAnimation = new PauseTransition(Duration.ONE);
            cancelSelection();
            for (int i = 0; i < path.size() - 1; i++) {

                int sourceID = path.get(i);
                int destID = path.get(i+1);
                getEdge(sourceID, destID).highLightEdge();
                getEdge(sourceID, destID).toFront();
                getVertex(sourceID).highLightCircle();
                getVertex(destID).highLightCircle();
            }
            return mainAnimation;
        }
    }

    public void resetGraph()
    {
        cancelSelection();
        canvas.getChildren().clear();
        vertexTable = new Vertex[2];
        edgeTable = new Edge[2][2];
        isDirected = null;

    }

    private double[][] writeToArrayGraph()
    {
        int size = vertexCount();
        double[][] newTable = new double[size][size];
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(getEdge(i,j)==null)
                    newTable[i][j] = Double.MAX_VALUE;
                else
                    newTable[i][j] = getEdge(i,j).getWeight();
            }
        }
        return newTable;
    }

    private PauseTransition dijkstraAnimation(ArrayList<Integer> path)
    {
        cancelSelection();

            PauseTransition mainAnimation = new PauseTransition(Duration.ONE);
            for (int destID = 0;destID<path.size();destID++) {
                if (path.get(destID) != -1) {

                    int sourceID = path.get(destID);
                    getEdge(sourceID, destID).highLightEdge();
                    getEdge(sourceID, destID).toFront();
                    getVertex(sourceID).highLightCircle();
                    getVertex(destID).highLightCircle();
                }
            }
            return mainAnimation;

    }

    private void stageTable( double[] distanceList) {


        ObservableList<Pair<String,String>> distanceData = FXCollections.observableArrayList();

        for(int i=0;i<distanceList.length;i++)
        {
            String double_format;
            double value=distanceList[i];
            if(value == Double.MAX_VALUE)
                double_format = "No Path";
            else if(value==0)
                double_format = "source";
            else if(value == (long)value)
                double_format = String.format("%d",(long)value);
            else
                double_format = String.format("%f",value);
            Pair<String,String> this_entry = new Pair<>(getVertex(i).getContent(),double_format);
            distanceData.add(this_entry);
        }
        Stage outTable = new Stage();
        outTable.setWidth(300);
        outTable.setHeight(500);

        //construct the table
        TableView table = new TableView<>();

        outTable.setTitle("Dijkstra's Path Table");
        table.setEditable(false);

        TableColumn destCol = new TableColumn("Destination");
        destCol.setCellValueFactory(new PropertyValueFactory<Pair<SimpleStringProperty,SimpleStringProperty>, String>("key"));
        TableColumn distCol = new TableColumn("Distance");

        table.getColumns().addAll(destCol,distCol);
        distCol.setCellValueFactory(new PropertyValueFactory<Pair<SimpleStringProperty,SimpleStringProperty>, String>("value"));

       table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       table.setItems(distanceData);

        StackPane pane = new StackPane(table);
        Scene scene = new Scene(pane);



        outTable.setScene(scene);
        outTable.show();

    }
}
