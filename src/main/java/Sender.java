import java.net.InetAddress;

public class Sender {
    private static int sentMessageCount = 0;
    public void sendMessage(byte[] message, InetAddress targetAddress) {
        System.out.println("Sending message "+ message.toString() + " to " + targetAddress);
        sentMessageCount++;
    }

    public static int getSentMessagesCount() {
        return sentMessageCount;
    }

}
