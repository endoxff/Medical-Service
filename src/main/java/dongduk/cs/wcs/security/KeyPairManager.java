package dongduk.cs.wcs.security;

import lombok.Getter;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyPairManager {
    private final String algorithm = "RSA";
    private final int keySize = 1024;

    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    @Getter
    private PublicKey publicKey;
    @Getter
    private PrivateKey privateKey;

    public KeyPair create() {
        keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public Key load(String fileName) {
        Key key = null;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object obj = ois.readObject();
                key = (Key) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return key;
    }

    public Key encode(byte[] bytes) {
        Key key = null;
        try {
            key = KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(bytes));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return key;
    }
}
