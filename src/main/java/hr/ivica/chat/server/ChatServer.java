package hr.ivica.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * ChatServer is a multi-threaded chat server. It listens on a predefined port and creates threads for managing
 * connected clients. It caches output streams to connected clients, making it easier to "publish" an incoming message
 * to all connected clients
 *
 * @author ivica
 */
public class ChatServer {

    /**
     * Initializes ChatServer
     *
     */
    public ChatServer(int port) {
        this.port = port;
    }

    

    /**
     * Starts the server by opening a server socket and listening for connections
     */
    public void start() throws IOException {
        int poolSize = 20 * Runtime.getRuntime().availableProcessors();
        ExecutorService tasks = Executors.newFixedThreadPool(poolSize);

        try (ServerSocket listenSocket = new ServerSocket(port);) {
            logger.info("Server listening on port {}", listenSocket.getLocalPort());
            while (true) {
                Socket socket = listenSocket.accept();
                logger.info("Accepted connection on Socket: {}", socket.toString());

                // create a PrintWriter and cache it in a Map 
                synchronized (outputWriters) {
                    outputWriters.put(socket, new PrintWriter(socket.getOutputStream(), true));
                    logger.debug("Number of active connections: {}", outputWriters.size());
                }
                // handle client in new thread
                tasks.execute(new ChatServerWorker(socket, this));
            }
        } catch (IOException e) {
            logger.fatal(e.toString());
            throw e;
        }
    }

    /**
     * Closes a single client connection
     *
     * @param s socket to close
     */
    public void closeConnection(Socket s) {
        synchronized (outputWriters) {
            outputWriters.remove(s);
            try {
                s.close();
            } catch (IOException ioe) {
                logger.error("Exception while closing connection to {}: {}", s.toString(), ioe.toString());
            }
        }
    }

    /**
     * Sends a message to all connected clients
     *
     * @param line the message to be sent
     */
    void sendToAll(String line) {
        synchronized (outputWriters) {
            for (PrintWriter pw : outputWriters.values()) {
                pw.println(line);
            }
        }
    }

    /**
     * Loads properties which are used to configure the server
     *
     * @return properties
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("server_config.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Error loading properties: " + e.getMessage(), e);
        }
        return properties;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Properties properties = loadProperties();
        Integer port = Integer.parseInt(properties.getProperty("port"));
        
        ChatServer server = new ChatServer(port);
        server.start();
    }
    private int port;
    private final Map<Socket, PrintWriter> outputWriters = new HashMap<>();
    private static Logger logger = LogManager.getLogger(ChatServer.class.getName());
}
