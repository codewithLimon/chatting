package com.example.chatting;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerController implements Initializable {

    @FXML
    private MFXTextField message_inputBox;

    @FXML
    private Label top_Messages;

    @FXML
    private AnchorPane messages_anchorpane;

    @FXML
    private VBox messages_vbox;

    @FXML
    private MFXButton sendBTN;

    @FXML
    private VBox user_card_VBOX;

    @FXML
    private MFXScrollPane messages_scrollPane;

    public void addCards(String name) {
        HBox hBox = new HBox();
        hBox.setPrefWidth(355);
        hBox.setPrefHeight(80);


        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(80);
        anchorPane.setPrefWidth(355);
//        anchorPane.setStyle("-fx-background-color: red");

        Circle circle = new Circle();
        circle.setRadius(25);
        circle.setFill(Color.BLACK);
        circle.setCenterX(35);
        circle.setCenterY(35);


        Label label = new Label(name);
        label.setStyle("-fx-font-size: 15px;" +
                "-fx-font-weight: bold");
        label.setLayoutY(25);
        label.setLayoutX(80);


//        Label label2=new Label(message);
//        label2.setStyle("-fx-font-size: 12px;");
//        label2.setLayoutY(40);
//        label2.setLayoutX(70);
////        label2.maxWidth(100);
//        label2.setTextOverrun(OverrunStyle.ELLIPSIS);

        anchorPane.getChildren().add(circle);
        anchorPane.getChildren().add(label);
//        anchorPane.getChildren().add(label2);

        hBox.getChildren().add(anchorPane);
        user_card_VBOX.getChildren().add(hBox);


        anchorPane.setOnMouseClicked(event -> {
            message_inputBox.setVisible(true);
            messages_vbox.setVisible(true);
            messages_anchorpane.setVisible(true);
        });


    }


    private void sendMessage(String message, Boolean isServer) {
        HBox hBox = new HBox();
        if (isServer) {
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(10, 20, 0, 20));

            Text text = new Text(message);
            text.setFill(Color.BLACK);

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #e8e8e8;" +
                    "-fx-text-fill: White;" +
                    "-fx-padding: 5 10 5 10px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-max-width: 400px");

            textFlow.setPadding(new Insets(10, 10, 0, 10));


            hBox.getChildren().add(textFlow);

            messages_vbox.getChildren().add(hBox);

        } else {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(10, 20, 0, 20));

            Text text = new Text(message);
            text.setFill(Color.rgb(255, 255, 255));

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #0066ff;" +
                    "-fx-text-fill: White;" +
                    "-fx-padding: 5 10 5 10px;" +
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-max-width: 400px");

            textFlow.setPadding(new Insets(10, 10, 0, 10));


            hBox.getChildren().add(textFlow);


            messages_vbox.getChildren().add(hBox);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        message_inputBox.setVisible(false);
        messages_vbox.setVisible(false);
        messages_anchorpane.setVisible(false);


        top_Messages.setOnMouseClicked(event -> {
            message_inputBox.setVisible(false);
            messages_vbox.setVisible(false);
            messages_anchorpane.setVisible(false);
        });


        // add cards

        try {
            String filename = "src/main/resources/com/example/chatting/account_name.txt";
            byte[] bytes = Files.readAllBytes(Paths.get(filename));
            String content = new String(bytes);

            String[] extractNames = content.split("@");

            for (String s : extractNames) {
                addCards(s);
                users.add(s);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        sendBTN.setOnAction(event -> {
            sendMessage(message_inputBox.getText(), true);
        });


        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String filename = "src/main/resources/com/example/chatting/account_name.txt";
                    byte[] bytes = Files.readAllBytes(Paths.get(filename));
                    String content = new String(bytes);

                    String[] extractNames = content.split("@");

                    for (String s : extractNames) {
//                            addCards(s);
//                            users.add(s);

                        if (!users.contains(s)) {
                            users.add(s);

                            Platform.runLater(() -> {
                                addCards(s);
                            });
                        }
                    }
                } catch (Exception e) {

                }
            }
        }), 0, 100, TimeUnit.MILLISECONDS);
    }

    private volatile boolean running = true;
    private ObservableList<String> nameList = FXCollections.observableArrayList();
    private Vector<String> users = new Vector<>();
}


