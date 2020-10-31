package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.core.holders.PropertiesHolder;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();

    public static AuthorizationPlugin getAuthorizationPlugin() {
        String className = null;
        className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AUTHORIZATION_PLUGIN_CLASS_KEY);
        return (AuthorizationPlugin) PluginInstantiator.classFactory.createPluginInstance(className);
    }
}
