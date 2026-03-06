package controller; 

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class passwordHashingUtils {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] hash = generateHash(password.toCharArray(), salt);

            return Base64.getEncoder().encodeToString(salt) + ":" + 
                   Base64.getEncoder().encodeToString(hash);
                   
        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }


    public static boolean verifyPassword(String providedPassword, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) return false;
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            byte[] calculatedHash = generateHash(providedPassword.toCharArray(), salt);

            if (storedHash.length != calculatedHash.length) return false;
            for (int i = 0; i < storedHash.length; i++) {
                if (storedHash[i] != calculatedHash[i]) return false;
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] generateHash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
}