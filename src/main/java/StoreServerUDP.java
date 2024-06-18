import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class StoreServerUDP {
    public static final int PORT = 8080;
    private Receiver receiver;

    public StoreServerUDP(Receiver receiver) {
        this.receiver = receiver;
    }

    public void startServer() throws IOException {
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Ми запустили сервер " + socket);

        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println("Отримано з'єднання від " + packet.getAddress() + ":" + packet.getPort());

                byte[] receivedData = packet.getData();
                receiver.receiveMessage(receivedData);
            }
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        StoreServerUDP server = new StoreServerUDP(receiver);
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
