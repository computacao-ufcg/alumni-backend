package br.edu.ufcg.computacao.alumni.api.parameters;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MatchParameter {
    @ApiModelProperty(position = 0, required = true, example = ApiDocumentation.Model.REGISTRATION)
    String registration;
    @ApiModelProperty(position = 1, required = true, example = ApiDocumentation.Model.LINKEDIN_ID)
    String linkedinId;

    public MatchParameter(String registration, String linkedinId) {
        this.registration = registration;
        this.linkedinId = linkedinId;
    }

    public String getRegistration() {
        return registration;
    }

    public String getLinkedinId() {
        return linkedinId;
    }
}
