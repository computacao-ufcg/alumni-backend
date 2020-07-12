package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.core.models.AlumnusData;
import io.swagger.annotations.ApiModelProperty;

public class Alumni {
    @ApiModelProperty(example = ApiDocumentation.Model.ALUMNI)
    private AlumnusData alumni[];

    public Alumni(AlumnusData alumni[]) {
        this.alumni = alumni;
    }

    public AlumnusData[] getAlumni() {
        return alumni;
    }

    public void setAlumni(AlumnusData alumni[]) {
        this.alumni = alumni;
    }
}
