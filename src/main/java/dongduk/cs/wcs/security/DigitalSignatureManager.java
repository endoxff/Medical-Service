package dongduk.cs.wcs.security;

import java.security.*;

public class DigitalSignatureManager {
    private final String signAlgorithm = "SHA256withRSA";

    private Signature signature;

    public byte[] sign(PrivateKey privateKey, String data)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes());

        return signature.sign();
    }

    public boolean verify(byte[] data, byte[] sign, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(publicKey);
        signature.update(data);

        return signature.verify(sign);
    }
}
