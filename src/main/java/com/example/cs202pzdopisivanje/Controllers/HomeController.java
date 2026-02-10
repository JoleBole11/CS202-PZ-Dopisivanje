package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.HomeApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HomeController {

    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> groupsList;

    private final ObservableList<String> items = FXCollections.observableArrayList (
            "Strahinja", "Viktor", "Andrija", "Matija");

    private final ObservableList<String> items2 = FXCollections.observableArrayList (
            "Paradox", "Unreal", "Amogus", "Meow");

    @FXML
    public void initialize() {
        if (friendsList != null) {
            friendsList.setItems(items);
            friendsList.toFront();
        }
    }

    @FXML
    public void OnEditMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.PROFILE);
    }

    @FXML
    public void OnLogOutMenuClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.LOGIN);
    }

    @FXML
    public void OnFriendsButtonClick(ActionEvent actionEvent) {
        HomeApplication.switchScene(SceneEnum.FRIENDS);
    }

    @FXML
    public void OnFriendsMenuSelected(ActionEvent actionEvent) {
        if (friendsList != null) {
            friendsList.toFront();
            groupsList.toBack();
            groupsList.setDisable(true);
            friendsList.setDisable(false);
            friendsList.setItems(items);
        }
    }

    @FXML
    public void OnGroupsMenuSelected(ActionEvent actionEvent) {
        if (groupsList != null) {
            groupsList.toFront();
            friendsList.toBack();
            groupsList.setDisable(false);
            friendsList.setDisable(true);
            groupsList.setItems(items2);
        }
    }
}