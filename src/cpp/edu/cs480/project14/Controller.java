package cpp.edu.cs480.project14; /**
 * Created by wxy03 on 4/22/2017.
 */

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Controller extends Application {


    public static final int BREADTH_FIRST_SEARCH=0;
    public static final int DEPTH_FIRST_SEARCH=1;
    public static  final int PRE_ORDER_TRAVERSAL=3;
    public static final int IN_ORDER_TRAVERSAL=4;
    public static final int POST_ORDER_TRAVERSAL=5;
    public static final int GREDDY_SHORTEST_PATH=7;
    public static final int DISJKSTRA_PATH=8;
    public static  final int MINIMUM_SPANNING_TREE=9;


    private Scene mainScene;
    private double mainPaneWidth = 1600;
    private double mainPaneHeight = 800;
    private Pane edgePane;
    private Pane vertexPane;
    private Button add,delete,operate,save,load;
    private ChoiceBox algChoice;
    private int algorithmType;
    private Label outputLabel;

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

        edgePane = new Pane();
        vertexPane = new Pane();
        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(mainPaneWidth,mainPaneHeight);
        stackPane.getChildren().addAll(edgePane,vertexPane);
        vertexPane.toFront();
        rootPane.setCenter(stackPane);


        HBox leftPane = new HBox(20f);
        leftPane.setPadding(new Insets(20f,20f,10f,20f));
        leftPane.setAlignment(Pos.BASELINE_LEFT);

        add = new Button("add vertex");
        delete = new Button("delete vertex");
        algChoice = new ChoiceBox(FXCollections.observableArrayList("breadth-first search",
                "depth-first search",new Separator(),"pre-order traversal","in-order traversal","post-order traversal",
                new Separator(),"greedy shortest path","Disjkstra's shortest path","minimum spanning tree"));
        algChoice.getSelectionModel().selectFirst();
        algorithmType = BREADTH_FIRST_SEARCH;

        algChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                algorithmType = newValue.intValue();
                System.out.println(algorithmType);
            }
        });


        operate = new Button("operate algorithm");
        leftPane.getChildren().addAll(add,delete,algChoice,operate);


        HBox rightPane = new HBox(20f);
        rightPane.setPadding(new Insets(20f,20f,10f,20f));
        rightPane.setAlignment(Pos.BASELINE_RIGHT);
        save = new Button("save");
        load = new Button("load");
        rightPane.getChildren().addAll(save,load);

        outputLabel = new Label("Here is output message");
        outputLabel.setPadding(new Insets(0,0,20f,20f));
        BorderPane topPane = new BorderPane();
        topPane.setLeft(leftPane);
        topPane.setRight(rightPane);
        topPane.setBottom(outputLabel);
        rootPane.setTop(topPane);

        Label copyrightLabel = new Label("Copyright (c) System.out, LLC Version 1.0 ");
        rootPane.setBottom(copyrightLabel);
        BorderPane.setAlignment(copyrightLabel,Pos.CENTER_RIGHT);



    }

}
