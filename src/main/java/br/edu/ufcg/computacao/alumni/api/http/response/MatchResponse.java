package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class MatchResponse {
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.REGISTRATION)
    private String registration;
    @ApiModelProperty(position = 1, example = ApiDocumentation.Model.FULL_NAME)
    private String fullName;
    @ApiModelProperty(position = 2, example = ApiDocumentation.Model.LINKEDIN_ID)
    private String linkedinId;

    public MatchResponse(String registration, String fullName, String linkedinId) {
        this.registration = registration;
        this.fullName = fullName;
        this.linkedinId = linkedinId;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }
}
