import java.io.IOException;
import java.net.*;
import java.util.Random;

public class StoreClientUDP {

    private Random random = new Random();
    private final InetAddress serverAddress;

    public StoreClientUDP(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void generateAndSendMessages() throws SocketException {
        for (int i = 0; i < 10; i++) {
            try (DatagramSocket socket = new DatagramSocket()) {
                int cType = random.nextInt(6);
                int bUserId = random.nextInt(1000);
                String messageText = "Message for client " + bUserId;
                Message message = new Message(cType, bUserId, messageText);
                MessageCreator creator = new MessageCreator();
                byte[] msg = creator.create(message);

                DatagramPacket packet = new DatagramPacket(msg, msg.length, serverAddress, StoreServerUDP.PORT);
                socket.send(packet);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("закриваємо клієнт");
        }
    }

    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getByName(null);
            System.out.println("адреса =" + serverAddress);
            StoreClientUDP client = new StoreClientUDP(serverAddress);
            client.generateAndSendMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
