package dongduk.cs.wcs.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

    public SecretKey load(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object obj = ois.readObject();
                secretKey = (SecretKey) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    public Key encode(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(bytes, algorithm);
    }
}
