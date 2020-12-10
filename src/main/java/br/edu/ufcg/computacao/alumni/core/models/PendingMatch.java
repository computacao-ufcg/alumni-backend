package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.util.ScoreComparator;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class PendingMatch {
	private UfcgAlumnusData alumnus;
	private Map<String, Collection<LinkedinAlumnusData>> possibleMatches;
	
	public PendingMatch(UfcgAlumnusData alumnus, Map<String, Collection<LinkedinAlumnusData>> possibleMatches) {
		this.alumnus = alumnus;
		this.possibleMatches = new TreeMap<>(new ScoreComparator());
		this.possibleMatches.putAll(possibleMatches);
	}
	
	public Map<String, Collection<LinkedinAlumnusData>> getPossibleMatches() {
		Map<String, Collection<LinkedinAlumnusData>> map = new TreeMap<>(new ScoreComparator());
		map.putAll(this.possibleMatches);
		return map;
	}

	public UfcgAlumnusData getAlumnus() {
		return this.alumnus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alumnus == null) ? 0 : alumnus.hashCode());
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
		if (alumnus == null) {
			if (other.alumnus != null)
				return false;
		} else if (!alumnus.equals(other.alumnus))
			return false;
		return true;
	}

}
