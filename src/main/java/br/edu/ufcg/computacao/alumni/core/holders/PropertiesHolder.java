package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHolder {
    private Logger LOGGER = Logger.getLogger(PropertiesHolder.class);

    private Properties properties;
    private static PropertiesHolder instance;

    private PropertiesHolder() throws FatalErrorException {
        String filePath = HomeDir.getPath() + SystemConstants.CONFIG_FILE;
        this.properties = readProperties(filePath);
    }

    public static synchronized PropertiesHolder getInstance() throws FatalErrorException {
        if (instance == null) {
            instance = new PropertiesHolder();
        }
        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getProperty(String propertyName, String defaultPropertyValue) {
        String propertyValue = this.properties.getProperty(propertyName, defaultPropertyValue);
        if (propertyValue.trim().isEmpty()) {
            propertyValue = defaultPropertyValue;
        }
        return propertyValue;
    }

    public Properties getProperties() {
        return this.properties;
    }

    private Properties readProperties(String fileName) throws FatalErrorException {
        Properties prop = new Properties();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(fileName);
            prop.load(fileInputStream);
        } catch (IOException e) {
            throw new FatalErrorException(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error(String.format(Messages.UNABLE_TO_CLOSE_FILE_S, fileName), e);
                }
            }
        }
        return prop;
    }
}
