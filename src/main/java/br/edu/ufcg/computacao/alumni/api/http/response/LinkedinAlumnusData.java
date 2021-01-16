package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinJobData;
import br.edu.ufcg.computacao.alumni.core.models.LinkedinSchoolData;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

public class LinkedinAlumnusData {
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.FULL_NAME)
    private String fullName;
    private String company;
    private String description;
    private String location;
    private LinkedinJobData jobs[];
    private LinkedinSchoolData schools[];
    private String email;
    private String linkedinProfile;
    private String twitterId;

    public LinkedinAlumnusData(String fullName, String company, String description, String location, LinkedinJobData[] jobs,
                               LinkedinSchoolData[] schools, String email, String linkedinProfile, String twitterId) {
        this.fullName = fullName;
        this.company = company;
        this.description = description;
        this.location = location;
        this.jobs = jobs;
        this.schools = schools;
        this.email = email;
        this.linkedinProfile = linkedinProfile;
        this.twitterId = twitterId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LinkedinJobData[] getJobs() {
        return jobs;
    }

    public void setJobs(LinkedinJobData[] jobs) {
        this.jobs = jobs;
    }

    public LinkedinSchoolData[] getSchools() {
        return this.schools;
    }

    public void setSchools(LinkedinSchoolData[] schools) {
        this.schools = schools;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public String getLinkedinId() {
        String[] splitedUrl = linkedinProfile.split("/");
        return splitedUrl[splitedUrl.length - 1];
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinAlumnusData that = (LinkedinAlumnusData) o;
        return getLinkedinProfile().equals(that.getLinkedinProfile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLinkedinProfile());
    }

    @Override
    public String toString() {
        return "LinkedinAlumnusData{" +
                "fullName='" + fullName + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", jobs=" + Arrays.toString(jobs) +
                ", schools=" + Arrays.toString(schools) +
                ", email='" + email + '\'' +
                ", linkedInProfile='" + linkedinProfile + '\'' +
                ", twitterId='" + twitterId + '\'' +
                '}';
    }
}
