package br.edu.ufcg.computacao.alumni.core.holders;

import java.security.interfaces.RSAPublicKey;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.api.http.request.PublicKey;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.util.PublicKeyUtil;

public class EurecaAsPublicKeyHolder {
    private RSAPublicKey asPublicKey;

    private static EurecaAsPublicKeyHolder instance;

    private EurecaAsPublicKeyHolder() {
    }

    public static synchronized EurecaAsPublicKeyHolder getInstance() {
        if (instance == null) {
            instance = new EurecaAsPublicKeyHolder();
        }
        return instance;
    }

    public RSAPublicKey getAsPublicKey() throws EurecaException {
        if (this.asPublicKey == null) {
            String asAddress = null;
            String asPort = null;
            asAddress = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AS_URL_KEY);
            asPort = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AS_PORT_KEY);
            this.asPublicKey = PublicKeyUtil.getPublicKey(asAddress, asPort, PublicKey.PUBLIC_KEY_ENDPOINT);
        }
        return this.asPublicKey;
    }
}
