import java.io.IOException;

public class Worker {

    MessageCreator creator = new MessageCreator();
    MessageGetter getter = new MessageGetter();
    Processor processor = new Processor();
    Sender sender = new Sender();
    public void work (byte[] acceptedPacket) throws IOException {

        Message message = getter.get(acceptedPacket);
        String result = processor.process(message);
        byte[] encodedResult = creator.create(new Message(message.getType(), message.getUserId(), result));
        sender.sendMessage(encodedResult, null);

    }
}
