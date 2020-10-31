package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class LinkedinNameProfilePair {
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.FULL_NAME)
    private String fullName;
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.LINKEDIN_ID)
    private String profileUrl;

    public LinkedinNameProfilePair(String fullName, String profileUrl) {
        this.fullName = fullName;
        this.profileUrl = profileUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinNameProfilePair that = (LinkedinNameProfilePair) o;
        return getFullName().equals(that.getFullName()) &&
                getProfileUrl().equals(that.getProfileUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName(), getProfileUrl());
    }

    @Override
    public String toString() {
        return "LinkedinNameProfilePair{" +
                "fullName='" + fullName + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }
}
