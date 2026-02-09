package com.example.cs202pzdopisivanje.Windows;

import Enums.SceneEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    public void OnEditMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.PROFILE);
    }

    @FXML
    public void OnLogOutMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }
}
