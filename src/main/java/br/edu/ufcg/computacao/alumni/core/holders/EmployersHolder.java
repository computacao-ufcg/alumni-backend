package br.edu.ufcg.computacao.alumni.core.holders;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.core.models.Employer;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;

public class EmployersHolder {
	private Logger LOGGER = Logger.getLogger(EmployersHolder.class);
	
	private static EmployersHolder instance;
	
	// company url
	private Map<String, Employer> classifiedEmployers; // associa um empregador com uma coleção de matrículas
	private Map<String, Employer> unclassifiedEmployers;
	
	private EmployersHolder() {
		this.classifiedEmployers = new HashMap<>();
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
	
	public synchronized void setEmployerType(String employerId, EmployerType type) throws FatalErrorException {
		if (!this.unclassifiedEmployers.containsKey(employerId)) {
			throw new FatalErrorException();
		} 

		Employer employer = this.unclassifiedEmployers.get(employerId);
		this.unclassifiedEmployers.remove(employerId, employer);
		employer.setType(type);		
		this.classifiedEmployers.put(employerId, employer);
	}
	
	// se o tipo será resetado, então o novo tipo é automaticamente "undefined"
	public synchronized void resetEmployerType(String employerId) throws FatalErrorException {
		if (!this.classifiedEmployers.containsKey(employerId)) {
			throw new FatalErrorException();
		}
		
		Employer employer = this.classifiedEmployers.get(employerId);
		this.classifiedEmployers.remove(employerId, employer);
		employer.setType(EmployerType.UNDEFINED);		
		this.unclassifiedEmployers.put(employerId, employer);
	}
	
	private synchronized Collection<EmployerResponse> getEmployers(Map<String, Employer> employers) {
		Collection<EmployerResponse> employersResponse = new LinkedList<>();
		
		for (Entry<String, Employer> entry : this.classifiedEmployers.entrySet()) {
			String employerId = entry.getKey();
			String employerName = entry.getValue().getName();
			EmployerType type = entry.getValue().getType();
			
			EmployerResponse response = new EmployerResponse(employerName, employerId, type);
			employersResponse.add(response);
		}
		
		return employersResponse;
	}
	
	public synchronized Collection<EmployerResponse> getUnsclassifiedEmployer() {
		return this.getEmployers(this.unclassifiedEmployers);
	}
	
	public synchronized Collection<EmployerResponse> getEmployers() {
		return this.getEmployers(this.classifiedEmployers);
	}
	
	public synchronized Collection<EmployerResponse> getEmployers(EmployerType type) {
		return this.getEmployers().stream()
				.filter(employer -> employer.getType().equals(type))
				.collect(Collectors.toList());
	}
}
