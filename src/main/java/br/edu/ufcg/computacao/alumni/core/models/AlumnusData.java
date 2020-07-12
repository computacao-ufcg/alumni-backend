package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Arrays;
import java.util.Objects;

public class AlumnusData {
    private String fullName;
    private String company;
    private String description;
    private String location;
    private JobData jobs[];
    private EducationData education[];
    private String email;
    private String linkedinProfile;
    private String twitterId;

    public AlumnusData(String fullName, String company, String description, String location, JobData[] jobs,
                       EducationData[] education, String email, String linkedinProfile, String twitterId) {
        this.fullName = fullName;
        this.company = company;
        this.description = description;
        this.location = location;
        this.jobs = jobs;
        this.education = education;
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

    public JobData[] getJobs() {
        return jobs;
    }

    public void setJobs(JobData[] jobs) {
        this.jobs = jobs;
    }

    public EducationData[] getEducation() {
        return education;
    }

    public void setEducation(EducationData[] education) {
        this.education = education;
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
        AlumnusData that = (AlumnusData) o;
        return getLinkedinProfile().equals(that.getLinkedinProfile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLinkedinProfile());
    }

    @Override
    public String toString() {
        return "AlumnusData{" +
                "fullName='" + fullName + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", jobs=" + Arrays.toString(jobs) +
                ", education=" + Arrays.toString(education) +
                ", email='" + email + '\'' +
                ", linkedInProfile='" + linkedinProfile + '\'' +
                ", twitterId='" + twitterId + '\'' +
                '}';
    }
}
