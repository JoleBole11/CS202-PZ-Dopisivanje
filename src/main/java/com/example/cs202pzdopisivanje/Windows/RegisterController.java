package com.example.cs202pzdopisivanje.Windows;

import Enums.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    public void OnCancelButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

    @FXML
    public void OnRegisterButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.HOME);
    }
}
