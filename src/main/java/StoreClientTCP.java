import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class StoreClientTCP {

    private Random random = new Random();
    private final InetAddress addr;
    public StoreClientTCP(InetAddress addr) {
        this.addr = addr;
    }

    public void generateAndSendMessages() throws ConnectionFailedException {
        boolean connected = false;
        long startTime = System.currentTimeMillis();
        long maxWaitTime = 60000; // Максимальний час очікування підключення - 60 секунд
        int retries = 0;

        while (!connected && System.currentTimeMillis() - startTime < maxWaitTime) {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Спроба під'єднатися до сервера " + addr + ":" + StoreServerTCP.PORT);
                    Socket socket = new Socket(addr, StoreServerTCP.PORT);
                    OutputStream outputStream = socket.getOutputStream();
                    System.out.println("Приєднався до сервера " + addr + ":" + StoreServerTCP.PORT);
                    connected = true;

                    int cType = random.nextInt(6);
                    int bUserId = random.nextInt(1000);
                    String messageText = "Message for client " + bUserId;
                    Message message = new Message(cType, bUserId, messageText);
                    MessageCreator creator = new MessageCreator();
                    byte[] msg = creator.create(message);
                    outputStream.write(msg);
                    outputStream.flush();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    socket.close(); // Закриваємо сокет після кожної відправки повідомлення
                } catch (ConnectException e) {
                    System.out.println("Помилка підключення: " + e.getMessage());
                    retries++;
                    if (retries >= 3) {
                        System.out.println("Перевищено максимальну кількість спроб. Закінчуємо роботу.");
                        throw new ConnectionFailedException("Connection to server failed after multiple retries.");
                    } else {
                        System.out.println("Спроба з'єднатися знову через 3 секунди...");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Lost connection to server. Attempting to reconnect...");
                    e.printStackTrace();
                    connected = false;
                    try {
                        Thread.sleep(3000); // Повторна спроба після 3 секунд
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (!connected) {
                System.out.println("Не вдалося підключитися до сервера протягом " + maxWaitTime / 1000 + " секунд.");
                throw new ConnectionFailedException("Connection to server timed out.");
            }

            System.out.println("Закінчили відправку повідомлень. Закриваємо клієнт.");
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            InetAddress addr = InetAddress.getByName(null);
            System.out.println("адреса =" + addr);
            StoreClientTCP client = new StoreClientTCP(addr);
            client.generateAndSendMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnectionFailedException e) {
            throw new RuntimeException(e);
        }
    }

    class ConnectionFailedException extends Exception {
        public ConnectionFailedException(String message) {
            super(message);
        }
    }
}

