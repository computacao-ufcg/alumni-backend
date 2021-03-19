package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.EmployerResponse;

import java.util.Objects;

public class LinkedinJobData {
    private EmployerResponse employer;
    private String jobTitle;
    private String location;
    private String description;
    private DateRange dateRange;

    public LinkedinJobData(String companyUrl, String companyName, String jobTitle, String location, String description,
                           DateRange dateRange) {
        this.employer = new EmployerResponse(companyName, companyUrl, EmployerType.UNDEFINED);
        this.jobTitle = jobTitle;
        this.location = location;
        this.description = description;
        this.dateRange = dateRange;
    }

    public LinkedinJobData(EmployerResponse employer, String jobTitle, String location, String description,
                           DateRange dateRange) {
        this(employer.getLinkedinId(), employer.getName(), jobTitle, location, description, dateRange);
    }

    public EmployerResponse getEmployer() {
        return employer;
    }

    public void setEmployer(EmployerResponse employer) {
        this.employer = employer;
    }

    public String getCompanyUrl() {
        return employer.getLinkedinId();
    }

    public void setCompanyUrl(String companyUrl) {
        this.employer.setLinkedinId(companyUrl);
    }

    public String getCompanyName() {
        return employer.getName();
    }

    public void setCompanyName(String companyName) {
        this.employer.setName(companyName);
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
                "companyUrl='" + employer.getLinkedinId() + '\'' +
                ", companyName='" + employer.getName() + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", dateRange='" + dateRange + '\'' +
                '}';
    }
}
