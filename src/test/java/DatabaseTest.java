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
    public void createProductTest(){
        dt.setup();
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
        assertEquals("Product Name: Buckwheat\n" +
                "Description: This is groats\n" +
                "Producer: Morning\n" +
                "Amount: 2\n" +
                "Price: 75.0\n" +
                "Group Name: Food\n", responce1);
    }

    @Test
    public void updateProductTest(){
        Message message1 = new Message(3, 1, "Buckwheat/This is groats/Morning/2/75/Food");
        String responce1 = processor.process(message1);
        assertEquals("Product has been successfully updated: Buckwheat", responce1);
    }

    @Test
    public void listByCriteriaProductTest(){
        Message message1 = new Message(5, 1, "producer");
        String responce1 = processor.process(message1);
        assertEquals("Products sorted by producer:\n" +
                "Product: Pasta, Description: This is bakery product, Producer: Lala, Amount: 3, Price: 150,00, Group_Name: Food\n" +
                "Product: Buckwheat, Description: This is groats, Producer: Morning, Amount: 2, Price: 75,00, Group_Name: Food\n", responce1);
    }

    @Test
    public void deleteProductTest(){
        Message message1 = new Message(4, 1, "Buckwheat");
        String responce1 = processor.process(message1);
        assertEquals("Product has been successfully deleted: Buckwheat", responce1);
    }
}
