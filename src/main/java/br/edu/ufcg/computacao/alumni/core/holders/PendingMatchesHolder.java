package br.edu.ufcg.computacao.alumni.core.holders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;

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

	public synchronized Page<PendingMatch> getPendingMatchesPage(int requiredPage) {
		Pageable pageable= new PageRequest(requiredPage, 10);
		int start = (int) pageable.getOffset();
		int end = (int) ((start + pageable.getPageSize()) > this.pendingMatches.size() ?
				this.pendingMatches.size() : (start + pageable.getPageSize()));
		List<PendingMatch> list = getPendingMatchesList();
		Page<PendingMatch> page = new PageImpl<PendingMatch>(list.subList(start, end), pageable, list.size());
		return page;
	}

	private synchronized List<PendingMatch> getPendingMatchesList() {
		List<PendingMatch> pendingMatchesList = new ArrayList<PendingMatch>();
		for (PendingMatch pendingMatch : this.pendingMatches) {
			pendingMatchesList.add(pendingMatch);
		}
		return pendingMatchesList;
	}

	public synchronized void setPendingMatches(Collection<PendingMatch> newPendingMatches) {
		this.pendingMatches = newPendingMatches;
	}
}
