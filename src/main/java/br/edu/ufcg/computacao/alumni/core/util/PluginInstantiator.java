package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.core.holders.PropertiesHolder;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;

import java.io.IOException;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();

    public static AuthorizationPlugin getAuthorizationPlugin() {
        String className = null;
        try {
            className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AUTHORIZATION_PLUGIN_CLASS_KEY);
        } catch (IOException e) {
            throw new FatalErrorException(e.getMessage());
        }
        return (AuthorizationPlugin) PluginInstantiator.classFactory.createPluginInstance(className);
    }
}
