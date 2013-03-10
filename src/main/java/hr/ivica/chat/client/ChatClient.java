package hr.ivica.chat.client;

import java.io.IOException;
import java.net.Socket;

/**
 * Client class that opens a connection to the ChatServer, then is able to 
 * send/receive messages to/from it.
 * 
 * @author ivica
 */
public class ChatClient {
    private Socket socket;

    public ChatClient(String host, int port) {
        try {
            this.socket = new Socket(host, port);
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 55555);
    }
}
