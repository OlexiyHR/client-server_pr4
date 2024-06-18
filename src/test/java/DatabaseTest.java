import command.DatabaseSetup;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    MessageCreator creator = new MessageCreator();
    private Receiver receiver = new Receiver();
    private StoreClientTCP storeClientTCP = new StoreClientTCP(InetAddress.getByName(null));
    private StoreServerTCP storeServerTCP = new StoreServerTCP(receiver);
    private StoreServerUDP storeServerUDP = new StoreServerUDP(receiver);
    private StoreClientUDP storeClientUDP = new StoreClientUDP(InetAddress.getByName(null));

    private Processor processor = new Processor();
    private Sender sender0 = new Sender();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private DatabaseSetup dt = new DatabaseSetup();

    public DatabaseTest() throws UnknownHostException {
    }


    @Test
    public void ProductTest() throws IOException {
        dt.setup();
        Message message = new Message(1, 1, "Buckwheat/This is groats/Morning/1/50/Food");
        byte[] msg = creator.create(message);
        receiver.receiveMessage(msg);
    }

    @Test
    public void createProductTest(){
        Message message1 = new Message(1, 1, "Buckwheat/This is groats/Morning/1/50/Food");
        String responce1 = processor.process(message1);
        assertEquals("Product has been successfully created: Buckwheat", responce1);
        Message message2 = new Message(1, 1, "Pasta/This is bakery product/Lala/3/150/Food");
        String responce2 = processor.process(message2);
        assertEquals("Product has been successfully created: Pasta", responce2);
    }

    @Test
    public void readProductTest(){
        Message message1 = new Message(2, 1, "Buckwheat");
        String responce1 = processor.process(message1);
        assertEquals("Buckwheat", responce1);
    }

    @Test
    public void updateProductTest(){
        Message message1 = new Message(3, 1, "Buckwheat/This is groats/Morning/2/75/Food");
        String responce1 = processor.process(message1);
        assertEquals("Product has been successfully updated: Buckwheat", responce1);
    }

    @Test
    public void listByCriteriaProductTest(){
        Message message1 = new Message(5, 1, "price");
        String responce1 = processor.process(message1);
        assertEquals("Pasta", responce1);
    }

    @Test
    public void deleteProductTest(){
        Message message1 = new Message(4, 1, "Buckwheat");
        String responce1 = processor.process(message1);
        assertEquals("Product has been successfully deleted: Buckwheat", responce1);
    }
}
