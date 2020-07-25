package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Objects;

public class LinkedinJobData {
    private String companyUrl;
    private String companyName;
    private String jobTitle;
    private String location;
    private String description;
    private DateRange dateRange;

    public LinkedinJobData(String companyUrl, String companyName, String jobTitle, String location, String description,
                           DateRange dateRange) {
        this.companyUrl = companyUrl;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.description = description;
        this.dateRange = dateRange;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinJobData linkedinJobData = (LinkedinJobData) o;
        return getCompanyUrl().equals(linkedinJobData.getCompanyUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompanyUrl());
    }

    @Override
    public String toString() {
        return "LinkedinJobData{" +
                "companyUrl='" + companyUrl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", dateRange='" + dateRange + '\'' +
                '}';
    }
}
