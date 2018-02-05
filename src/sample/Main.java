package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.geometry.HPos.RIGHT;

public class Main extends Application {

    private static final int MAX_SEGMENTS = 6;

    private Stage primaryS;

    private int cWidth = 800;
    private Rectangle background;
    private TextField length;
    private TextField nOpp;
    private TextField nPd;

    Group root;

    private double lyy;
    private int nPdValue;
    private int nPp;
    private int nOPP;
    private int nNPP;
    private TextField[] alfa;
    private double[] attenuation;

    @Override
    public void start(Stage primaryStage) {

        primaryS = primaryStage;

        primaryStage.setTitle("CompNet");
        primaryStage.setHeight(850);
        primaryStage.setWidth(1200);
        primaryStage.setResizable(true);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        attenuation = new double[MAX_SEGMENTS];
        setSceneObjects(grid);
        makeLabels(grid);
        makeTextFields(grid);
        makeButtons(grid);

        primaryStage.show();
    }

    private void makeLabels(GridPane grid) {
        Label l1;

        Label length = new Label("L:");
        grid.add(length, 0, 1);

        Label pw = new Label("n_опп:");
        grid.add(pw, 0, 2);

        Label sl = new Label("n_пд:");
        grid.add(sl, 0, 3);

        for (int i = 1; i <= MAX_SEGMENTS; ++i) {
            l1 = new Label("α" + i + ":");
            grid.add(l1, 2, i);
        }


    }

    private void makeTextFields(GridPane grid) {
        length = new TextField();
        grid.add(length, 1, 1);

        nOpp = new TextField();
        grid.add(nOpp, 1, 2);

        nPd = new TextField();
        grid.add(nPd, 1, 3);

        alfa = new TextField[MAX_SEGMENTS];

        for (int i = 0; i < MAX_SEGMENTS; ++i) {
            alfa[i] = new TextField();
            grid.add(alfa[i], 3, i + 1);
        }
    }

    private void makeButtons(GridPane grid) {
        Button btn = new Button("Calculate");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 7);
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");

        btn.setOnAction(e -> {

            calculateLab1();
            outputLab1(actiontarget);

        });
    }

    private void setSceneObjects(GridPane grid) {
        root = new Group();

        background = new Rectangle(cWidth, cWidth);

        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        root.getChildren().add(background);

        HBox hBox = new HBox(10, root, grid);

        primaryS.setScene(new Scene(hBox, 800, 600));
    }

    private void calculateLab1() {

        nPdValue = Integer.valueOf(nPd.getText());

        lyy = Double.valueOf(length.getText()) / nPdValue;
        nPp = nPdValue - 1;
        nOPP = (int)(nPp * Double.valueOf(nOpp.getText()));
        nNPP = nPp - nOPP;

        for (int i = 0; i < MAX_SEGMENTS; ++i) {
            attenuation[i] = Double.valueOf(alfa[i].getText()) * lyy;
            System.out.println(attenuation[i]);
        }

    }

    private void outputLab1(Text text) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < MAX_SEGMENTS; ++i) {

            stringBuilder.append("A" + (i + 1) + ": " + attenuation[i]);
            stringBuilder.append("\n");

        }

        text.setText("Довжина підсилюваної ділянки: " + lyy + "\nЧисло обслуговуємих підсилюючих пунктів: " + nOPP +
        "\nЧисло необслуговуємих підсилюючих пунктів: " + nNPP + "\n" + stringBuilder.toString());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
