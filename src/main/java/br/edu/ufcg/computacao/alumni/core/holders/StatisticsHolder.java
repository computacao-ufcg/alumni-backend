package br.edu.ufcg.computacao.alumni.core.holders;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Level;

public class StatisticsHolder {
	private Logger LOGGER = Logger.getLogger(StatisticsHolder.class);
	
	private static StatisticsHolder instance;
		
	private Map<CourseName, StatisticsModel> courseNamesStatistics;
	private Map<Level, StatisticsModel> levelsStatistics;
	
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
	
	public synchronized void setLevelsStatistics(Map<Level, StatisticsModel> statistics) {
		this.levelsStatistics = statistics;
	}
	
	public synchronized void setCourseNamesStatistics(Map<CourseName, StatisticsModel> statistics) {
		this.courseNamesStatistics = statistics;
	}
	
	public synchronized StatisticsModel getStatistics(CourseName courseName) {
		return this.courseNamesStatistics.get(courseName);
	}
	
	public synchronized StatisticsModel getStatistics(Level level) {
		return this.levelsStatistics.get(level);
	}
}
