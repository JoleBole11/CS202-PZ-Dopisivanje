package com.example.cs202pzdopisivanje.Controllers;

import Enums.SceneEnum;
import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.HomeApplication;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Requests.GroupRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;

public class HomeController {

    @FXML
    private ListView<String> friendsList;

    @FXML
    private ListView<String> groupsList;

    private final ObservableList<String> items = FXCollections.observableArrayList (
            "Strahinja", "Viktor", "Andrija", "Matija");

    private final ObservableList<String> groups = FXCollections.observableArrayList ();

    @FXML
    public void initialize() throws IOException {
        if (friendsList != null) {
            friendsList.setItems(items);
            friendsList.toFront();
        }

        // Load user's groups
        try {
            Client.getHandler().send(new GroupRequest(DbManager.getAccountID()));
            GroupRequest response = (GroupRequest) Client.getHandler().tryReceive();
            
            if (response != null && response.getGroups() != null) {
                groups.clear();
                groups.addAll(response.getGroups());
                System.out.println("Loaded " + groups.size() + " groups"); // Debug output
            } else {
                System.out.println("No groups received or response was null"); // Debug output
            }
        } catch (Exception e) {
            System.out.println("Error loading groups: " + e.getMessage());
            e.printStackTrace();
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
            groupsList.setItems(groups);
        }
    }
}