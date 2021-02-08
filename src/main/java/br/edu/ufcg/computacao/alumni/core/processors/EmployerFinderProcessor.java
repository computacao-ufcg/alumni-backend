package br.edu.ufcg.computacao.alumni.core.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.computacao.alumni.core.models.Employer;
import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.EmployersHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;

public class EmployerFinderProcessor extends Thread {
	private Logger LOGGER = Logger.getLogger(EmployerFinderProcessor.class);
	
	public EmployerFinderProcessor() {
	}
	
	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Collection<EmployerResponse> classifiedEmployers = EmployersHolder.getInstance().getClassifiedEmployers();
				Collection<LinkedinAlumnusData> linkedinProfiles = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
				Map<String, Employer> newEmployers = new HashMap<>();
				
				for (LinkedinAlumnusData profile : linkedinProfiles) {
					LinkedinJobData[] jobData = profile.getJobs();

					for (LinkedinJobData job : jobData) {
						String name = job.getCompanyName();
						String linkedinId = job.getCompanyUrl();
						
						EmployerResponse employer = new EmployerResponse(name, linkedinId, EmployerType.UNDEFINED);
						
						if (!classifiedEmployers.contains(employer)) {
							newEmployers.put(employer.getLinkedinId(), new Employer(name, EmployerType.UNDEFINED));
							LOGGER.debug(String.format(Messages.ADD_EMPLOYER_S, name));
						}
					}
				}
				EmployersHolder.getInstance().setUnclassifiedEmployers(newEmployers);
				
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
			} catch (InterruptedException e) {
				isActive = false;
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}
