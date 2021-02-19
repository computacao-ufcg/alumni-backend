package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.UfcgAlumnusData;
import br.edu.ufcg.computacao.alumni.core.util.ScoreComparator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PendingMatch implements Comparable<PendingMatch> {
	private UfcgAlumnusData alumnus;
	private List<PossibleMatch> possibleMatches;

	public PendingMatch(UfcgAlumnusData alumnus, Collection<PossibleMatch> possibleMatches) {
		this.alumnus = alumnus;
		this.possibleMatches = new LinkedList<>(possibleMatches);
		this.possibleMatches.sort(new ScoreComparator());
	}
	
	public Collection<PossibleMatch> getPossibleMatches() {
		return new LinkedList<>(this.possibleMatches);
	}

	public UfcgAlumnusData getAlumnus() {
		return this.alumnus;
	}

	public void setPossibleMatches(List<PossibleMatch> possibleMatches) {
		this.possibleMatches = possibleMatches;
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

	@Override
	public int compareTo(PendingMatch o) {
		return Integer.compare(this.getPossibleMatches().size(), o.getPossibleMatches().size());
	}
}
