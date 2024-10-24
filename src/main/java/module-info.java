module org.example.gameoflife {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gameoflife to javafx.fxml;
    exports org.example.gameoflife;
}