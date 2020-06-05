package seedu.canvas.storage;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Formatter;

public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String HMAC = "HmacSHA512";

    private static final String NEWLINE = "\n";

    private String secretKey;

    /**
     * Constructor for the Cryptographic Utility class.
     *
     * @param secretKey
     *  The secret key for encrypting and decrypting the text
     */
    public CryptoUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Encrypts the plaintext via a rather bad encryption scheme.
     *
     * @param plaintext
     *  The plaintext to be encrypted
     * @return
     *  The encrypted data
     */
    public String encrypt(String plaintext) {
        String encryptedData = plaintext;

        for (int i = 0; i < 5; ++i) {
            encryptedData = Base64.getEncoder().encodeToString(encryptedData.getBytes());
        }

        return encryptedData;
    }

    /**
     * Decrypts the encrypted data via a similarly bad decryption scheme.
     *
     * @param encryptedData
     *  The encrypted data to be decrypted
     * @return
     *  The original plaintext
     */
    public String decrypt(String encryptedData) {
        byte[] plaintext = encryptedData.getBytes();

        for (int i = 0; i < 5; ++i) {
            plaintext = Base64.getDecoder().decode(plaintext);
        }

        return new String(plaintext);
    }

    /**
     * Generates the hash digest of the given plaintext.
     *
     * @param plaintext
     *  The plaintext to be hashed
     * @return
     *  The corresponding hash digest of the plaintext
     * @throws Exception
     *  If there is an error in computing the hash
     */
    public String generateHash(String plaintext) throws Exception {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC);
            Mac hmac = Mac.getInstance(HMAC);
            hmac.init(secretKeySpec);

            byte[] hmacData = hmac.doFinal(plaintext.getBytes());

            return Base64.getEncoder().encodeToString(hmacData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("CryptoUtil: Unable to generate hash.");
            throw new Exception("CryptoUtil: Unable to generate hash.");
        }
    }

    /**
     * Checks if the content is corrupted by comparing its hash with the given hash.
     *
     * @param plaintext
     *  The content to be checked
     * @param hash
     *  The supposed hash of the content
     * @return
     *  True if the content is NOT corrupted, or False otherwise
     * @throws Exception
     *  If there is an error in computing the hash
     */
    public boolean isNotCorrupted(String plaintext, String hash) throws Exception {
        return generateHash(plaintext).equals(hash);
    }

    private SecretKey generateSecretKey(char[] key, byte[] salt) throws Exception {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(key, salt, 65536, 256);

            return new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), ALGORITHM);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e.getMessage());
            throw new Exception("CryptoUtil: Error generating secret key.");
        }
    }

    /**
     * Converts a byte array into it corresponding hex string.
     *
     * @param bytes
     *  The array of bytes to be converted
     * @return
     *  The corresponding hex string represented by the byte array
     */
    private static String bytesToHex(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte oneByte : bytes) {
            formatter.format("%02x", oneByte);
        }

        return formatter.toString();
    }
}
