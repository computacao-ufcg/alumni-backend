package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.util.CryptoUtil;
import org.apache.commons.lang.StringUtils;
import java.security.Key;

public class TokenProtector {
    public static String encrypt(Key key, String unprotectedToken, String tokenSeparator) throws Exception {
        String randomKey;
        String encryptedToken;
        String encryptedKey;
        try {
            randomKey = CryptoUtil.generateAESKey();
            encryptedToken = CryptoUtil.encryptAES(randomKey.getBytes("UTF-8"), unprotectedToken);
            encryptedKey = CryptoUtil.encrypt(randomKey, key);
            return encryptedKey + tokenSeparator + encryptedToken;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static String decrypt(Key key, String protectedToken, String tokenSeparator)
            throws Exception {
        String randomKey;
        String decryptedToken;
        String[] tokenParts = StringUtils.splitByWholeSeparator(protectedToken, tokenSeparator);

        try {
            randomKey = CryptoUtil.decrypt(tokenParts[0], key);
            decryptedToken = CryptoUtil.decryptAES(randomKey.getBytes("UTF-8"), tokenParts[1]);
            return decryptedToken;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static String rewrap(Key decryptKey, Key encryptKey, String protectedToken, String tokenSeparator)
            throws Exception{
        String unprotectedToken = decrypt(decryptKey, protectedToken, tokenSeparator);
        String newProtectedToken = encrypt(encryptKey, unprotectedToken, tokenSeparator);
        return newProtectedToken;
    }
}
