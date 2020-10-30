package br.edu.ufcg.computacao.alumni.core.util;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class ServiceAsymmetricKeysHolder {
    private static final Logger LOGGER = Logger.getLogger(ServiceAsymmetricKeysHolder.class);

    private RSAPublicKey servicePublicKey;
    private RSAPrivateKey servicePrivateKey;
    private String publicKeyFilePath;
    private String privateKeyFilePath;
    private static ServiceAsymmetricKeysHolder instance;

    public static synchronized ServiceAsymmetricKeysHolder getInstance() {
        if (instance == null) {
            instance = new ServiceAsymmetricKeysHolder();
        }
        return instance;
    }

    public void setPublicKeyFilePath(String publicKeyFilePath) {
        this.publicKeyFilePath = publicKeyFilePath;
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public RSAPublicKey getPublicKey() throws Exception {
        if (this.servicePublicKey == null) {
            if (this.publicKeyFilePath == null) throw new IllegalArgumentException();
            try {
                this.servicePublicKey = CryptoUtil.getPublicKey(this.publicKeyFilePath);
            } catch (IOException | GeneralSecurityException e) {
                throw new Exception(e.getMessage());
            }
        }
        return this.servicePublicKey;
    }

    public RSAPrivateKey getPrivateKey() throws Exception {
        if (this.servicePrivateKey == null) {
            if (this.privateKeyFilePath == null) throw new IllegalArgumentException();
            try {
                this.servicePrivateKey = CryptoUtil.getPrivateKey(this.privateKeyFilePath);
            } catch (IOException | GeneralSecurityException e) {
                throw new Exception(e.getMessage());
            }
        }
        return this.servicePrivateKey;
    }
}
