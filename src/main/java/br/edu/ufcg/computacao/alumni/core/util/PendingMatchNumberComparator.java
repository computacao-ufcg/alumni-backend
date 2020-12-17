package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;

import java.util.Comparator;

public class PendingMatchNumberComparator implements Comparator<PendingMatch> {

    @Override
    public int compare(PendingMatch o1, PendingMatch o2) {
        return Integer.compare(o1.getPossibleMatches().size(), o2.getPossibleMatches().size());
    }
}
