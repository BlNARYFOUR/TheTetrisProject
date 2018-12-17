package util;

import org.pmw.tinylog.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implements multiple hashing algorithms.
 */
public final class Hash {
    private Hash() {

    }

    public static String md5(final String x) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.info(e.getMessage());
        }
        if (m != null) {
            m.update(x.getBytes(), 0, x.length());
            return new BigInteger(1, m.digest()).toString(16);
        } else {
            return null;
        }
    }
}
