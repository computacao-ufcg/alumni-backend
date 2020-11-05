package br.edu.ufcg.computacao.alumni.core.holders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;

public class EmployersHolder {
	private Logger LOGGER = Logger.getLogger(EmployersHolder.class);
	
	private static EmployersHolder instance;
	
	private String employersFilePath;
	private Map<EmployerResponse, Collection<String>> employers; // associa um empregador com uma coleção de matrículas
	
	private EmployersHolder() {
		this.employersFilePath = HomeDir.getPath() + PropertiesHolder.getInstance()
				.getProperty(ConfigurationPropertyKeys.EMPLOYERS_INPUT_KEY);
		
		this.loadEmployers(this.employersFilePath);
	}
	
	public static EmployersHolder getInstance() {
		synchronized (EmployersHolder.class) {
			if (instance == null) {
				instance = new EmployersHolder();
			}
			return instance;
		}
	}
	
	private synchronized void loadEmployers(String filePath) throws FatalErrorException {
		this.employers = new HashMap<>();
		try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(",");
				
				String registration = data[0].trim();
				String name = data[1].trim();
				String linkedinId = data[2].trim();
				EmployerType type = EmployerType.valueOf(data[3].trim());
				
				EmployerResponse employer = new EmployerResponse(name, linkedinId, type);
				
				if (!this.employers.containsKey(employer)) {
					this.employers.put(employer, new HashSet<>());
				}

				this.employers.get(employer).add(registration);
				
				LOGGER.info("");
			}
		} catch (IOException e) {
			throw new FatalErrorException(e.getMessage());
		}
	}
	
	public synchronized Map<EmployerResponse, Collection<String>> getEmployers() {
		return new HashMap<EmployerResponse, Collection<String>>(this.employers);
	}
	
	public synchronized Map<EmployerResponse, Collection<String>> getEmployers(EmployerType type) {
		Map<EmployerResponse, Collection<String>> filteredEmployers = new HashMap<>();
		
		for (Entry<EmployerResponse, Collection<String>> entry : this.employers.entrySet()) {
			EmployerResponse employer = entry.getKey();
			
			if (employer.getType().equals(type)) {
				Collection<String> registrations = entry.getValue();
				filteredEmployers.put(employer, registrations);
			}
		}
		
		return filteredEmployers;
	}
}
