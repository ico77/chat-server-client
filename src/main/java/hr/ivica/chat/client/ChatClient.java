package hr.ivica.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client class that opens a connection to the ChatServer, then is able to 
 * send/receive messages to/from it.
 * 
 * @author ivica
 */
public class ChatClient implements Runnable {
    private Socket socket;
    private PrintWriter writer;
    private volatile boolean closing = false;
    private String incomingMessage;
    
    /**
     * Initializes ChatClient 
     * @param host server to connect to
     * @param port port which will be used to listen for connections
     *          
     */
    public ChatClient(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
        
    }
    
    public void setClosing(boolean closing) {
        this.closing = closing;
    }

    public boolean isClosing() {
        return closing;
    }
    
    /**
     * Listens for incoming messages from the server, after receiving the message
     * the registered view is notified so it can display the message.
     * 
     */
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!isClosing()) {
                incomingMessage = reader.readLine();
                System.out.println(incomingMessage);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        } 
    }
    
    /**
     * Sends a string message to the server
     * @param the message to be sent
     */
    public void sendMessage(String outgoingMessage) {
        writer.println(outgoingMessage);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 55555);
        new Thread(client).start();
        
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                client.setClosing(true);
                break;
            }
            client.sendMessage(message);
        }
    }
    
}
