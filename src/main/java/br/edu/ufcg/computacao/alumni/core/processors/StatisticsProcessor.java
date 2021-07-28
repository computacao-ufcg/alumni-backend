package br.edu.ufcg.computacao.alumni.core.processors;

import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.*;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatisticsProcessor extends Thread {
	private Logger LOGGER = Logger.getLogger(StatisticsProcessor.class);
	
	public StatisticsProcessor() {
	}

	private int getNumberMappedAlumni(Collection<UfcgAlumnusData> alumni) {
		Set<String> alumniRegistrations = alumni.stream().map(UfcgAlumnusData::getRegistration).collect(Collectors.toSet());
		Set<String> matchesRegistrations = MatchesHolder.getInstance().getMatches().keySet();
		
		Set<String> mappedAlumni = new HashSet<>(matchesRegistrations);
		mappedAlumni.retainAll(alumniRegistrations);
		
		return mappedAlumni.size();
	}
	
	private int getNumberTypeEmployed(Collection<UfcgAlumnusData> alumni, EmployerType type) {
		Collection<ConsolidatedEmployer> employers = EmployersHolder.getInstance().getClassifiedEmployers(type);

		Collection<String> employersCompanyNames = employers.stream()
				.map(ConsolidatedEmployer::getName)
				.collect(Collectors.toList());

		int num = 0;
		for (UfcgAlumnusData alumnus : alumni) {
			String linkedinUrl = MatchesHolder.getInstance().getLinkedinId(alumnus.getRegistration());
			
			Collection<CurrentJob> currentJobs = LinkedinDataHolder.getInstance().getAlumnusCurrentJob(linkedinUrl);

			for (CurrentJob currentJob : currentJobs) {
				if (employersCompanyNames.contains(currentJob.getCurrentJob())) {
					num++;
				}
			}
		}
		
		return num;
	}

	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Collection<UfcgAlumnusData> alumni = AlumniHolder.getInstance().getAlumniData();

				int numberAlumni = alumni.size();
				int numberMappedAlumni = getNumberMappedAlumni(alumni);
				int numberAcademyEmployed = getNumberTypeEmployed(alumni, EmployerType.ACADEMY);
				int numberGovernmentEmployed = getNumberTypeEmployed(alumni, EmployerType.GOVERNMENT);
				int numberOngEmployed = getNumberTypeEmployed(alumni, EmployerType.ONG);
				int numberPublicCompanyEmployed = getNumberTypeEmployed(alumni, EmployerType.PUBLIC_COMPANY);
				int numberPrivateCompanyEmployed = getNumberTypeEmployed(alumni, EmployerType.PRIVATE_COMPANY);
				int numberMixedCompanyEmployed = getNumberTypeEmployed(alumni, EmployerType.MIXED_COMPANY);
				int numberConsolidatedEmployers = EmployersHolder.getInstance().getTotalConsolidatedEmployers();
				int numberTotalEmployers = EmployersHolder.getInstance().getTotalEmployers();

				StatisticsModel statistics = new StatisticsModel(numberAlumni, numberMappedAlumni, numberAcademyEmployed, numberGovernmentEmployed,
						numberOngEmployed, numberPublicCompanyEmployed, numberPrivateCompanyEmployed, numberMixedCompanyEmployed, numberConsolidatedEmployers, numberTotalEmployers);

				StatisticsHolder.getInstance().setStatistics(statistics);

				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
			} catch (InterruptedException e) {
				isActive = false;
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}