package br.edu.ufcg.computacao.alumni.core.processors;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

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
	
	// implementar eurística
	private synchronized EmployerType getEmployerType(String jobTitle) {
		return EmployerType.valueOf(jobTitle);
	}
	
	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Collection<EmployerResponse> consolidatedEmployers = EmployersHolder.getInstance().getEmployers().keySet(); 
				Collection<LinkedinAlumnusData> linkedinProfiles = LinkedinDataHolder.getInstance().getLinkedinAlumniData();
				
				for (LinkedinAlumnusData profile : linkedinProfiles) {
					LinkedinJobData[] jobData = profile.getJobs();

					for (LinkedinJobData job : jobData) {
						String name = job.getCompanyName();
						String linkedinId = job.getCompanyUrl();
						EmployerType type = getEmployerType(job.getJobTitle());
						
						EmployerResponse employer = new EmployerResponse(name, linkedinId, type);
						
						if (!consolidatedEmployers.contains(employer)) {
							consolidatedEmployers.add(employer);
						}
					}
				}
				
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
			} catch (InterruptedException e) {
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}
