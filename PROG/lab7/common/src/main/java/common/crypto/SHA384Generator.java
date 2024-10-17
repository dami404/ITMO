package common.crypto;

import common.exceptions.EncryptionException;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHA384Generator {
    public static String hash(String str) throws EncryptionException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        } catch (Exception e) {
            throw new EncryptionException();
        }
    }
}
