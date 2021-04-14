package br.edu.ufcg.computacao.alumni.api.parameters;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class EmployerIdData {

    @ApiModelProperty(value = ApiDocumentation.Model.CURRENT_LINKEDIN_ID)
    private String currentId;

    @ApiModelProperty(value = ApiDocumentation.Model.CONSOLIDATED_LINKEDIN_ID)
    private String newId;

    public String getCurrentId() {
        return currentId;
    }

    public String getNewId() {
        return newId;
    }
}
