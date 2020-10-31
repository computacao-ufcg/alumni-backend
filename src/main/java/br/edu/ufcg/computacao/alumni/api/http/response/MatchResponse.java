package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.api.parameters.MatchParameter;

public class MatchResponse extends MatchParameter {
    public MatchResponse(String registration, String linkedinId) {
        super(registration, linkedinId);
    }
}
