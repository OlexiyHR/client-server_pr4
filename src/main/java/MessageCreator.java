import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MessageCreator {
    static int count = 1;

    public byte[] create(Message msg) throws IOException {
        MessagePacket packet = new MessagePacket((byte) 0x13, (byte) 1, count++, msg.wLen(), msg);

        byte bMagic = packet.getMagic();
        byte bSrc = packet.getSrc();
        byte[] bPktId = ByteBuffer.allocate(8).putLong(packet.getPktId()).array();
        byte[] wLen = ByteBuffer.allocate(4).putInt(packet.getWLen()).array();
        byte[] hCrc = ByteBuffer.allocate(2).putShort(packet.getHCrc()).array();
        byte[] mCrc = ByteBuffer.allocate(2).putShort(packet.getMCrc()).array();

        byte[] cType = ByteBuffer.allocate(4).putInt(msg.getType()).array();
        byte[] bUserId = ByteBuffer.allocate(4).putInt(msg.getUserId()).array();
        byte[] byteMessage = new byte[msg.wLen()];
        System.arraycopy(cType, 0, byteMessage, 0, 4);
        System.arraycopy(bUserId, 0, byteMessage, 4, 4);
        System.arraycopy(msg.getMessage(), 0, byteMessage, 8, msg.wLen()-8);


        byte[] bMsq = Encryptor.encrypt(byteMessage);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(bMagic);
        stream.write(bSrc);
        stream.write(bPktId);
        stream.write(wLen);
        stream.write(hCrc);
        stream.write(bMsq);
        stream.write(mCrc);

        return stream.toByteArray();
    }
}
