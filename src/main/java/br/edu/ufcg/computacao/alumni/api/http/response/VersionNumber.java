package br.edu.ufcg.computacao.alumni.api.http.response;

import io.swagger.annotations.ApiModelProperty;

public class VersionNumber {
    @ApiModelProperty(example = "v.2.0.0-ms-cd46c62-common-4e0d74e")
    private String version;

    public VersionNumber() {
    }

    public VersionNumber(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
