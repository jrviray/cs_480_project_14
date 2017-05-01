package cpp.edu.cs480.project14;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by wxy03 on 4/23/2017.
 */
public class Vertex extends Group{

    public  final static Color NORMAL_STROKE = new Color(0.0745, 0.3843, 0.8039, 1);

    public static double RADIUS=25f;

    private Circle circle;
    private Text text;

    private int ID;

    public Vertex(int ID,String content)
    {
        this(ID,content,60.0,60.0);

    }

    public Vertex(int ID,String content,double x,double y)
    {
        this.ID= ID;
        circle = new Circle(RADIUS);
        circle.setFill(new Color(0.5216, 1, 0.7725, 1));
        circle.setStroke(NORMAL_STROKE);
        setCursor(Cursor.HAND);
        setX(x);
        setY(y);
        text = new Text(content);
        text.setFont(Font.font(18));
        double H =  text.getBoundsInLocal().getHeight();
        double W = text.getBoundsInLocal().getWidth();
        text.translateXProperty().bind(getXProperty().subtract(W/2));
        text.translateYProperty().bind(getYProperty().add(H/4));
        text.setFill(NORMAL_STROKE);
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

    public int getID()
    {
        return ID;
    }

    public String getContent()
    {
        return text.getText();
    }






}
