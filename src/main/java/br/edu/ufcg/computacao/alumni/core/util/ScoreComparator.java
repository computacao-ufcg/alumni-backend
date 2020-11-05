package br.edu.ufcg.computacao.alumni.core.util;

import java.util.Comparator;

public class ScoreComparator implements Comparator<String> {

	@Override
	public int compare(String score1, String score2) {
		Integer score1Int = Integer.parseInt(score1);
		Integer score2Int = Integer.parseInt(score2);
		
		return score2Int.compareTo(score1Int);
	}

}
