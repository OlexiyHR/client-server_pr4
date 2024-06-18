import java.io.*;
import java.net.*;
import org.apache.commons.io.IOUtils;

public class StoreServerTCP {
    public static final int PORT = 8080;

    private Receiver receiver;

    public StoreServerTCP(Receiver receiver) {
        this.receiver = receiver;
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        StoreServerTCP server = new StoreServerTCP(receiver);
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Ми запустили сервер " + serverSocket);

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("Встановили з'єднання: " + socket);
                    InputStream inputStream = socket.getInputStream();
                    byte[] message = IOUtils.toByteArray(inputStream);
                    receiver.receiveMessage(message);
                } finally {
                    System.out.println("Сервер закрив сокет ...");
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}
