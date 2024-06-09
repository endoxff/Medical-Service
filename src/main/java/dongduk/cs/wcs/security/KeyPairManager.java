package dongduk.cs.wcs.security;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;

public class KeyPairManager {
    private final String algorithm = "RSA";
    private final int keySize = 1024;

    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    @Getter
    private PublicKey publicKey;
    @Getter
    private PrivateKey privateKey;

    public KeyPair create() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);

        keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        return keyPair;
    }

    public boolean save(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(privateKey);
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
