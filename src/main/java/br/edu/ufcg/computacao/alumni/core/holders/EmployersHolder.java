package br.edu.ufcg.computacao.alumni.core.holders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.core.models.Employer;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class EmployersHolder {
	private Logger LOGGER = Logger.getLogger(EmployersHolder.class);
    private static final String FIELD_SEPARATOR = ",";

	private static EmployersHolder instance;
	
	// company url
	private Map<String, Employer> classifiedEmployers;
	private Map<String, Employer> unclassifiedEmployers;
	private String employerFilePath;
	
	private EmployersHolder() {
		this.employerFilePath = HomeDir.getPath() + PropertiesHolder.getInstance()
			.getProperty(ConfigurationPropertyKeys.EMPLOYERS_FILE_KEY,
					ConfigurationPropertyDefaults.DEFAULT_EMPLOYER_CLASSIFICATION_FILE_NAME);
		this.loadClassifiedEmployers(this.employerFilePath);
		this.unclassifiedEmployers = new HashMap<>();
	}
	
	public static EmployersHolder getInstance() {
		synchronized (EmployersHolder.class) {
			if (instance == null) {
				instance = new EmployersHolder();
			}
			return instance;
		}
	}
	
	public synchronized void setEmployerType(String employerId, EmployerType type) throws FatalErrorException, InvalidParameterException {
		if (!this.unclassifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		} 

		Employer employer = this.unclassifiedEmployers.get(employerId);
		this.unclassifiedEmployers.remove(employerId, employer);
		employer.setType(type);		
		this.classifiedEmployers.put(employerId, employer);
	}
	
	public synchronized void resetEmployerType(String employerId) throws FatalErrorException, InvalidParameterException {
		if (!this.classifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}
		
		Employer employer = this.classifiedEmployers.get(employerId);
		this.classifiedEmployers.remove(employerId, employer);
		employer.setType(EmployerType.UNDEFINED);		
		this.unclassifiedEmployers.put(employerId, employer);
	}
	
	public synchronized void loadClassifiedEmployers(String filePath) throws FatalErrorException {
		this.classifiedEmployers = new HashMap<>();
		try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
			String row;
			while ((row = csvReader.readLine()) != null) {
				String data[] = row.split(FIELD_SEPARATOR);
				String employerId = data[0];
				String employerName = data[1];
				EmployerType employerType = EmployerType.valueOf(data[2]);
				
				this.classifiedEmployers.put(employerId, new Employer(employerName, employerType));
			}
			csvReader.close();
		} catch (IOException e) {
			throw new FatalErrorException(e.getMessage());
		}
	}
	
	public synchronized void saveEmployers() throws IOException {
		BufferedWriter csvWriter = new BufferedWriter(new FileWriter(this.employerFilePath, false));
		for (Entry<String, Employer> entry : this.classifiedEmployers.entrySet()) {
			String employerId = entry.getKey();
			Employer employer = entry.getValue();
			String employerName = employer.getName();
			String employerType = employer.getType().getValue();
			
			csvWriter.write(employerId + FIELD_SEPARATOR + employerName + FIELD_SEPARATOR + employerType + System.lineSeparator());
		}
		csvWriter.close();
	}
	
	public synchronized void addEmployers(String employerId, String employerName, String employerType) {
		if (this.classifiedEmployers.containsKey(employerId)) {
			this.classifiedEmployers.replace(employerId, new Employer(employerName, EmployerType.valueOf(employerType)));
		} else {
			this.classifiedEmployers.put(employerId, new Employer(employerName, EmployerType.valueOf(employerType)));
		}
		try {
			this.saveEmployers();
		} catch (IOException e) {
			LOGGER.error(String.format(Messages.COULD_NOT_SAVE_EMPLOYERS_S, this.classifiedEmployers));
		}
	}
	
	private synchronized Collection<EmployerResponse> getEmployers(Map<String, Employer> employers) {
		Collection<EmployerResponse> employersResponse = new LinkedList<>();
		
		for (Entry<String, Employer> entry : employers.entrySet()) {
			String employerId = entry.getKey();
			String employerName = entry.getValue().getName();
			EmployerType type = entry.getValue().getType();
			
			EmployerResponse response = new EmployerResponse(employerName, employerId, type);
			employersResponse.add(response);
		}
		
		return employersResponse;
	}

	public synchronized Page<EmployerResponse> getEmployersPage(int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getEmployers();

		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > employers.size() ?
				employers.size() : (start + pageable.getPageSize()));
		List<EmployerResponse> list = getEmployersList(employers);
		Page<EmployerResponse> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return page;
	}

	private synchronized List<EmployerResponse> getEmployersList(Collection<EmployerResponse> employers) {
		List<EmployerResponse> employersList = new ArrayList<>();
		for (EmployerResponse employer : employers) {
			employersList.add(employer);
		}
		return employersList;
	}
	
	public synchronized Collection<EmployerResponse> getUnclassifiedEmployer() {
		return this.getEmployers(this.unclassifiedEmployers);
	}

	public synchronized Page<EmployerResponse> getUnclassifiedEmployersPage(int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getUnclassifiedEmployer();

		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > employers.size() ?
				employers.size() : (start + pageable.getPageSize()));
		List<EmployerResponse> list = getEmployersList(employers);
		Page<EmployerResponse> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return page;
	}
	
	public synchronized Collection<EmployerResponse> getEmployers() {
		return this.getEmployers(this.classifiedEmployers);
	}

	public synchronized void setEmployers(Map<String, Employer> employers) {
		this.unclassifiedEmployers = employers;
	}
	
	public synchronized Collection<EmployerResponse> getEmployers(EmployerType type) {
		return this.getEmployers().stream()
				.filter(employer -> employer.getType().equals(type))
				.collect(Collectors.toList());
	}

	public synchronized Page<EmployerResponse> getEmployersPage(int requiredPage, EmployerType type) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getEmployers(type);

		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > employers.size() ?
				employers.size() : (start + pageable.getPageSize()));
		List<EmployerResponse> list = getEmployersList(employers);
		Page<EmployerResponse> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return page;
	}
}
