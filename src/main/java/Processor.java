import command.*;

import java.nio.charset.StandardCharsets;

public class Processor {
    public String process(Message message){
        String messageText = new String(message.getMessage(), StandardCharsets.UTF_8);
        return switch (message.getType()){
            case 1 -> new CreateProduct().executeCommand(messageText);
            case 2 -> new ReadProduct().executeCommand(messageText);
            case 3 -> new UpdateProduct().executeCommand(messageText);
            case 4 -> new DeleteProduct().executeCommand(messageText);
            case 5 -> new ListByCriteria().executeCommand(messageText);
            default -> throw new IllegalArgumentException("Unsupported command type");
        };
    }
}
