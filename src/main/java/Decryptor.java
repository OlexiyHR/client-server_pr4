import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryptor {
    private static final byte[] key = new byte[16];
    private static final byte[] iv = new byte[16];

    static {
        for (int i = 0; i < 16; i++) {
            key[i] = (byte) ("012345678876543210".charAt(i));
            iv[i] = 0;
        }
    }

    public static byte[] decrypt(byte[] message) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            return cipher.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }
}
