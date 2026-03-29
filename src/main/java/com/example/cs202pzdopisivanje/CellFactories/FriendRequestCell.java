package com.example.cs202pzdopisivanje.CellFactories;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;
import com.example.cs202pzdopisivanje.Requests.AcceptFriendRequest;
import com.example.cs202pzdopisivanje.Requests.DenyFriendRequest;
import com.example.cs202pzdopisivanje.Requests.JoinGroupRequest;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Objects;

/**
 * Custom cell factory for displaying Friend requests in ListView.
 */
public class FriendRequestCell extends ListCell<FriendRequestObject> {

    private final HBox hBox = new HBox(10);
    private final Label nameLabel = new Label();
    private final Region spacer = new Region();
    private final Button acceptButton = new Button("Accept");
    private final Button denyButton = new Button("Deny");
    private final Label pendingLabel = new Label("Pending");

    public FriendRequestCell() {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_LEFT);
        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        denyButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        pendingLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
    }

    @Override
    protected void updateItem(FriendRequestObject item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        nameLabel.setText(item.getName());
        hBox.getChildren().clear();

        if (Objects.equals(item.getType(), "Pending")) {
            acceptButton.setOnAction(e -> acceptFriendRequest(item));
            denyButton.setOnAction(e -> denyFriendRequest(item));
            hBox.getChildren().addAll(nameLabel, spacer, acceptButton, denyButton);
        } else if (Objects.equals(item.getType(), "Sent")) {
            hBox.getChildren().addAll(nameLabel, spacer, pendingLabel);
        }

        setGraphic(hBox);
    }

    /**
     * Accepts the pending Friend Request.
     */
    private void acceptFriendRequest(FriendRequestObject item) {
        try {
            Client.getHandler().send(new AcceptFriendRequest(item.getName()));
            AcceptFriendRequest response = (AcceptFriendRequest) Client.getHandler().tryReceive();
            
            if (Objects.equals(response.getUsername(), "Success")) {
                Platform.runLater(() -> getListView().getItems().remove(item));
                System.out.println("Friend request accepted successfully");
            } else {
                System.out.println("Failed to accept friend request: " + response.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Denies the pending Friend Request.
     */
    private void denyFriendRequest(FriendRequestObject item) {
        try {
            Client.getHandler().send(new DenyFriendRequest(item.getName()));
            DenyFriendRequest response = (DenyFriendRequest) Client.getHandler().tryReceive();
            
            if (Objects.equals(response.getUsername(), "Success")) {
                Platform.runLater(() -> getListView().getItems().remove(item));
                System.out.println("Friend request denied successfully");
            } else {
                System.out.println("Failed to deny friend request: " + response.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}