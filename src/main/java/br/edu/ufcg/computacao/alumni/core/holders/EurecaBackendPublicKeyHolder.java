package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.api.http.request.PublicKey;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.util.PublicKeyUtil;

import java.security.interfaces.RSAPublicKey;

public class EurecaBackendPublicKeyHolder {
    private RSAPublicKey backendPublicKey;

    private static EurecaBackendPublicKeyHolder instance;

    private EurecaBackendPublicKeyHolder() {
    }

    public static synchronized EurecaBackendPublicKeyHolder getInstance() {
        if (instance == null) {
            instance = new EurecaBackendPublicKeyHolder();
        }
        return instance;
    }

    public RSAPublicKey getBackendPublicKey() throws EurecaException {
        if (this.backendPublicKey == null) {
            String backendAddress = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BACKEND_URL_KEY);
            String backendPort = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BACKEND_PORT_KEY);
            this.backendPublicKey = PublicKeyUtil.getPublicKey(backendAddress, backendPort, PublicKey.ENDPOINT);
        }
        return this.backendPublicKey;
    }
}
