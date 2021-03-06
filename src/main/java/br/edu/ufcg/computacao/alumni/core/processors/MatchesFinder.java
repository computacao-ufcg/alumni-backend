package br.edu.ufcg.computacao.alumni.core.processors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import org.apache.log4j.Logger;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.alumni.constants.Messages;
import br.edu.ufcg.computacao.alumni.core.Match;
import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
import br.edu.ufcg.computacao.alumni.core.holders.MatchesHolder;
import br.edu.ufcg.computacao.alumni.core.holders.PropertiesHolder;
import br.edu.ufcg.computacao.alumni.core.models.DateRange;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.models.SchoolName;

public class MatchesFinder extends Thread {
	private Logger LOGGER = Logger.getLogger(MatchesFinder.class);
	
	private static MatchesFinder instance;
	private Collection<PendingMatch> pendingMatches;
	private SchoolName schoolName;

	private MatchesFinder() throws FatalErrorException {
		this.pendingMatches = new HashSet<>();
		try {
			String path = HomeDir.getPath();
			this.loadSchoolName(path + PropertiesHolder.getInstance().
					getProperty(ConfigurationPropertyKeys.SCHOOL_INPUT_KEY));
		} catch (IOException e) {
			throw new FatalErrorException(e.getMessage());
		}
	}
	
	public static MatchesFinder getInstance() {
		synchronized (MatchesFinder.class) {
			if (instance == null) {
				instance = new MatchesFinder();
			}
		}
		return instance;
	}

	public synchronized Collection<PendingMatch> getPendingMatches() {
		return new LinkedList<>(this.pendingMatches);
	}
	
	private synchronized void loadSchoolName(String filePath) throws IOException {
		BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
		String row;
		
		List<String> schoolNamesList = new ArrayList<>();
		List<DateRange> dateRangesList = new ArrayList<>();
		
		while ((row = csvReader.readLine()) != null) {
			String[] data = row.split(",");
			String schoolName = data[0].trim();
			DateRange dateRange = new DateRange(data[1].trim());

			schoolNamesList.add(schoolName);
			dateRangesList.add(dateRange);
		}
		
		csvReader.close();
		
		String[] names = new String[schoolNamesList.size()];
		DateRange[] dateRanges = new DateRange[dateRangesList.size()];
		
		for (int i = 0; i < schoolNamesList.size(); i++) {
			names[i] = schoolNamesList.get(i);
		}
		
		for (int i = 0; i < dateRangesList.size(); i++) {
			dateRanges[i] = dateRangesList.get(i);
		}
		
		this.schoolName = new SchoolName(names, dateRanges);
	}

	@Override
	public void run() {
		boolean isActive = true;
		
		while (isActive) {
			try {
				Map<String, String> consolidatedMatches = MatchesHolder.getInstance().getMatches();
				Collection<UfcgAlumnusData> alumni = AlumniHolder.getInstance().getAlumniData();
				Collection<PendingMatch> newPendingMatches = new LinkedList<>();
			
				for (UfcgAlumnusData alumnus : alumni) {
					String registration = alumnus.getRegistration();
					
					if (!consolidatedMatches.containsKey(registration)) {
						Map<Integer, Collection<LinkedinAlumnusData>> possibleMatches =
								Match.getInstance().getMatches(alumnus, schoolName);
						
						if (!possibleMatches.isEmpty()) {
							newPendingMatches.add(new PendingMatch(alumnus, possibleMatches));
						}
					}
				}
				this.pendingMatches = newPendingMatches;
				Thread.sleep(Long.parseLong(Long.toString(TimeUnit.MINUTES.toMillis(10))));
				
			} catch (InterruptedException e) {
				isActive = false;
                LOGGER.error(Messages.THREAD_HAS_BEEN_INTERRUPTED, e);
			} catch (Exception e) {
				LOGGER.error(Messages.COULD_NOT_LOAD_ALUMNI_DATA, e);
			}
		}
	}
}
