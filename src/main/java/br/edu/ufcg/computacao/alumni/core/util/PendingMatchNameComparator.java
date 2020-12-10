package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;

public class PendingMatchNameComparator implements SortStrategy {

    @Override
    public int compare(PendingMatch p1, PendingMatch p2) {
        return p1.getAlumnus().getFullName().toLowerCase().compareTo(p2.getAlumnus().getFullName().toLowerCase());
    }
}
