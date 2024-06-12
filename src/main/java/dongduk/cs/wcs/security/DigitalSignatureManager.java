package dongduk.cs.wcs.security;

import java.security.*;

public class DigitalSignatureManager {
    private final String signAlgorithm = "SHA256withRSA";

    private Signature signature;

    public byte[] sign(PrivateKey privateKey, String data) {
        signature = null;
        byte[] result = null;

        try {
            signature = Signature.getInstance(signAlgorithm);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            result = signature.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } finally {
            if (result == null) {
                result = new byte[0];
            }
        }

        return result;
    }

    public boolean verify(byte[] data, byte[] sign, PublicKey publicKey) {
        boolean result = false;
        try {
            signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            result = signature.verify(sign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return result;
    }
}
