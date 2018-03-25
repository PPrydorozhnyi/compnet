package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import networkEntities.BoosterStation;

import java.util.ArrayList;

import static javafx.geometry.HPos.RIGHT;

public class Main extends Application {

    private static final int MAX_SEGMENTS = 4;

    private Stage primaryS;

    private TextField length;
    private TextField nOpp;
    private TextField nPd;
    private TextField nChan;

    private Group root;

    private double lyy;
    private int nPp;
    private int nOPP;
    private int nNPP;
    private int nChannels;
    private TextField[] alfa;
    private double[] attenuation;
    private BoosterStation[] boosters;

    private int nAIP;
    private int nAPP;
    private int nAVP;
    private int nATP;
    private int nACP;

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

        drawScene(grid);
    }

    private void drawScene(GridPane grid) {
        setSceneObjects(grid);
        makeLabels(grid);
        makeTextFields(grid);
        makeButtons(grid);

        primaryS.show();
    }

    private void makeLabels(GridPane grid) {


        Label length = new Label("L:");
        grid.add(length, 0, 1);

        Label pw = new Label("n_опп:");
        grid.add(pw, 0, 2);

        Label sl = new Label("n_пд:");
        grid.add(sl, 0, 3);

        for (int i = 1; i <= MAX_SEGMENTS; ++i) {
            Label l1 = new Label("α" + i + ":");
            grid.add(l1, 2, i);
        }


        Label nChannels = new Label("Number of channels");
        grid.add(nChannels, 0, 7);
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
            alfa[i].setText("1");
            grid.add(alfa[i], 3, i + 1);
        }

        nChan = new TextField();
        grid.add(nChan, 1, 7);
    }

    private void makeButtons(GridPane grid) {
        Button btn = new Button("Calculate");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 2, 7);
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");

        btn.setOnAction(e -> {

            calculateLab1();
            outputLab1(actiontarget);


            createBoosters(root);

            createConnections(root);


        });

        Button btnChan = new Button("Result");
        HBox hbChan = new HBox(10);
        hbChan.setAlignment(Pos.BOTTOM_RIGHT);
        hbChan.getChildren().add(btnChan);
        grid.add(hbChan, 1, 8);

