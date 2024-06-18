import java.nio.charset.StandardCharsets;

public class MessageGetter {

    public Message get(byte[] packet) {
        int wLen = (int) (((packet[10] & 0xFF) << 24) | ((packet[11] & 0xFF) << 16) | ((packet[12] & 0xFF) << 8) | (packet[13] & 0xFF));
        byte[] msg = new byte[wLen];
        System.arraycopy(packet, 16, msg, 0, wLen);
        short crc2 = (short) (((packet[16+wLen] & 0xFF) << 8) | (packet[17+wLen] & 0xFF));

        msg = Decryptor.decrypt(msg);

        int cType = (int) (((msg[0] & 0xFF) << 24) | ((msg[1] & 0xFF) << 16) | ((msg[2] & 0xFF) << 8) | (msg[3] & 0xFF));
        int bUserId = (int) (((msg[4] & 0xFF) << 24) | ((msg[5] & 0xFF) << 16) | ((msg[6] & 0xFF) << 8) | (msg[7] & 0xFF));
        byte[] messageBytes = new byte[wLen-8];
        System.arraycopy(msg, 8, messageBytes, 0, wLen-8);
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        Message bMsq = new Message(cType, bUserId, message);

        checkCRC1(packet);
        checkCRC2(msg, crc2);

        return bMsq;
    }

    private void checkCRC1(byte[] packet){
        byte[] dataForHCrc = new byte[14];
        System.arraycopy(packet, 0, dataForHCrc, 0, 14);
        short hCrc = CRC16.calculateCRC16(dataForHCrc);

        short hCrc1 = (short) (((packet[14] & 0xFF) << 8) | (packet[15] & 0xFF));

        if (hCrc != hCrc1) {
            throw new RuntimeException("CRC 1 failed");
        }
    }

    private void checkCRC2(byte[] message, short mCrc1){

        short mCrc = CRC16.calculateCRC16(message);


        if (mCrc != mCrc1) {
            throw new RuntimeException("CRC 2 failed");
        }
    }
}
