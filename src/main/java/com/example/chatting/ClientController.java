package com.example.chatting;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientController implements Initializable {

    @FXML
    private VBox messageBodyVbox;

    @FXML
    private MFXTextField messageBox;

    @FXML
    private MFXButton sendBtn;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;


    private void sendMessage(String message, Boolean isServer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isServer) {
                    HBox hBox = new HBox();
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

                    Platform.runLater(() -> {
                        messageBodyVbox.getChildren().add(hBox);
                    });
                } else {
                    HBox hBox = new HBox();
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

                    Platform.runLater(() -> {
                        messageBodyVbox.getChildren().add(hBox);
                    });
                }

            }
        }).start();
    }

    public void sendButtonClicked(Event e) {
        String message = messageBox.getText().trim();
        System.out.println(message);
        if (!message.isEmpty()) {
            sendMessage(message, false);
            messageBox.clear();
            writer.println(message);

        }
    }

    String serverAddress = "127.0.0.1";
    int serverPort = 33333;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String name=JOptionPane.showInputDialog("Enter Your Name:");

        if(!name.isEmpty()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        //read name from file

//            System.out.print("Enter name of the client: ");
//            Scanner scanner = new Scanner(System.in);
//            name = scanner.nextLine();
                        String filename = "src/main/resources/com/example/chatting/account_name.txt";
                        File user_account_name = new File("src/main/resources/com/example/chatting/account_name.txt");
                        if (user_account_name.exists()) {


                            byte[] bytes = Files.readAllBytes(Paths.get(filename));
                            String content = new String(bytes);
                            String l_content=content.toLowerCase();
                            System.out.println(l_content);

                            if(!l_content.contains(name.toLowerCase())){
                                PrintWriter pw=new PrintWriter(new FileWriter(user_account_name,true));
                                pw.print(name+"@");
                                pw.close();
                            }
                        }

//                        socket = new Socket("localhost", 22223);
//                        writer = new PrintWriter(socket.getOutputStream(), true);
//                        scanner = new Scanner(socket.getInputStream());
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while(true){
//                                    String input = scanner.nextLine();
//                                    sendMessage(input,true);
//
//                                }
//                            }
//                        }).start();


                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }).start();
        }


//        try{
//            socket=new Socket("Localhost",9090);
//            System.out.println("Connected to server..");
//
//            printWriter=new PrintWriter(socket.getOutputStream(),true);
//            readMessage=new Scanner(socket.getInputStream());
//            System.out.println(readMessage);
//
//        }catch (IOException ie){
//            ie.printStackTrace();
//        }


    }

    private PrintWriter printWriter;
    private  Scanner readMessage;
}
