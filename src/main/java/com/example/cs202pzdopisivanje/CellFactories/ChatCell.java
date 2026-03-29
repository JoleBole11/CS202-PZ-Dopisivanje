package com.example.cs202pzdopisivanje.CellFactories;

import com.example.cs202pzdopisivanje.Objects.Chat;
import javafx.scene.control.ListCell;

/**
 * Custom cell factory for displaying Chat objects in ListView.
 */
public class ChatCell extends ListCell<Chat> {

    @Override
    protected void updateItem(Chat chat, boolean empty) {
        super.updateItem(chat, empty);
        
        if (empty || chat == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(chat.getChatName());
        }
    }
}
