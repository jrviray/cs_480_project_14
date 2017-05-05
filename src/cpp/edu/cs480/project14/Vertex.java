package cpp.edu.cs480.project14;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by wxy03 on 4/23/2017.
 */
public class Vertex extends Group{

    public  final static Color NORMAL_STROKE = new Color(0.3922, 0, 0, 1);

    public static double RADIUS=25f;

    private Circle circle;
    private Text text;

    private int ID;

    public Vertex(int ID,String context)
    {
        this.ID= ID;
        circle = new Circle(RADIUS);
        circle.setFill(new Color(1, 0.3373, 0.3098, 1));
        circle.setStroke(NORMAL_STROKE);
        setCursor(Cursor.HAND);
        circle.setTranslateX(60);
        circle.setTranslateY(60);
        text = new Text(context);
        text.setFont(Font.font(18));
        double H =  text.getBoundsInLocal().getHeight();
        double W = text.getBoundsInLocal().getWidth();
        text.translateXProperty().bind(getXProperty().subtract(W/2));
        text.translateYProperty().bind(getYProperty().add(H/4));
        getChildren().addAll(circle,text);

    }

    public DoubleProperty getXProperty()
    {
        return circle.translateXProperty();
    }

    public DoubleProperty getYProperty()
    {
        return circle.translateYProperty();
    }

    public double getX()
    {
        return circle.getTranslateX();
    }

    public double getY()
    {
        return circle.getTranslateY();
    }

    public void setX(double x)
    {
        circle.setTranslateX(x);
    }

    public void setY(double y)
    {
        circle.setTranslateY(y);
    }






}
