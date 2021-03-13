package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerTypeResponse;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.models.EmployerModel;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployersHolder {
	private Logger LOGGER = Logger.getLogger(EmployersHolder.class);
    private static final String FIELD_SEPARATOR = ",";

	private static EmployersHolder instance;
	
	// company url
	private Map<String, EmployerModel> classifiedEmployers;
	private Map<String, EmployerModel> unclassifiedEmployers;
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

	public Collection<EmployerTypeResponse> getEmployerTypes() {
		return Arrays.stream(EmployerType.values())
				.map(EmployerTypeResponse::new)
				.collect(Collectors.toList());
	}

	private String decodeUrl(String url) {
		return URLDecoder.decode(url, StandardCharsets.UTF_8);
	}

	public Set<String> getConsolidatedUrls() {
		Collection<EmployerResponse> employers = this.getEmployers(this.unclassifiedEmployers);
		return employers.stream()
				.filter(EmployerResponse::isConsolidated)
				.map(EmployerResponse::getId)
				.map(this::decodeUrl)
				.map(this::filterUrl)
				.collect(Collectors.toCollection(TreeSet::new));
	}

	private String filterUrl(String url) {
		List<String> splitedUrl = Arrays.stream(url.split("-")).filter(item -> !item.isEmpty()).collect(Collectors.toList());
		return String.join(" ", splitedUrl);
	}

	public synchronized void setEmployerType(String employerId, EmployerType type) throws FatalErrorException, InvalidParameterException {
		if (!this.unclassifiedEmployers.containsKey(employerId)) {
			throw new InvalidParameterException(Messages.NO_SUCH_LINKEDIN_ID);
		}

		EmployerModel employer = this.unclassifiedEmployers.get(employerId);
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
		
		EmployerModel employer = this.classifiedEmployers.get(employerId);
		this.classifiedEmployers.remove(employerId, employer);
		employer.setType(EmployerType.UNDEFINED);		
		this.unclassifiedEmployers.put(employerId, employer);
		try {
			this.saveClassifiedEmployers();
		} catch (IOException e) {
			LOGGER.error(String.format(Messages.COULD_NOT_SAVE_EMPLOYERS_S, this.classifiedEmployers));
		}
	}

	public synchronized Map<String, EmployerModel> getMapClassifiedEmployers() {
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
				EmployerType employerType = EmployerType.getType(data[1]);
				Set<String> employerIds = Arrays.stream(Arrays.copyOfRange(data, 2, data.length)).collect(Collectors.toSet());

				EmployerModel employer = new EmployerModel(employerName, employerType, employerIds);
				this.classifiedEmployers.put(employerName, employer);
			}
			csvReader.close();
		} catch (IOException e) {
			LOGGER.info(Messages.COULD_NOT_LOAD_EMPLOYERS_S);
			throw new FatalErrorException(e.getMessage());
		}
	}

	private String getFormatedIds(Set<String> ids) {
		StringBuilder sb = new StringBuilder();
		ids.forEach(id -> sb.append(id).append(FIELD_SEPARATOR));
		return sb.toString();
	}
	
	public synchronized void saveClassifiedEmployers() throws IOException {
		BufferedWriter csvWriter = new BufferedWriter(new FileWriter(this.employerFilePath, false));
		for (Entry<String, EmployerModel> entry : this.classifiedEmployers.entrySet()) {
			EmployerModel employer = entry.getValue();
			String employerName = employer.getName();
			String employerType = employer.getType().getValue();
//			String ids = getFormatedIds(employer.getIds());
			String ids = employer.getName();

			csvWriter.write(employerName + FIELD_SEPARATOR + employerType + FIELD_SEPARATOR + ids + System.lineSeparator());
		}
		csvWriter.close();
	}

	private synchronized Collection<EmployerResponse> getEmployers(Map<String, EmployerModel> employers) {
		Collection<EmployerResponse> employersResponse = new LinkedList<>();
		
		for (Entry<String, EmployerModel> entry : employers.entrySet()) {
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

		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > employers.size() ?
				employers.size() : (start + pageable.getPageSize()));
		List<EmployerResponse> list = getEmployersList(employers);
		Page<EmployerResponse> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return page;
	}
	
	public synchronized Collection<EmployerResponse> getClassifiedEmployers() {
		return this.getEmployers(this.classifiedEmployers);
	}

	public synchronized void setUnclassifiedEmployers(Map<String, EmployerModel> employers) {
		this.unclassifiedEmployers = employers;
	}
	
	public synchronized Collection<EmployerResponse> getClassifiedEmployers(EmployerType type) {
		if (type == null)
			return this.getClassifiedEmployers();

		return this.getClassifiedEmployers().stream()
				.filter(employer -> employer.getType().equals(type))
				.collect(Collectors.toList());
	}

	public synchronized Page<EmployerResponse> getClassifiedEmployersPage(EmployerType employerType, int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		Collection<EmployerResponse> employers = this.getClassifiedEmployers(employerType);

		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > employers.size() ?
				employers.size() : (start + pageable.getPageSize()));
		List<EmployerResponse> list = getEmployersList(employers);
		Page<EmployerResponse> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
		return page;
	}
}
