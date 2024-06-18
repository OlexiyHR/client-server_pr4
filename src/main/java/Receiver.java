import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private final AtomicInteger processedMessageCount = new AtomicInteger(0);
    public void receiveMessage(byte[] message){
        threadPool.submit(() -> {
            Worker worker = new Worker();
            try {
                worker.work(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            processedMessageCount.incrementAndGet();
        });
    }
    public int getProcessedMessagesCount() {
        return processedMessageCount.get();
    }
}
