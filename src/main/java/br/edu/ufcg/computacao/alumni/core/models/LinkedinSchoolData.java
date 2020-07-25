package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Objects;

public class LinkedinSchoolData {
    private String schoolUrl;
    private String schoolName;
    private String degree;
    private String field;
    private DateRange dateRange;
    private String description;

    public LinkedinSchoolData(String schoolUrl, String schoolName, String degree, String field,
                              DateRange dateRange, String description) {
        this.schoolUrl = schoolUrl;
        this.schoolName = schoolName;
        this.degree = degree;
        this.field = field;
        this.dateRange = dateRange;
        this.description = description;
    }

    public String getSchoolUrl() {
        return schoolUrl;
    }

    public void setSchoolUrl(String schoolUrl) {
        this.schoolUrl = schoolUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedinSchoolData that = (LinkedinSchoolData) o;
        return getSchoolUrl().equals(that.getSchoolUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchoolUrl());
    }

    @Override
    public String toString() {
        return "LinkedinSchoolData{" +
                "schoolUrl='" + schoolUrl + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", degree='" + degree + '\'' +
                ", field='" + field + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
