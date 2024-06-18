import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Message {
    private int type;
    private int userId;
    private byte[] message;

    public Message(int type, int userId, String message) {
        this.type = type;
        this.userId = userId;
        this.message = messageToByte(message);
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public byte[] getMessage() { return message; }
    public void setMessage(byte[] message) { this.message = message; }

    public int wLen() {
        int messageSize = message.length;
        return 4 + 4 + messageSize; }

    private byte[] messageToByte(String message){
        if(message.endsWith(".json")){
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(message))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error: File not found: " + message, e);
            } catch (IOException e) {
                System.err.println("Error reading file: " + message + ". Reason: " + e.getMessage());
                return null;
            }
            message = sb.toString();
        }
        return message.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", userId=" + userId +
                ", message=" + new String(message, StandardCharsets.UTF_8) +
                '}';
    }
}
