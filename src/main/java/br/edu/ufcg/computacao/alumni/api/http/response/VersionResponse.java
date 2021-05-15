package br.edu.ufcg.computacao.alumni.api.http.response;

import io.swagger.annotations.ApiModelProperty;

public class VersionResponse {
    @ApiModelProperty(example = "1.0.0-alumni-e1052fa-eureca-b2a2f14-as-3fc96f0-common-06d8761")
    private String version;

    public VersionResponse() {
    }

    public VersionResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
