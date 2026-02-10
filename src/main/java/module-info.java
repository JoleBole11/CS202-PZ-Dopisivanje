module com.example.cs202pzdopisivanje {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    exports com.example.cs202pzdopisivanje;

    exports com.example.cs202pzdopisivanje.Controllers;
    opens com.example.cs202pzdopisivanje.Controllers to javafx.graphics, javafx.fxml;
    opens com.example.cs202pzdopisivanje to javafx.fxml, javafx.graphics;

}