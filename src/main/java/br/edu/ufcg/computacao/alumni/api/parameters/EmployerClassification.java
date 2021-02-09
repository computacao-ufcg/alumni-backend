package br.edu.ufcg.computacao.alumni.api.parameters;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class EmployerClassification {

    @ApiModelProperty(value = ApiDocumentation.Model.EMPLOYER_NAME)
    private String name;
    @ApiModelProperty(value = ApiDocumentation.Model.LINKEDIN_ID)
    private String linkedinId;
    @ApiModelProperty(value = ApiDocumentation.Model.EMPLOYER_TYPE)
    private String type;

    public EmployerClassification(String name, String linkedinId, String type) {
        this.name = name;
        this.linkedinId = linkedinId;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public String getType() {
        return type;
    }

}
