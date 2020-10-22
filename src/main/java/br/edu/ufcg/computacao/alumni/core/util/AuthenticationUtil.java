package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.util.CryptoUtil;
import br.edu.ufcg.computacao.alumni.core.util.ServiceAsymmetricKeysHolder;
import br.edu.ufcg.computacao.alumni.core.models.SystemUser;
import org.apache.commons.lang.StringUtils;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationUtil {
    private static final long EXPIRATION_INTERVAL = TimeUnit.DAYS.toMillis(1); // One day

    public static SystemUser authenticate(PublicKey asPublicKey, String encryptedTokenValue)
            throws Exception {
        RSAPrivateKey privateKey = null;
        try {
            privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
            String plainTokenValue = TokenProtector.decrypt(privateKey, encryptedTokenValue,
                    SystemConstants.TOKEN_STRING_SEPARATOR);
            String[] tokenFields = StringUtils.splitByWholeSeparator(plainTokenValue, SystemConstants.TOKEN_SEPARATOR);
            String payload = tokenFields[0];
            String signature = tokenFields[1];
            checkIfSignatureIsValid(asPublicKey, payload, signature);
            String[] payloadFields = StringUtils.splitByWholeSeparator(payload, SystemConstants.PAYLOAD_SEPARATOR);
            String federationUserString = payloadFields[0];
            String expirationTime = payloadFields[1];
            checkIfTokenHasNotExprired(expirationTime);
            return SystemUser.deserialize(federationUserString);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String createFogbowToken(SystemUser systemUser, RSAPrivateKey privateKey, String publicKeyString)
            throws Exception {
        String tokenAttributes = SystemUser.serialize(systemUser);
        String expirationTime = generateExpirationTime();
        String payload = tokenAttributes + SystemConstants.PAYLOAD_SEPARATOR + expirationTime;
        try {
            String signature = CryptoUtil.sign(privateKey, payload);
            String signedUnprotectedToken = payload + SystemConstants.TOKEN_SEPARATOR + signature;
            RSAPublicKey publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
            return TokenProtector.encrypt(publicKey, signedUnprotectedToken, SystemConstants.TOKEN_STRING_SEPARATOR);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new Exception();
        }
    }

    private static void checkIfSignatureIsValid(PublicKey publicKey, String payload, String signature)
            throws Exception {

        try {
            if (!CryptoUtil.verify(publicKey, payload, signature)) {
                throw new Exception(Messages.INVALID_TOKEN);
            }
        } catch (SignatureException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new Exception(e.getMessage());
        }
    }

    private static void checkIfTokenHasNotExprired(String expirationTime) throws Exception {
        Date currentDate = new Date(getNow());
        Date expirationDate = new Date(Long.parseLong(expirationTime));
        if (expirationDate.before(currentDate)) {
            throw new Exception(Messages.EXPIRED_TOKEN);
        }
    }

    private static String generateExpirationTime() {
        Date expirationDate = new Date(getNow() + EXPIRATION_INTERVAL);
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    private static long getNow() {
        return System.currentTimeMillis();
    }

}
