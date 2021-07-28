package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;
import org.apache.log4j.Logger;

public class StatisticsHolder {
	private Logger LOGGER = Logger.getLogger(StatisticsHolder.class);
	
	private static StatisticsHolder instance;

	private StatisticsModel statistics;

	private StatisticsHolder() {
		this.statistics = new StatisticsModel();
	}
	
	public static StatisticsHolder getInstance() {
		synchronized (StatisticsHolder.class) {
			if (instance == null) {
				instance = new StatisticsHolder();
			}
			return instance;
		}
	}

	public synchronized void setStatistics(StatisticsModel statistics) {
		this.statistics = statistics;
	}
	
	public synchronized StatisticsModel getStatistics() {
		return this.statistics;
	}
}
