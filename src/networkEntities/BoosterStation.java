package networkEntities;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;

/**
 * Created by drake on 05/02/18.
 *
 * @author P.Pridorozhny
 */
public class BoosterStation {

    protected Label label;
//    protected Rectangle rectangle;
    protected final static int WIDTH = 60;
    protected int n;

    public BoosterStation(Group root, String text, int number) {

        n = number;
//        rectangle = new Rectangle(WIDTH, WIDTH);
//        rectangle.setStroke(Color.BLACK);
//        rectangle.setFill(Color.WHITE);
//
//        StackPane stack = new StackPane();
//        stack.getChildren().addAll(rectangle, text);
//        gridPane.getChildren().add(stack);

        label = new Label(text);
        label.setStyle("-fx-background-color: coral; -fx-padding: 10px;");


        root.getChildren().add(label);
        label.setMinSize(WIDTH, WIDTH);
        label.setMaxSize(WIDTH, WIDTH);
        label.setAlignment(Pos.CENTER);
        label.setTranslateX(10 + n * WIDTH + 20 * n);
        label.setTranslateY(200);

    }

    public int getWidth() {
        return WIDTH;
    }

    public int getX() {
        return (int)label.getTranslateX();
    }

    public int getY() {
        return (int)label.getTranslateY();
    }


}
