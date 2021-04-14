package br.edu.ufcg.computacao.alumni.core.processors;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.*;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatisticsProcessor extends Thread {
	private Logger LOGGER = Logger.getLogger(StatisticsProcessor.class);
	
	public StatisticsProcessor() {
	}
	
	private Set<UfcgAlumnusData> filterAlumniByCourseName(Collection<UfcgAlumnusData> alumni, CourseName courseName) {
		return alumni.stream()
				.filter(alumnus -> alumnus.getDegree().getCourseName().equals(courseName))
				.collect(Collectors.toSet());
	}
	
	private Set<UfcgAlumnusData> filterAlumniByLevel(Collection<UfcgAlumnusData> alumni, Level level) {
		return alumni.stream()
				.filter(alumnus -> alumnus.getDegree().getLevel().equals(level))
				.collect(Collectors.toSet());
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
				CourseName[] courseNames = CourseName.values();
				Level[] levels = Level.values();
				
				Map<CourseName, StatisticsModel> courseStatistics = new HashMap<>();
				Map<Level, StatisticsModel> levelStatistics = new HashMap<>();
				
				for (CourseName courseName : courseNames) {
					Collection<UfcgAlumnusData> filteredAlumniByCourse = filterAlumniByCourseName(alumni, courseName);
					
					int numberAlumniCourse = filteredAlumniByCourse.size();
					int numberMappedAlumniCourse = getNumberMappedAlumni(filteredAlumniByCourse);
					int numberAcademyEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.ACADEMY);
					int numberGovernmentEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.GOVERNMENT);
					int numberOngEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.ONG);
					int numberPublicCompanyEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.PUBLIC_COMPANY);
					int numberPrivateCompanyEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.PRIVATE_COMPANY);
					int numberMixedCompanyEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.MIXED_COMPANY);

					StatisticsModel courseStatistic = new StatisticsModel(numberAlumniCourse, numberMappedAlumniCourse, numberAcademyEmployedCourse,
							numberGovernmentEmployedCourse, numberOngEmployedCourse
							, numberPublicCompanyEmployedCourse, numberPrivateCompanyEmployedCourse, numberMixedCompanyEmployedCourse);
					
					courseStatistics.put(courseName, courseStatistic);
					
					for (Level level : levels) {
						Collection<UfcgAlumnusData> filteredAlumniByLevel = filterAlumniByLevel(alumni, level);
						
						int numberAlumniLevel = filteredAlumniByLevel.size();
						int numberMappedAlumniLevel = getNumberMappedAlumni(filteredAlumniByLevel);
						int numberAcademyEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.ACADEMY);
						int numberGovernmentEmployedLevel= getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.GOVERNMENT);
						int numberOngEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.ONG);
						int numberPublicCompanyEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.PUBLIC_COMPANY);
						int numberPrivateCompanyEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.PRIVATE_COMPANY);
						int numberMixedCompanyEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.MIXED_COMPANY);

						StatisticsModel levelStatistic = new StatisticsModel(numberAlumniLevel, numberMappedAlumniLevel, numberAcademyEmployedLevel,
								numberGovernmentEmployedLevel, numberOngEmployedLevel,
								numberPublicCompanyEmployedLevel, numberPrivateCompanyEmployedLevel, numberMixedCompanyEmployedLevel);
						
						levelStatistics.put(level, levelStatistic);
					}
				}
				
				StatisticsHolder.getInstance().setCourseNamesStatistics(courseStatistics);
				StatisticsHolder.getInstance().setLevelsStatistics(levelStatistics);
				
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
				
			} catch (InterruptedException e) {
				isActive = false;
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}