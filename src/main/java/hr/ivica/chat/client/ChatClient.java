package hr.ivica.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client class that opens a connection to the ChatServer, then is able to send/receive messages to/from it.
 *
 * @author ivica
 */
public class ChatClient implements Runnable {

    /**
     * Initializes ChatClient
     *
     * @param host server to connect to
     * @param port port which will be used to listen for connections
     *
     */
    public ChatClient(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        } catch (IOException ioe) {
            logger.fatal(ioe);
            throw ioe;
        }
    }

    /**
     * Initializes ChatClient
     *
     * @param host server to connect to
     * @param port port which will be used to listen for connections
     * @param view object implementing the GUI
     *
     */
    public ChatClient(String host, int port, ChatClientView view) throws IOException {
        this(host, port);
        this.view = view;
        view.addSendMsgListener(new SendMsgActionListener());
    }

    /**
     * Tests the connection to the server
     * @param host server to connect to
     * @param port port to connect to
     * @return 1 if the test was successful, 0 otherwise
     */
    public static boolean testConnection(String host, int port) {
        boolean success = false;
        try (Socket s = new Socket(host, port)){
            success = true;
        } catch (IOException ioe) {
            logger.info("Error while testing the connection {}:", ioe.toString());
        }
        return success;
    }
    
    /**
     * Listens for incoming messages from the server, after receiving the message the registered view is notified so it can display the message.
     *
     */
    @Override
    public void run() {
        String incomingMessage;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                incomingMessage = reader.readLine();
                if (incomingMessage == null) {
                    throw new IOException("Input stream closed unexpectedly");
                }
                logger.debug(incomingMessage);
                view.appendIncomingMessage(incomingMessage);
            }
        } catch (IOException ioe) {
            logger.fatal(ioe.toString());
            view.showErrorDialogAndQuit(ioe);
        }
    }

    /**
     * Sends a string message to the server
     *
     * @param outgoingMessage the message to be sent
     */
    public void sendMessage(String outgoingMessage) {
        writer.println(outgoingMessage);
    }

    /**
     * Inner class implementing that is sent to GUI
     * controls as an action listener
     */
    private class SendMsgActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("Thread info {}", Thread.currentThread().toString());
            logger.debug("ActionEvent info {}", e);
                        
            String nickname = view.getNickname();
            if (nickname.isEmpty()) nickname = "Unknown user";
            
            sendMessage(nickname + " says: " + view.getMessage());
        }
    }
    
    private Socket socket;
    private PrintWriter writer;
    private ChatClientView view;
    private static final Logger logger = LogManager.getLogger(ChatClient.class.getName());
}
