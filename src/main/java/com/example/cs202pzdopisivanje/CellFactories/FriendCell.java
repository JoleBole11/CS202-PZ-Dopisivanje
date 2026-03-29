package com.example.cs202pzdopisivanje.CellFactories;

import com.example.cs202pzdopisivanje.Database.DbManager;
import com.example.cs202pzdopisivanje.Network.Client;
import com.example.cs202pzdopisivanje.Objects.Chat;
import com.example.cs202pzdopisivanje.Requests.RemoveFriendRequest;
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
 * Custom cell factory for displaying Friend objects in ListView.
 */
public class FriendCell extends ListCell<Chat> {

    /**
     * The HBox representing the layout of the cell.
     */
    private final HBox hBox = new HBox(10);
    /**
     * The name that will be displayed in the cell.
     */
    private final Label nameLabel = new Label();
    /**
     * An aesthetic spacer.
     */
    private final Region spacer = new Region();
    /**
     * The button to remove a friend.
     */
    private final Button removeButton = new Button("Remove");

    public FriendCell() {
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_LEFT);
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
    }

    @Override
    protected void updateItem(Chat chat, boolean empty) {
        super.updateItem(chat, empty);
        if (empty || chat == null) {
            setGraphic(null);
            return;
        }

        nameLabel.setText(chat.getChatName());
        hBox.getChildren().clear();
        
        removeButton.setOnAction(e -> removeFriend(chat));
        hBox.getChildren().addAll(nameLabel, spacer, removeButton);

        setGraphic(hBox);
    }

    /**
     * Removes Friend when the button is clicked.
     */
    private void removeFriend(Chat friendChat) {
        try {
            Client.getHandler().send(new RemoveFriendRequest(friendChat.getChatName()));
            RemoveFriendRequest response = (RemoveFriendRequest) Client.getHandler().tryReceive();
            
            if (Objects.equals(response.getUsername(), "Success")) {
                Platform.runLater(() -> getListView().getItems().remove(friendChat));
                System.out.println("Friend removed successfully");
            } else {
                System.out.println("Failed to remove friend: " + response.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}