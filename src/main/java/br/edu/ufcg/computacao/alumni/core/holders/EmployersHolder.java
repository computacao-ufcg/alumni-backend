package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerTypeResponse;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmployersHolder {
	private final Logger LOGGER = Logger.getLogger(EmployersHolder.class);
    private static final String FIELD_SEPARATOR = ",";

	private static EmployersHolder instance;
	
	// company url
	private Collection<EmployerResponse> unknownEmployers;
	private Map<String, EmployerResponse> classifiedEmployers;
	private Map<String, EmployerResponse> unclassifiedEmployers;
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

	public Collection<EmployerResponse> getUnknownEmployers() {
		return this.unknownEmployers;
	}

	public Collection<EmployerTypeResponse> getEmployerTypes() {
		return Arrays.stream(EmployerType.values())
				.map(EmployerTypeResponse::new)
				.collect(Collectors.toList());
	}

	private EmployerResponse getUnknownEmployer(String linkedinId) {
		for (EmployerResponse employer : this.unknownEmployers) {
			if (employer.getLinkedinId().equals(linkedinId))
				return employer;
		}
		return null;
	}

	public void consolidateUrls() {
		List<EmployerResponse> consolidatedEmployers = new ArrayList<>(this.unclassifiedEmployers.values());
		List<EmployerResponse> notConsolidatedEmployers = new ArrayList<>(this.unknownEmployers);

		LOGGER.info(String.format("%d consolidados antes do metodo\n", consolidatedEmployers.size()));
		LOGGER.info(String.format("%d nao consolidados antes do metodo\n", notConsolidatedEmployers.size()));

		int c = 0;
		for (EmployerResponse unknownEmployer: notConsolidatedEmployers) {
			Pattern urlPattern = Pattern.compile(unknownEmployer.getName(), Pattern.CASE_INSENSITIVE);
			for (EmployerResponse consolidatedEmployer: consolidatedEmployers) {
				Matcher nameMatcher = urlPattern.matcher(consolidatedEmployer.getName());
				if(nameMatcher.find()) {
					String consolidatedId = consolidatedEmployer.getLinkedinId();
					c++;

					this.unknownEmployers.remove(getUnknownEmployer(unknownEmployer.getLinkedinId()));
					this.unclassifiedEmployers.put(unknownEmployer.getLinkedinId(), unknownEmployer);

					unknownEmployer.setLinkedinId(consolidatedId);
					unknownEmployer.setIsConsolidated(true);
//					break;
				}
			}
		}

		LOGGER.info(String.format("entrei %d vezes no if", c));
		LOGGER.info(String.format("%d consolidados depois do metodo\n", this.unclassifiedEmployers.size()));
		LOGGER.info(String.format("%d nao consolidados depois do metodo\n", this.unknownEmployers.size()));
	}

	public synchronized void setEmployerType(String employerId, EmployerType type) throws FatalErrorException, InvalidParameterException {
		if (!this.unclassifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}

		EmployerResponse employer = this.unclassifiedEmployers.get(employerId);
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
		
		EmployerResponse employer = this.classifiedEmployers.get(employerId);
		this.classifiedEmployers.remove(employerId, employer);
		employer.setType(EmployerType.UNDEFINED);		
		this.unclassifiedEmployers.put(employerId, employer);
		try {
			this.saveClassifiedEmployers();
		} catch (IOException e) {
			LOGGER.error(String.format(Messages.COULD_NOT_SAVE_EMPLOYERS_S, this.classifiedEmployers));
		}
	}

	public synchronized Map<String, EmployerResponse> getMapClassifiedEmployers() {
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

				EmployerResponse employer = new EmployerResponse(employerName, employerId, employerType);
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
		for (Entry<String, EmployerResponse> entry : this.classifiedEmployers.entrySet()) {
			EmployerResponse employer = entry.getValue();
			String employerName = employer.getName();
			String employerType = employer.getType().getValue();
			String employerId = employer.getLinkedinId();

			csvWriter.write(employerName + FIELD_SEPARATOR + employerType + FIELD_SEPARATOR + employerId + System.lineSeparator());
		}
		csvWriter.close();
	}

	private synchronized Collection<EmployerResponse> getEmployers(Map<String, EmployerResponse> employers) {
		Collection<EmployerResponse> employersResponse = new LinkedList<>();
		
		for (Entry<String, EmployerResponse> entry : employers.entrySet()) {
			String employerId = entry.getKey();
			String employerName = entry.getValue().getName();
			EmployerType type = entry.getValue().getType();
			
			EmployerResponse response = new EmployerResponse(employerName, employerId, type);
			employersResponse.add(response);
		}
		
		return employersResponse;
	}

	private synchronized List<EmployerResponse> getEmployersList(Collection<EmployerResponse> employers) {
		return new ArrayList<>(employers);
	}
	
	public synchronized Collection<EmployerResponse> getUnclassifiedEmployers() {
		return this.getEmployers(this.unclassifiedEmployers);
	}

	public synchronized Page<EmployerResponse> getUnclassifiedEmployersPage(int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getUnclassifiedEmployers();

		return getEmployerResponses(pageable, employers);
	}

	private Page<EmployerResponse> getEmployerResponses(Pageable pageable, Collection<EmployerResponse> employers) {
		int start = pageable.getOffset();
		int end = (Math.min((start + pageable.getPageSize()), employers.size()));
		List<EmployerResponse> list = getEmployersList(employers);
		return new PageImpl<>(list.subList(start, end), pageable, list.size());
	}

	public synchronized Collection<EmployerResponse> getClassifiedEmployers() {
		return this.classifiedEmployers.values();
	}

	public synchronized void setUnclassifiedEmployers(Map<String, EmployerResponse> employers) {
		this.unclassifiedEmployers = employers;
	}
	
	public synchronized Collection<EmployerResponse> getClassifiedEmployers(EmployerType type) {
		if (type == null)
			return this.getClassifiedEmployers();

		return this.getClassifiedEmployers()
				.stream()
				.filter(employer -> employer.getType().equals(type))
				.collect(Collectors.toList());
	}

	public void setUnknownEmployers(Collection<EmployerResponse> employers) {
		this.unknownEmployers = employers;
	}

	public synchronized Page<EmployerResponse> getClassifiedEmployersPage(EmployerType employerType, int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getClassifiedEmployers(employerType);

		return getEmployerResponses(pageable, employers);
	}
}
