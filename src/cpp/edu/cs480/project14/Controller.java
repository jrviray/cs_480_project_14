package cpp.edu.cs480.project14; /**
 * Created by wxy03 on 4/22/2017.
 */

import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Controller extends Application {


    public static final int BREADTH_FIRST_SEARCH=0;
    public static final int DEPTH_FIRST_SEARCH=1;
    public static final int GREEDY_SHORTEST_PATH = 3;
    public static final int DISJKSTRA_PATH=4;
    public static  final int MINIMUM_SPANNING_TREE=5;


    private Scene mainScene;
    private double mainPaneWidth = 1600;
    private double mainPaneHeight = 800;
    private Pane canvas;
    private Button addVertex,operate,save,load,addEdge,deleteEdge,deleteVertex,clear_output;
    private ChoiceBox algChoice;
    private int algorithmType;
    private Animator animator;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initialize();
        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void initialize()
    {
        BorderPane rootPane = new BorderPane();
        mainScene = new Scene(rootPane);

        canvas = new Pane();

        canvas.setPrefSize(mainPaneWidth,mainPaneHeight);
        rootPane.setCenter(canvas);




        HBox leftPane = new HBox(20f);
        leftPane.setPadding(new Insets(20f,20f,10f,20f));
        leftPane.setAlignment(Pos.BASELINE_LEFT);

        addVertex = new Button("add vertex");
        addVertex.setOnMouseClicked(event -> {animator.createVertex();});
        deleteVertex = new Button("delete vertex");
        deleteVertex.setOnMouseClicked(event -> {animator.deleteVertex();});
        addEdge = new Button("add edge");
        addEdge.setOnMouseClicked(event -> {animator.addEdge();});
        deleteEdge = new Button("delete edge");
        deleteEdge.setOnMouseClicked(event -> {animator.deleteEdge();});
        clear_output = new Button("clear output");
        clear_output.setDisable(true);
        clear_output.setOnMouseClicked(mouseEvent -> {animator.clearOutput();});



        algChoice = new ChoiceBox(FXCollections.observableArrayList("breadth-first search",
                "depth-first search",new Separator(), "greedy shortest path","Disjkstra's shortest path","minimum spanning tree"));
        algChoice.getSelectionModel().selectFirst();
        algorithmType = BREADTH_FIRST_SEARCH;

        algChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            algorithmType = newValue.intValue();
            System.out.println(algorithmType);
        });


        operate = new Button("operate algorithm");
        operate.setOnMouseClicked(event -> {animator.writeToFile();
            SequentialTransition mainAnimation = animator.searchAnimation(new int[]{0,1,2});
        mainAnimation.setOnFinished(actionEvent -> {clear_output.setDisable(false);});
        mainAnimation.play();});
        leftPane.getChildren().addAll(addVertex,deleteVertex,addEdge,deleteEdge,algChoice,operate,clear_output);
        


        HBox rightPane = new HBox(20f);
        rightPane.setPadding(new Insets(20f,20f,10f,20f));
        rightPane.setAlignment(Pos.BASELINE_RIGHT);
        save = new Button("save");
        save.setOnMouseClicked(event -> {animator.saveGraph();});
        load = new Button("load");
        load.setOnMouseClicked(event -> {animator.loadGraph();});
        rightPane.getChildren().addAll(save,load);

        HBox outputBox = new HBox();
        outputBox.setPadding(new Insets(0,0,5f,20f));
        BorderPane topPane = new BorderPane();
        topPane.setLeft(leftPane);
        topPane.setRight(rightPane);
        topPane.setBottom(outputBox);
        rootPane.setTop(topPane);

        Label copyrightLabel = new Label("Copyright (c) System.out, LLC Version 1.0 ");
        rootPane.setBottom(copyrightLabel);
        BorderPane.setAlignment(copyrightLabel,Pos.CENTER_RIGHT);

        animator = new Animator(canvas,outputBox);


    }
}







