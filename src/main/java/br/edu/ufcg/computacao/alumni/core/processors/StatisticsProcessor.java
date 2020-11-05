package br.edu.ufcg.computacao.alumni.core.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;
import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.EmployersHolder;
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
	
	private Set<UfcgAlumnusData> filterAlumniByCourseName(Collection<UfcgAlumnusData> alumni, CourseName courseName) {
		return alumni.stream()
				.filter(alumnus -> {
					Degree[] alumnusDegrees = alumnus.getGraus();
					for (Degree degree : alumnusDegrees) {
						if (degree.getCourseName().equals(courseName)) {
							return true;
						}
					}
					return false;
				}).collect(Collectors.toSet());
	}
	
	private Set<UfcgAlumnusData> filterAlumniByLevel(Collection<UfcgAlumnusData> alumni, Level level) {
		return alumni.stream()
				.filter(alumnus -> {
					Degree[] alumnusDegrees = alumnus.getGraus();
					for (Degree degree : alumnusDegrees) {
						if (degree.getLevel().equals(level)) {
							return true;
						}
					}
					return false;
				}).collect(Collectors.toSet());
	}
	
	private int getNumberMappedAlumni(Collection<UfcgAlumnusData> alumni) {
		Map<String, String> matches = MatchesHolder.getInstance().getMatches();
		
		Set<String> alumniRegistrations = alumni.stream().map(UfcgAlumnusData::getRegistration).collect(Collectors.toSet());
		Set<String> matchesRegistrations = matches.keySet();
		
		Set<String> mappedAlumni = new HashSet<>(matchesRegistrations);
		mappedAlumni.retainAll(alumniRegistrations);
		
		return mappedAlumni.size();
	}
	
	private int getNumberTypeEmployed(Collection<UfcgAlumnusData> alumni, EmployerType type) {
		Map<EmployerResponse, Collection<String>> employers = EmployersHolder.getInstance().getEmployers(type);
		
		Set<String> alumniRegistrations = alumni.stream().map(UfcgAlumnusData::getRegistration).collect(Collectors.toSet());
		Set<String> employerRegistrations = new HashSet<>();
		employers.values().forEach(registrations -> employerRegistrations.addAll(registrations));
		
		employerRegistrations.retainAll(alumniRegistrations);
		
		return employerRegistrations.size();
	}

	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Collection<UfcgAlumnusData> alumni = AlumniHolder.getInstance().getAlumniData();
				CourseName[] courseNames = CourseName.values();
				Level[] levels = Level.values();
				
				Map<CourseName, StatisticsResponse> courseStatistics = new HashMap<>();
				Map<Level, StatisticsResponse> levelStatistics = new HashMap<>();
				
				for (CourseName courseName : courseNames) {
					Collection<UfcgAlumnusData> filteredAlumniByCourse = filterAlumniByCourseName(alumni, courseName);
					
					int numberAlumniCourse = filteredAlumniByCourse.size();
					int numberMappedAlumniCourse = getNumberMappedAlumni(filteredAlumniByCourse);
					int numberAcademyEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.ACADEMY);
					int numberIndustryEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.INDUSTRY);
					int numberGovernmentEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.GOVERNMENT);
					int numberOngEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.ONG);
					int numberOthersEmployedCourse = getNumberTypeEmployed(filteredAlumniByCourse, EmployerType.OTHERS);
					
					StatisticsResponse courseStatistic = new StatisticsResponse(numberAlumniCourse, numberMappedAlumniCourse, numberAcademyEmployedCourse, numberIndustryEmployedCourse, numberGovernmentEmployedCourse, numberOngEmployedCourse, numberOthersEmployedCourse);
					
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
						
						StatisticsResponse levelStatistic = new StatisticsResponse(numberAlumniLevel, numberMappedAlumniLevel, numberAcademyEmployedLevel, numberIndustryEmployedLevel, numberGovernmentEmployedLevel, numberOngEmployedLevel, numberOthersEmployedLevel);
						
						levelStatistics.put(level, levelStatistic);
					}
				}
				
				StatisticsHolder.getInstance().setCourseNamesStatistics(courseStatistics);
				StatisticsHolder.getInstance().setLevelsStatistics(levelStatistics);
				
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(1))));
				
			} catch (InterruptedException e) {
				LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			}
		}
	}
}