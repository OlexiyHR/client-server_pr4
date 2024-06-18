import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MessagePacket {
    private byte magic;
    private byte src;
    private long pktId;
    private int wLen;
    private short hCrc;
    private Message msg;
    private short mCrc;

    public MessagePacket(byte magic, byte src, long pktId, int wLen, Message msg) throws IOException {
        this.magic = magic;
        this.src = src;
        this.pktId = pktId;
        this.wLen = wLen;

        byte[] bPktId = ByteBuffer.allocate(8).putLong(pktId).array();
        byte[] WLen = ByteBuffer.allocate(4).putInt(wLen).array();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(magic);
        stream.write(src);
        stream.write(bPktId);

        stream.write(WLen);


        byte [] stream1 = stream.toByteArray();
        this.hCrc = CRC16.calculateCRC16(stream1);
        this.msg = msg;

        byte[] cType = ByteBuffer.allocate(4).putInt(msg.getType()).array();
        byte[] bUserId = ByteBuffer.allocate(4).putInt(msg.getUserId()).array();
        byte[] byteMessage = new byte[msg.wLen()];
        System.arraycopy(cType, 0, byteMessage, 0, 4);
        System.arraycopy(bUserId, 0, byteMessage, 4, 4);
        System.arraycopy(msg.getMessage(), 0, byteMessage, 8, msg.wLen()-8);

        this.mCrc = CRC16.calculateCRC16(byteMessage);
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte getSrc() {
        return src;
    }

    public void setSrc(byte src) {
        this.src = src;
    }

    public long getPktId() {
        return pktId;
    }

    public void setPktId(long pktId) {
        this.pktId = pktId;
    }

    public int getWLen() {
        return wLen;
    }

    public void setWLen(int wLen) {
        this.wLen = wLen;
    }

    public short getHCrc() {
        return hCrc;
    }

    public void setHCrc(short hCrc) {
        this.hCrc = hCrc;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public short getMCrc() {
        return mCrc;
    }

    public void setMCrc(short mCrc) {
        this.mCrc = mCrc;
    }
}
