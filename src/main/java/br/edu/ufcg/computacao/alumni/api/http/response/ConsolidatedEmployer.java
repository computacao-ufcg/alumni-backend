package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.EmployerModel;
import br.edu.ufcg.computacao.alumni.core.models.EmployerType;

public class ConsolidatedEmployer extends EmployerModel {

    public ConsolidatedEmployer(String name, String linkedinId, EmployerType type) {
        super(linkedinId, name, type);
    }

}
