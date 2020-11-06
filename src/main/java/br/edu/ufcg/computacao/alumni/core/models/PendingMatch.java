package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.util.ScoreComparator;

public class PendingMatch {

	private UfcgAlumnusData alumni;
	private Map<String, Collection<LinkedinAlumnusData>> possibleMatches;
	
	public PendingMatch(UfcgAlumnusData alumni, Map<String, Collection<LinkedinAlumnusData>> possibleMatches) {
		this.alumni = alumni;
		this.possibleMatches = possibleMatches;
	}
	
	public Map<String, Collection<LinkedinAlumnusData>> getPossibleMatches() {
		return new TreeMap<>(new ScoreComparator());
	}
	
	public Collection<LinkedinAlumnusData> getPossibleMatchesFromScore(String score) {
		return this.possibleMatches.get(score);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alumni == null) ? 0 : alumni.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PendingMatch other = (PendingMatch) obj;
		if (alumni == null) {
			if (other.alumni != null)
				return false;
		} else if (!alumni.equals(other.alumni))
			return false;
		return true;
	}
	
}
