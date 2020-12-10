package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;

public class PendingMatchNumberComparator implements SortStrategy {

    @Override
    public int compare(PendingMatch p1, PendingMatch p2) {
        return Integer.compare(p1.getPossibleMatches().size(), p2.getPossibleMatches().size());
    }
}
