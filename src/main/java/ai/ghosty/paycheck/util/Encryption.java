package ai.ghosty.paycheck.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String stringToSHA256(String input) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest mds = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = mds.digest(input.getBytes(StandardCharsets.UTF_8));

            String hex;
            for (byte b : hashBytes) {
                hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) hex = "0" + hex;
                sb.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[error] failed to find hashing algorithm " + e.getMessage());
        }

        return sb.toString();
    }
}
