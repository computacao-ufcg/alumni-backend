package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerTypeResponse;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.EmployerMatcher;
import br.edu.ufcg.computacao.alumni.core.models.EmployerModel;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.UnknownEmployer;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InvalidParameterException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EmployersHolder {
	private final Logger LOGGER = Logger.getLogger(EmployersHolder.class);
    private static final String FIELD_SEPARATOR = ",";

	private static EmployersHolder instance;

	// company url
	private Map<String, UnknownEmployer> unknownEmployers;
	private Map<String, ConsolidatedEmployer> classifiedEmployers;
	private Map<String, ConsolidatedEmployer> unclassifiedEmployers;
	private String employerFilePath;

	private EmployersHolder() {
		this.employerFilePath = HomeDir.getPath() + PropertiesHolder.getInstance()
			.getProperty(ConfigurationPropertyKeys.EMPLOYERS_FILE_KEY,
					ConfigurationPropertyDefaults.DEFAULT_EMPLOYER_CLASSIFICATION_FILE_NAME);
		this.loadClassifiedEmployers(this.employerFilePath);
		this.unclassifiedEmployers = new HashMap<>();
		this.unknownEmployers = new HashMap<>();
	}

	public static EmployersHolder getInstance() {
		synchronized (EmployersHolder.class) {
			if (instance == null) {
				instance = new EmployersHolder();
			}
			return instance;
		}
	}

	public Collection<UnknownEmployer> getUnknownEmployers() {
		return this.unknownEmployers.values();
	}

	public Collection<EmployerTypeResponse> getEmployerTypes() {
		return Arrays.stream(EmployerType.values())
				.map(EmployerTypeResponse::new)
				.collect(Collectors.toList());
	}

	public void setUnknownEmployerUrl(String currentLinkedinId, String newLinkedinId) throws InvalidParameterException {
		if (!this.unknownEmployers.containsKey(currentLinkedinId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}

		UnknownEmployer employer = this.unknownEmployers.get(currentLinkedinId);
		ConsolidatedEmployer consolidatedEmployer = new ConsolidatedEmployer(newLinkedinId, employer.getName(), employer.getType());
		this.unknownEmployers.remove(currentLinkedinId);
		this.unclassifiedEmployers.put(newLinkedinId, consolidatedEmployer);
	}

	public synchronized void setEmployerType(String employerId, EmployerType type) throws FatalErrorException, InvalidParameterException {
		if (!this.unclassifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}

		ConsolidatedEmployer employer = this.unclassifiedEmployers.get(employerId);
		this.unclassifiedEmployers.remove(employerId);
		employer.setType(type);
		this.classifiedEmployers.put(employerId, employer);
		try {
			this.saveClassifiedEmployers();
		} catch (IOException e) {
			LOGGER.error(String.format(Messages.COULD_NOT_SAVE_EMPLOYERS_S, this.classifiedEmployers));
		}
	}
	
	public synchronized void resetEmployerType(String employerId) throws FatalErrorException, InvalidParameterException {
		if (!this.classifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}

		ConsolidatedEmployer employer = this.classifiedEmployers.get(employerId);
		this.classifiedEmployers.remove(employerId, employer);
		employer.setType(EmployerType.UNDEFINED);		
		this.unclassifiedEmployers.put(employerId, employer);
		try {
			this.saveClassifiedEmployers();
		} catch (IOException e) {
			LOGGER.error(String.format(Messages.COULD_NOT_SAVE_EMPLOYERS_S, this.classifiedEmployers));
		}
	}

	public synchronized Map<String, ConsolidatedEmployer> getMapClassifiedEmployers() {
		return this.classifiedEmployers;
	}
	
	public synchronized void loadClassifiedEmployers(String filePath) throws FatalErrorException {
		this.classifiedEmployers = new HashMap<>();
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(FIELD_SEPARATOR);
				String employerName = data[0];
				String employerId = data[1];
				EmployerType employerType = EmployerType.getType(data[2]);

				ConsolidatedEmployer employer = new ConsolidatedEmployer(employerName, employerId, employerType);
				this.classifiedEmployers.put(employerName, employer);
			}
			csvReader.close();
		} catch (IOException e) {
			LOGGER.info(Messages.COULD_NOT_LOAD_EMPLOYERS_S);
			throw new FatalErrorException(e.getMessage());
		}
	}

	public synchronized void saveClassifiedEmployers() throws IOException {
		BufferedWriter csvWriter = new BufferedWriter(new FileWriter(this.employerFilePath, false));
		for (Entry<String, ConsolidatedEmployer> entry : this.classifiedEmployers.entrySet()) {
			ConsolidatedEmployer employer = entry.getValue();
			String employerName = employer.getName();
			String employerType = employer.getType().getValue();
			String employerId = employer.getLinkedinId();

			csvWriter.write(employerName + FIELD_SEPARATOR + employerType + FIELD_SEPARATOR + employerId + System.lineSeparator());
		}
		csvWriter.close();
	}

	private synchronized Collection<ConsolidatedEmployer> getEmployers(Map<String, ConsolidatedEmployer> employers) {
		Collection<ConsolidatedEmployer> employersResponse = new LinkedList<>();
		
		for (Entry<String, ConsolidatedEmployer> entry : employers.entrySet()) {
			String employerId = entry.getKey();
			String employerName = entry.getValue().getName();
			EmployerType type = entry.getValue().getType();
			
			ConsolidatedEmployer response = new ConsolidatedEmployer(employerName, employerId, type);
			employersResponse.add(response);
		}
		
		return employersResponse;
	}

	private synchronized List<ConsolidatedEmployer> getEmployersList(Collection<ConsolidatedEmployer> employers) {
		return new ArrayList<>(employers);
	}
	
	public synchronized Collection<ConsolidatedEmployer> getUnclassifiedEmployers() {
		return this.getEmployers(this.unclassifiedEmployers);
	}

	public synchronized Page<ConsolidatedEmployer> getUnclassifiedEmployersPage(int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<ConsolidatedEmployer> employers = this.getUnclassifiedEmployers();

		return getEmployerResponses(pageable, employers);
	}

	private Page<ConsolidatedEmployer> getEmployerResponses(Pageable pageable, Collection<ConsolidatedEmployer> employers) {
		int start = pageable.getOffset();
		int end = (Math.min((start + pageable.getPageSize()), employers.size()));
		List<ConsolidatedEmployer> list = getEmployersList(employers);
		return new PageImpl<>(list.subList(start, end), pageable, list.size());
	}

	public synchronized Collection<ConsolidatedEmployer> getClassifiedEmployers() {
		return this.classifiedEmployers.values();
	}

	public synchronized void setUnclassifiedEmployers(Map<String, ConsolidatedEmployer> employers) {
		this.unclassifiedEmployers = employers;
	}
	
	public synchronized Collection<ConsolidatedEmployer> getClassifiedEmployers(EmployerType type) {
		if (type == null)
			return this.getClassifiedEmployers();

		return this.getClassifiedEmployers()
				.stream()
				.filter(employer -> employer.getType().equals(type))
				.collect(Collectors.toList());
	}

	public void setUnknownEmployers(Map<String, EmployerModel> employers) {
		for (Entry<String, EmployerModel> entry : employers.entrySet()) {
			String linkedinId = entry.getKey();
			EmployerModel employer = entry.getValue();

			UnknownEmployer unknownEmployer = new UnknownEmployer(linkedinId, employer.getName());
			Set<ConsolidatedEmployer> possibleEmployers = EmployerMatcher.getInstance().getPossibleEmployers(unknownEmployer);
			unknownEmployer.setPossibleEmployers(possibleEmployers);

			this.unknownEmployers.put(unknownEmployer.getLinkedinId(), unknownEmployer);
		}
	}

	public synchronized Page<ConsolidatedEmployer> getClassifiedEmployersPage(EmployerType employerType, int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<ConsolidatedEmployer> employers = this.getClassifiedEmployers(employerType);

		return getEmployerResponses(pageable, employers);
	}
}
