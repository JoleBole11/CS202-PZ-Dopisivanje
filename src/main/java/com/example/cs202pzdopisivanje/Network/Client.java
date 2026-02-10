package com.example.cs202pzdopisivanje.Network;

import Enums.Constants;

import java.io.IOException;
import java.net.Socket;

/**
 * Client class responsible for managing the connection to the server in the ATM application.
 */
public class Client {
    /**
     * Represents the connection status to the server.
     */
    private static boolean connected = false;

    /**
     * Checks if the client is connected to the server.
     *
     * @return True if connected, false otherwise.
     */
    public static boolean isConnected() {
        return connected;
    }

    /**
     * Represents the client socket for communication with the server.
     */
    private static Socket socket;

    /**
     * Represents the handler for processing server communication.
     */
    private static Handler handler;

    /**
     * Gets the handler associated with the client's socket.
     *
     * @return The handler for processing server communication.
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * Establishes a connection to the server.
     *
     * @throws IOException If an I/O error occurs while establishing the connection.
     */
    public static void connect() throws IOException {
        if (isConnected()) return;

        System.out.println(Constants.clientIsConnecting);
        socket = new Socket(Server.IP, Server.PORT);
        System.out.println(Constants.clientConnected);
        connected = true;

        handler = new Handler(socket, true);
    }

    /**
     * Closes the connection to the server.
     *
     * @throws Exception If an error occurs while closing the connection.
     */
    public static void disconnect() throws Exception {
        if (!isConnected()) return;

        handler.close();

        System.out.println(Constants.clientIsDisconnecting);
        socket.close();
        System.out.println(Constants.clientDisconnected);
        connected = false;
    }
}
