package br.edu.ufcg.computacao.alumni.core.processors;

import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.EmployersHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.models.EmployerModel;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EmployerFinderProcessor extends Thread {
	private Logger LOGGER = Logger.getLogger(EmployerFinderProcessor.class);
	
	public EmployerFinderProcessor() {
	}
	
	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Map<String, ConsolidatedEmployer> classifiedEmployers = EmployersHolder.getInstance().getMapClassifiedEmployers();
				Map<String, ConsolidatedEmployer> unclassifiedEmployers = EmployersHolder.getInstance().getMapUnclassifiedEmployers();
				Map<String, ConsolidatedEmployer> newEmployers = new HashMap<>();
				Map<String, EmployerModel> unknownEmployers = new HashMap<>();

				Collection<LinkedinAlumnusData> linkedinProfiles = LinkedinDataHolder.getInstance().getLinkedinAlumniData();

				for (LinkedinAlumnusData profile : linkedinProfiles) {
					LinkedinJobData[] jobData = profile.getJobs();

					for (LinkedinJobData job : jobData) {
						String name = job.getCompanyName();
						String linkedinId = job.getCompanyUrl();

						if (!classifiedEmployers.containsKey(linkedinId)) {
						  	if (!job.getEmployer().isConsolidated()) {
								unknownEmployers.put(linkedinId, job.getEmployer());
							} else if (!unclassifiedEmployers.containsKey(linkedinId)) {
								newEmployers.put(linkedinId, new ConsolidatedEmployer(name, linkedinId, EmployerType.UNDEFINED));
								LOGGER.debug(String.format(Messages.ADD_EMPLOYER_S, name));
							}
						}
					}
				}

				EmployersHolder.getInstance().setUnclassifiedEmployers(newEmployers);
				EmployersHolder.getInstance().setUnknownEmployers(unknownEmployers);
				
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
			} catch (InterruptedException e) {
				isActive = false;
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}
