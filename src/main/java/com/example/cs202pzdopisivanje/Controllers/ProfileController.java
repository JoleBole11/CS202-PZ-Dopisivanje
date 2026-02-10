package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProfileController {

    @FXML
    public void OnCancelButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    @FXML
    public void OnSaveButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }
}
