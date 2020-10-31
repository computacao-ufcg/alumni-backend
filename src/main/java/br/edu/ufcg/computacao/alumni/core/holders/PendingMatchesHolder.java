package br.edu.ufcg.computacao.alumni.core.holders;

import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import org.apache.log4j.Logger;
import java.util.*;

public class PendingMatchesHolder extends Thread {
	private Logger LOGGER = Logger.getLogger(PendingMatchesHolder.class);

	private static PendingMatchesHolder instance;
	private Collection<PendingMatch> pendingMatches;

	private PendingMatchesHolder() {
		this.pendingMatches = new HashSet<>();
	}
	
	public static PendingMatchesHolder getInstance() {
		synchronized (PendingMatchesHolder.class) {
			if (instance == null) {
				instance = new PendingMatchesHolder();
			}
		}
		return instance;
	}

	public synchronized Collection<PendingMatch> getPendingMatches() {
		return new LinkedList<>(this.pendingMatches);
	}

	public synchronized void setPendingMatches(Collection<PendingMatch> newPendingMatches) {
		this.pendingMatches = newPendingMatches;
	}
}
