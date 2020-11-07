package br.edu.ufcg.computacao.alumni;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.ApplicationFacade;
import br.edu.ufcg.computacao.alumni.core.holders.*;
import br.edu.ufcg.computacao.alumni.core.plugins.AuthorizationPlugin;
import br.edu.ufcg.computacao.alumni.core.processors.EmployerFinderProcessor;
import br.edu.ufcg.computacao.alumni.core.processors.MatchesFinderProcessor;
import br.edu.ufcg.computacao.alumni.core.util.PluginInstantiator;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner {
    private final Logger LOGGER = Logger.getLogger(Main.class);

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Setting up asymmetric cryptography
            String publicKeyFilePath = PropertiesHolder.getInstance().
                    getProperty(ConfigurationPropertyKeys.ALUMNI_PUBLICKEY_FILE_KEY);
            String privateKeyFilePath = PropertiesHolder.getInstance().
                    getProperty(ConfigurationPropertyKeys.ALUMNI_PRIVATEKEY_FILE_KEY);
            ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(HomeDir.getPath() + publicKeyFilePath);
            ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(HomeDir.getPath() + privateKeyFilePath);

            // Setting up plugin
            AuthorizationPlugin authorizationPlugin = PluginInstantiator.getAuthorizationPlugin();

            // Setting up facade
            ApplicationFacade applicationFacade = ApplicationFacade.getInstance();
            applicationFacade.setAuthorizationPlugin(authorizationPlugin);

            // Reading input data
            LinkedinDataHolder.getInstance().start();
            AlumniHolder.getInstance().start();
            EmployersHolder.getInstance();
            MatchesHolder.getInstance();
            PendingMatchesHolder.getInstance();
            new MatchesFinderProcessor().start();
            new EmployerFinderProcessor().start();
            LOGGER.info(Messages.ALL_SET);
        } catch (Exception e) {
            LOGGER.error(Messages.ERROR_READING_CONFIGURATION_FILE, e);
            System.exit(-1);
        }
    }
}
