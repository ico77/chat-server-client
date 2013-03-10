/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.ivica.chat.server;

import java.net.Socket;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
