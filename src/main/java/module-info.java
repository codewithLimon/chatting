module com.example.chatting {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens com.example.chatting to javafx.fxml;
    exports com.example.chatting;
}