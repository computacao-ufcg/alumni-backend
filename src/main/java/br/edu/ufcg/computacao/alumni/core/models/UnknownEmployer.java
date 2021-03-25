package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;

import java.util.HashSet;
import java.util.Set;

public class UnknownEmployer extends EmployerModel {

    private Set<ConsolidatedEmployer> possibleEmployers;

    public UnknownEmployer(String linkedinId, String name, Set<ConsolidatedEmployer> possibleEmployers) {
        super(linkedinId, name, EmployerType.UNDEFINED);
        this.possibleEmployers = possibleEmployers;
    }

    public UnknownEmployer(String linkedinId, String name) {
        this(linkedinId, name, new HashSet<>());
    }

    public Set<ConsolidatedEmployer> getPossibleEmployers() {
        return possibleEmployers;
    }

    public void setPossibleEmployers(Set<ConsolidatedEmployer> possibleEmployers) {
        this.possibleEmployers = possibleEmployers;
    }
}
