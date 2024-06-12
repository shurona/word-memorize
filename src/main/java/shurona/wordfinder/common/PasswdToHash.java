package shurona.wordfinder.common;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswdToHash {
    public static String doProcess(String plainText) {
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("SHA-256");
            instance.update(plainText.getBytes());
            return Base64.encodeBase64String(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
