package com.example.cs202pzdopisivanje.Windows;

import Enums.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    protected void OnLoginButtonClick() {
        HomeApplication.switchScene(SceneEnum.HOME);
    }

    @FXML
    public void OnRegisterButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.REGISTER);
    }
}
