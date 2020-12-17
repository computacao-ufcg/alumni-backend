package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.PossibleMatch;

import java.util.Comparator;

public class ScoreComparator implements Comparator<PossibleMatch> {

    @Override
    public int compare(PossibleMatch o1, PossibleMatch o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    }
}
