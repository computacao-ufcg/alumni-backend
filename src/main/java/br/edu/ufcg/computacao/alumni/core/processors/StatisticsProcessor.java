package br.edu.ufcg.computacao.alumni.core.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.CurrentJob;
import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.EmployersHolder;
import br.edu.ufcg.computacao.alumni.core.holders.LinkedinDataHolder;
import br.edu.ufcg.computacao.alumni.core.holders.MatchesHolder;
import br.edu.ufcg.computacao.alumni.core.holders.StatisticsHolder;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Degree;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;
import br.edu.ufcg.computacao.alumni.core.models.Level;

public class StatisticsProcessor extends Thread {
	private Logger LOGGER = Logger.getLogger(StatisticsProcessor.class);
	
	public StatisticsProcessor() {
	}
	
	public Set<UfcgAlumnusData> filterAlumniByCourseName(Collection<UfcgAlumnusData> alumni, CourseName courseName) {
		return alumni.stream()
				.filter(alumnus -> alumnus.getDegree().getCourseName().equals(courseName))
				.collect(Collectors.toSet());
	}
	
	public Set<UfcgAlumnusData> filterAlumniByLevel(Collection<UfcgAlumnusData> alumni, Level level) {
		return alumni.stream()
				.filter(alumnus -> alumnus.getDegree().getLevel().equals(level))
				.collect(Collectors.toSet());
	}
	
	public int getNumberMappedAlumni(Collection<UfcgAlumnusData> alumni) {
		Set<String> alumniRegistrations = alumni.stream().map(UfcgAlumnusData::getRegistration).collect(Collectors.toSet());
		Set<String> matchesRegistrations = MatchesHolder.getInstance().getMatches().keySet();
		
		Set<String> mappedAlumni = new HashSet<>(matchesRegistrations);
		mappedAlumni.retainAll(alumniRegistrations);
		
		return mappedAlumni.size();
	}
	
	public int getNumberTypeEmployed(Collection<UfcgAlumnusData> alumni, EmployerType type) {
		Collection<EmployerResponse> employers = EmployersHolder.getInstance().getClassifiedEmployers(type);

		Collection<String> employersCompanyNames = employers.stream()
				.map(EmployerResponse::getName)
				.collect(Collectors.toList());

		int num = 0;
		for (UfcgAlumnusData alumnus : alumni) {
			String alumnusFullName = alumnus.getFullName();
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
					int numberIndustryEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.INDUSTRY);
					int numberGovernmentEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.GOVERNMENT);
					int numberOngEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.ONG);
					int numberOthersEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.OTHERS);
					
					StatisticsModel courseStatistic = new StatisticsModel(numberAlumniCourse, numberMappedAlumniCourse, numberAcademyEmployedCourse, numberIndustryEmployedCourse, numberGovernmentEmployedCourse, numberOngEmployedCourse, numberOthersEmployedCourse);
					
					courseStatistics.put(courseName, courseStatistic);
					
					for (Level level : levels) {
						Collection<UfcgAlumnusData> filteredAlumniByLevel = filterAlumniByLevel(alumni, level);
						
						int numberAlumniLevel = filteredAlumniByLevel.size();
						int numberMappedAlumniLevel = getNumberMappedAlumni(filteredAlumniByLevel);
						int numberAcademyEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.ACADEMY);
						int numberIndustryEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.INDUSTRY);
						int numberGovernmentEmployedLevel= getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.GOVERNMENT);
						int numberOngEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.ONG);
						int numberOthersEmployedLevel = getNumberTypeEmployed(filteredAlumniByLevel, EmployerType.OTHERS);
						
						StatisticsModel levelStatistic = new StatisticsModel(numberAlumniLevel, numberMappedAlumniLevel, numberAcademyEmployedLevel, numberIndustryEmployedLevel, numberGovernmentEmployedLevel, numberOngEmployedLevel, numberOthersEmployedLevel);
						
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