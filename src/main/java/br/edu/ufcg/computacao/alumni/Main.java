package br.edu.ufcg.computacao.alumni;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
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
            AlumniHolder.getInstance();
        } catch (Exception e) {
            LOGGER.error(Messages.ERROR_READING_CONFIGURATION_FILE, e);
            System.exit(-1);
        }
    }
}
