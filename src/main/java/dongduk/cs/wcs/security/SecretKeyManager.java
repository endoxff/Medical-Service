package dongduk.cs.wcs.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

public class SecretKeyManager {
    private final String algorithm = "AES";
    private final int keySize = 128;

    private KeyGenerator keyGenerator;
    private SecretKey secretKey;

    public SecretKey create() throws NoSuchAlgorithmException {
        keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);

        secretKey = keyGenerator.generateKey();

        return secretKey;
    }

    public boolean save(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(secretKey);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
