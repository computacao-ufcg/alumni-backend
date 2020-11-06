package br.edu.ufcg.computacao.alumni.core.holders;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.StatisticsResponse;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;

public class StatisticsHolder {
	private Logger LOGGER = Logger.getLogger(StatisticsHolder.class);
	
	private static StatisticsHolder instance;
		
	private Map<CourseName, StatisticsResponse> courseNamesStatistics;
	private Map<Level, StatisticsResponse> levelsStatistics;
	
	private StatisticsHolder() {
		this.courseNamesStatistics = new HashMap<>();
		this.levelsStatistics = new HashMap<>();
	}
	
	public static StatisticsHolder getInstance() {
		synchronized (StatisticsHolder.class) {
			if (instance == null) {
				instance = new StatisticsHolder();
			}
			return instance;
		}
	}
	
	public synchronized void setLevelsStatistics(Map<Level, StatisticsResponse> statistics) {
		this.levelsStatistics = statistics;
	}
	
	public synchronized void setCourseNamesStatistics(Map<CourseName, StatisticsResponse> statistics) {
		this.courseNamesStatistics = statistics;
	}
	
	public synchronized StatisticsResponse getStatistics(CourseName courseName) {
		return this.courseNamesStatistics.get(courseName);
	}
	
	public synchronized StatisticsResponse getStatistics(Level level) {
		return this.levelsStatistics.get(level);
	}
}
