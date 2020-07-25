package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class Alumni {
    @ApiModelProperty(example = ApiDocumentation.Model.ALUMNI)
    private LinkedinAlumnusData alumni[];

    public Alumni(LinkedinAlumnusData alumni[]) {
        this.alumni = alumni;
    }

    public LinkedinAlumnusData[] getAlumni() {
        return alumni;
    }

    public void setAlumni(LinkedinAlumnusData alumni[]) {
        this.alumni = alumni;
    }
}
