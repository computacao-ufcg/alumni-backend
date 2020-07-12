package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class CurrentJob {
    @ApiModelProperty(position = 1, example = ApiDocumentation.Model.ALUMNUS_NAME)
    private String alumnusName;
    @ApiModelProperty(position = 2, example = ApiDocumentation.Model.CURRENT_JOB)
    private String currentJob;
    @ApiModelProperty(position = 2, example = ApiDocumentation.Model.START_YEAR)
    private String startYear;

    public CurrentJob(String alumnusName, String currentJob, String startYear) {
        this.alumnusName = alumnusName;
        this.currentJob = currentJob;
        this.startYear = startYear;
    }

    public String getAlumnusName() {
        return alumnusName;
    }

    public void setAlumnusName(String alumnusName) {
        this.alumnusName = alumnusName;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }
}
