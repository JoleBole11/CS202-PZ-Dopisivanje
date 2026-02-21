package com.example.cs202pzdopisivanje.CellFactories;

import com.example.cs202pzdopisivanje.Objects.FriendRequestObject;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

public class FriendRequestCell extends ListCell<FriendRequestObject> {

    private final HBox hBox = new HBox(10);
    private final Label nameLabel = new Label();
    private final Region spacer = new Region();
    private final Button acceptButton = new Button("Accept");
    private final Button denyButton = new Button("Deny");
    private final Label pendingLabel = new Label("Pending");

    private Runnable onAccept;
    private Runnable onDeny;

    public FriendRequestCell() {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_LEFT);
        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        denyButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        pendingLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");

        acceptButton.setOnAction(e -> { if (onAccept != null) onAccept.run(); });
        denyButton.setOnAction(e -> { if (onDeny != null) onDeny.run(); });
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
            // Incoming request — show Accept/Deny
            onAccept = () -> getListView().getItems().remove(item);
            onDeny = () -> getListView().getItems().remove(item);
            hBox.getChildren().addAll(nameLabel, spacer, acceptButton, denyButton);
        } else if (Objects.equals(item.getType(), "Sent")) {
            // Sent request — show Pending label
            hBox.getChildren().addAll(nameLabel, spacer, pendingLabel);
        }

        setGraphic(hBox);
    }
}