//        final Text actiontargetC = new Text();
//        grid.add(actiontargetC, 2, 8);
//        GridPane.setColumnSpan(actiontarget, 2);
//        GridPane.setHalignment(actiontarget, RIGHT);
//        actiontarget.setId("actiontarget");

        btnChan.setOnAction(e -> {

            ArrayList<BoosterStation> upperStations = new ArrayList<>();
            ArrayList<BoosterStation> lowerStations = new ArrayList<>();

            System.out.println("Done");

            nChannels = Integer.valueOf(nChan.getText());

            root.getChildren().clear();
            grid.getChildren().clear();
            drawScene(grid);

            calculateNagp();

            createAGP(root, upperStations, lowerStations);
            createLines(upperStations);
            connectGO(lowerStations);

            //System.out.println(nAIP + " " + nAPP + " " + nAVP + " " + nATP + " " + nACP);

        });

        Button btnChan1 = new Button("Result1");
        HBox hbChan1 = new HBox(10);
        hbChan1.setAlignment(Pos.BOTTOM_RIGHT);
        hbChan1.getChildren().add(btnChan1);
        grid.add(hbChan1, 1, 9);

        btnChan1.setOnAction(e -> {

            System.out.println("Done");

            nChannels = Integer.valueOf(nChan.getText());

            root.getChildren().clear();
            grid.getChildren().clear();
            drawScene(grid);
            Label label1 = new Label();
            root.getChildren().add(label1);

            calculateNagp();

            createLab3(label1);


        });
    }

    private void createBoosters(Group root) {
        boosters = new BoosterStation[nPp + 2];
        int frequency = nNPP / (nOPP + 1);
        //System.out.println(frequency);

        boosters[0] = new BoosterStation(root, "KC", 0);
        boosters[boosters.length - 1] = new BoosterStation(root, "KC", boosters.length - 1);

        for (int i = 1, count = 0; i < boosters.length - 1; ++i) {
            if (count == frequency && nOPP !=0) {
                boosters[i] = new BoosterStation(root, "OПП", i);
                count = 0;
                --nOPP;
            } else {

                boosters[i] = new BoosterStation(root, "НПП", i);
                count++;
            }

        }
    }

    private void setSceneObjects(GridPane grid) {
        root = new Group();

        int cWidth = 800;
        Rectangle background = new Rectangle(cWidth, cWidth);

        background.setFill(Color.WHITE);
//        background.setStroke(Color.BLACK);
        root.getChildren().add(background);

        HBox hBox = new HBox(10, root, grid);

        primaryS.setScene(new Scene(hBox, 800, 600));
    }

    private void calculateLab1() {

        int nPdValue = Integer.valueOf(nPd.getText());

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

            stringBuilder.append("A");
            stringBuilder.append(i + 1);
            stringBuilder.append(": ");
            stringBuilder.append(attenuation[i]);
            stringBuilder.append("\n");

        }

        text.setText("Довжина підсилюваної ділянки: " + lyy + "\nЧисло обслуговуємих підсилюючих пунктів: " + nOPP +
        "\nЧисло необслуговуємих підсилюючих пунктів: " + nNPP + "\n" + stringBuilder.toString());


    }

    private void createConnections(Group root) {
        Line line;
        int y = boosters[0].getY() + boosters[0].getWidth() / 2;

        for (int i = 0; i < boosters.length - 1; ++i) {
            line = new Line(boosters[i].getX() + boosters[i].getWidth(),y ,
                    boosters[i + 1].getX(), y);

            root.getChildren().add(line);
        }
    }

    private void calculateNagp() {

        if (nChannels <= 12) {
            nAIP = nChannels / 3;
        } else {
            nAIP = 0;
            nAPP = nChannels / 12;
            System.out.println(nAPP);

            if (nAPP / 5 > 0) {
                nAVP = nAPP / 5;

                if (nAVP / 5 > 0) {
                    nATP = nAVP / 5;

                    if (nATP / 5 > 0) {
                        nACP = nATP / 5;
                    }
                }

            }
        }

    }

    private void createLab3(Label label) {

        Image image;
        Label[] labels = new Label[6];

        if (nAIP != 0) {
            image = new Image("case1.jpg");
            label.setGraphic(new ImageView(image));

            labels[0] = new Label("1");
            labels[0].setTranslateX(125);
            labels[0].setTranslateY(260);
            labels[1] = new Label(String.valueOf(nAIP));
            labels[1].setTranslateX(125);
            labels[1].setTranslateY(345);

            root.getChildren().addAll(labels[0], labels[1]);
        } else {

            if (nAPP > 1 && nAPP < 5 && nAVP == 0) {

                image = new Image("case2.jpg");
                label.setGraphic(new ImageView(image));

                labels[0] = new Label("1");
                labels[0].setTranslateX(95);
                labels[0].setTranslateY(213);
                labels[1] = new Label(String.valueOf(nAPP));
                labels[1].setTranslateX(95);
                labels[1].setTranslateY(295);

                root.getChildren().addAll(labels[0], labels[1]);
            } else if (nAVP == 1 && nAPP == 5) {
                image = new Image("case3.0.jpg");
                label.setGraphic(new ImageView(image));

                labels[0] = new Label("1");
                labels[0].setTranslateX(95);
                labels[0].setTranslateY(213);
                labels[1] = new Label(String.valueOf(nAPP));
                labels[1].setTranslateX(95);
                labels[1].setTranslateY(295);

                labels[2] = new Label("1");
                labels[2].setTranslateX(350);
                labels[2].setTranslateY(393);

                root.getChildren().addAll(labels[0],labels[1], labels[2]);

            } else if (nAPP >= 5 && nAPP %5 == 0 && nAVP < 5 && nATP == 0) {

                image = new Image("case3.jpg");
                label.setGraphic(new ImageView(image));

                labels[0] = new Label("1");
                labels[0].setTranslateX(95);
                labels[0].setTranslateY(213);
                labels[1] = new Label(String.valueOf(nAPP));
                labels[1].setTranslateX(95);
                labels[1].setTranslateY(295);

                labels[2] = new Label("1");
                labels[2].setTranslateX(350);
                labels[2].setTranslateY(393);
                labels[3] = new Label(String.valueOf(nAVP));
                labels[3].setTranslateX(350);
                labels[3].setTranslateY(480);

                root.getChildren().addAll(labels[0],labels[1], labels[2], labels[3]);

            } else if (nAPP >= 5 && nAPP %5 != 0 && nAVP == 1) {

                image = new Image("case4.jpg");
                label.setGraphic(new ImageView(image));

                labels[0] = new Label("1");
                labels[0].setTranslateX(95);
                labels[0].setTranslateY(213);
                labels[1] = new Label(String.valueOf(nAPP - nAPP % 5));
                labels[1].setTranslateX(95);
                labels[1].setTranslateY(295);

                labels[4] = new Label(String.valueOf(nAPP));
                labels[4].setTranslateX(95);
                labels[4].setTranslateY(393);

                labels[2] = new Label("1");
                labels[2].setTranslateX(350);
                labels[2].setTranslateY(393);


                root.getChildren().addAll(labels[0],labels[1], labels[2], labels[4]);

            } else if(nATP >= 1 && nATP < 5) {

                image = new Image("Case5.png");
                label.setGraphic(new ImageView(image));

                labels[0] = new Label("1");
                labels[0].setTranslateX(95);
                labels[0].setTranslateY(213);
                labels[1] = new Label(String.valueOf(nAPP));
                labels[1].setTranslateX(95);
                labels[1].setTranslateY(295);

                labels[2] = new Label("1");
                labels[2].setTranslateX(350);
                labels[2].setTranslateY(393);
                labels[3] = new Label(String.valueOf(nAVP));
                labels[3].setTranslateX(350);
                labels[3].setTranslateY(480);

                labels[4] = new Label("1");
                labels[4].setTranslateX(615);
                labels[4].setTranslateY(568);
                labels[5] = new Label(String.valueOf(nATP));
                labels[5].setTranslateX(615);
                labels[5].setTranslateY(655);

                root.getChildren().addAll(labels);

            }

        }


    }

    private void createAGP(Group root, ArrayList<BoosterStation> upperStations,ArrayList<BoosterStation> lowerStations ) {

        BoosterStation au;
        BoosterStation kalt;
        int lastN;

        if (nAIP != 0) {
            if (nAIP > 1)
                createCoupleStations(upperStations, lowerStations, "АІП", 0, nAIP);
            else
               createSigleStation(upperStations, lowerStations, "АІП", 0);

            lastN = 0;

        } else {

            lastN = 0;

            if (nAPP > 1)
                createCoupleStations(upperStations, lowerStations, "АІП", lastN, nAPP);
            else
                createSigleStation(upperStations, lowerStations, "АІП", lastN);


            if (nAVP != 0) {

//                lastN = 1;

                if (nAVP > 1)
                    createCoupleStations(upperStations, lowerStations, "АПП",++lastN, nAVP );
                else
                    createSigleStation(upperStations, lowerStations, "АПП", ++lastN);

                if (nATP != 0) {
//                    lastN = 2;

                    if (nATP > 1)
                        createCoupleStations(upperStations, lowerStations, "АВП",++lastN, nATP);
                    else
                        createSigleStation(upperStations, lowerStations, "АВП", ++lastN);

                    if (nACP != 0) {
//                        lastN = 3;

                        if (nACP > 1)
                            createCoupleStations(upperStations, lowerStations, "АТП", ++lastN, nACP);
                        else
                            createSigleStation(upperStations, lowerStations, "АТП", ++lastN);
                    }
                }
            }

        }

        au = new BoosterStation(root, "АУ", ++lastN);
        lowerStations.add(au);
        upperStations.add(au);
        kalt = new BoosterStation(root, "КАЛТ", ++lastN);
        upperStations.add(kalt);


    }

    private void createCoupleStations(ArrayList<BoosterStation> upperStations, ArrayList<BoosterStation> lowerStations,
                                      String text, int number, int endNumber) {
        BoosterStation lastStation;
        BoosterStation currentStation;
        Line connector;

        Label first;
        Label second;

        lastStation = new BoosterStation(root, text, number);
        currentStation = new BoosterStation(root, text, number);
        currentStation.setLevel(1);

        upperStations.add(lastStation);
        lowerStations.add(currentStation);

        connector = new Line(lastStation.getX() + lastStation.getWidth() / 2,
                lastStation.getY() + lastStation.getWidth(),
                currentStation.getX() + currentStation.getWidth() /2,
                currentStation.getY());
        root.getChildren().add(connector);
        // TODO: add label with number of station

        first = new Label("1");
        first.setTranslateX(lastStation.getX() - 30);
        first.setTranslateY(lastStation.getY());

        second = new Label(String.valueOf(endNumber));
        second.setTranslateX(currentStation.getX() - 30);
        second.setTranslateY(currentStation.getY());

        root.getChildren().addAll(first, second);
    }

    private void createSigleStation(ArrayList<BoosterStation> upperStations,ArrayList<BoosterStation> lowerStations,
                                    String text, int number) {
        BoosterStation lastStation;

        lastStation = new BoosterStation(root, text, number);
        upperStations.add(lastStation);
        lowerStations.add(lastStation);
    }

    private void createLines(ArrayList<BoosterStation> stations) {
        Line line;
        int y = stations.get(0).getY() +stations.get(0).getWidth() / 2;

        for (int i = 0; i < stations.size() - 1; ++i) {
            line = new Line(stations.get(i).getX() + stations.get(i).getWidth(),y ,
                    stations.get(i + 1).getX(), y);

            root.getChildren().add(line);
        }
    }

    private void connectGO(ArrayList<BoosterStation> stations) {
        Line line;
        Label go;
        int width = 60;

        go = new Label("ГО");
        go.setTranslateX(380);
        go.setTranslateY(600);
        go.setStyle("-fx-background-color: coral; -fx-padding: 10px;");
        go.setMinSize(width, width);
        go.setMaxSize(width, width);
        go.setAlignment(Pos.CENTER);

        root.getChildren().add(go);

        for (BoosterStation station: stations) {

            line = new Line(station.getX() + station.getWidth() / 2,
                    station.getY() + station.getWidth(),
                    go.getTranslateX() + width / 2, go.getTranslateY());

            root.getChildren().add(line);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
