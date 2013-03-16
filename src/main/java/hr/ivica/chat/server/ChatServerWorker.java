/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.ivica.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivica
 */
public class ChatServerWorker implements Runnable{
    private Socket socket;
    private ChatServer server;

    public ChatServerWorker(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String line = reader.readLine();
                System.out.println(line);
                if (line == null)
                    break;
                server.sendToAll(line);
            }
        } catch (IOException ex) {
            server.getLogger().error("Error handling client connection {} : {}", socket.toString(), ex.toString());
        } finally {
            server.getLogger().info("Closing client connection {}", socket.toString());
            server.closeConnection(socket);                       
        }
    }
    
    
}
