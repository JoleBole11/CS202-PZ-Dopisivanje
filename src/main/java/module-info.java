module com.example.cs202pzdopisivanje {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.cs202pzdopisivanje to javafx.fxml;
    exports com.example.cs202pzdopisivanje;
}