package com.example.cs202pzdopisivanje.CellFactories;

import com.example.cs202pzdopisivanje.Objects.Chat;
import javafx.scene.control.ListCell;

/**
 * Custom cell factory for displaying Chat objects in ListView.
 * Shows the chat name but keeps the Chat object with its ID intact.
 */
public class ChatCell extends ListCell<Chat> {
    
    @Override
    protected void updateItem(Chat chat, boolean empty) {
        super.updateItem(chat, empty);
        
        if (empty || chat == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Display the chat name in the list
            setText(chat.getChatName());
        }
    }
}
