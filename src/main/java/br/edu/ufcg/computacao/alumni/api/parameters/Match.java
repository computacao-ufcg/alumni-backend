package br.edu.ufcg.computacao.alumni.api.parameters;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Match {
    @ApiModelProperty(position = 0, required = true, example = ApiDocumentation.Model.REGISTRATION)
    String registration;
    @ApiModelProperty(position = 0, required = true, example = ApiDocumentation.Model.LINKEDIN_ID)
    String linkedinId;

    public String getRegistration() {
        return registration;
    }

    public String getLinkedinId() {
        return linkedinId;
    }
}
