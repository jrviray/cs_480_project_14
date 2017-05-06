package cpp.edu.cs480.project14;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 * Created by wxy03 on 4/23/2017.
 */
public class Edge extends Group{

    private Line edgeLine;
    private Polygon arrow;
    private Text weightText;
    private double weight;
    private boolean directed;

    public Edge(Vertex source,Vertex destination, double weight,boolean directed)
    {
        this.weight = weight;
        this.directed=directed;
        arrow = new Polygon();


        DoubleBinding lengthBinding = new DoubleBinding() {

            {
                super.bind(source.getXProperty(),source.getYProperty(),destination.getXProperty(),destination.getYProperty());
            }
            @Override
            protected double computeValue() {

                double sourceX = source.getX();
                double sourceY = source.getY();
                double deltaX = destination.getX() - sourceX;
                double deltaY = destination.getY() - sourceY;
                double length = Math.sqrt(deltaX*deltaX+deltaY*deltaY)-2*Vertex.RADIUS;
                if(length<0)
                    length=0;
                if(length==0)
                    arrow.getPoints().clear();
                else
                    arrow.getPoints().setAll(sourceX + length + Vertex.RADIUS, sourceY,
                            sourceX + Vertex.RADIUS + length - 10, sourceY + 5,
                            sourceX + Vertex.RADIUS + length - 5, sourceY,
                            sourceX + Vertex.RADIUS + length - 10, sourceY - 5);
                return length;
            }
        };

        DoubleBinding angleBinding = new DoubleBinding() {
            {
                super.bind(source.getXProperty(),source.getYProperty(),destination.getXProperty(),destination.getYProperty());
            }
            @Override
            protected double computeValue() {
                double sourceX = source.getX();
                double sourceY = source.getY();
                double destX = destination.getX();
                double destY = destination.getY();
                double deltaX = destX - sourceX;
                double deltaY = destY - sourceY;

                if(deltaX==0)
                {
                    if(sourceY>destY)
                        return 270;
                    else
                        return 90;
                }
                else
                {
                    double angle = Math.toDegrees(Math.atan(deltaY/deltaX));
                    if(sourceX<destX) {
                        weightText.setRotate(0);
                        return angle;
                    }
                    else {
                        weightText.setRotate(180);
                        return angle + 180;
                    }
                }
            }
        };

        edgeLine = new Line();

        edgeLine.startXProperty().bind(source.getXProperty().add(Vertex.RADIUS));
        edgeLine.startYProperty().bind(source.getYProperty());
        edgeLine.endYProperty().bind(source.getYProperty());
        edgeLine.endXProperty().bind(source.getXProperty().add(Vertex.RADIUS).add(lengthBinding));

        weightText = new Text(Double.toString(weight));
        weightText.setFont(Font.font(15));
        weightText.translateXProperty().bind(source.getXProperty().add(lengthBinding.divide(2)));
        weightText.translateYProperty().bind(source.getYProperty().subtract(5));

        Rotate rotate = new Rotate();
        rotate.angleProperty().bind(angleBinding);
        rotate.pivotXProperty().bind(source.getXProperty());
        rotate.pivotYProperty().bind(source.getYProperty());
        this.getTransforms().add(rotate);
        if(directed)
        getChildren().addAll(edgeLine,arrow,weightText);
        else
            getChildren().addAll(edgeLine,weightText);

    }

    public double getWeight()
{
    return weight;
}

    // highlight edge
    public void highLightEdge(){

        edgeLine.setStroke(Vertex.HIGHLIGHT);
        edgeLine.setStrokeWidth(3f);
        arrow.setFill(Vertex.HIGHLIGHT);
}

    public void unhighLightEdge() {
        edgeLine.setStroke(Color.BLACK);
        edgeLine.setStrokeWidth(1f);
        arrow.setFill(Color.BLACK);
}

    public void setWeight(double weight)
    {
        this.weight = weight;
        weightText.setText(Double.toString(weight));
    }
    
}
