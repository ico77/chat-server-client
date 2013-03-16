package hr.ivica.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 55555);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.socket.getOutputStream()), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message.equals("exit")) break;
            writer.println(message);
            System.out.println(reader.readLine());
        }
    }
}
