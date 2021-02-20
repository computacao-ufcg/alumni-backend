package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;

import java.time.LocalDate;

public class AlumniSiteStatisticsResponse {

    private int numberAcademyEmployedCourse;
    private int numberAlumniCourse;
    private int numberGovernmentEmployedCourse;
    private int numberMatchedAlumniCourse;
    private int numberPrivateCompanyEmployedCourse;
    private int numberPublicCompanyEmployedCourse;
    private int numberMixedCompanyEmployedCourse;
    private int historyYears;

    public AlumniSiteStatisticsResponse(StatisticsModel courseStatistics) {
        this.numberAcademyEmployedCourse = courseStatistics.getNumberAcademyEmployed();
        this.numberAlumniCourse = courseStatistics.getNumberAlumni();
        this.numberGovernmentEmployedCourse = courseStatistics.getNumberGovernmentEmployed();
        this.numberMatchedAlumniCourse = courseStatistics.getNumberMatchedAlumni();
        this.numberPrivateCompanyEmployedCourse = courseStatistics.getNumberPrivateCompanyEmployed();
        this.numberPublicCompanyEmployedCourse = courseStatistics.getNumberPublicCompanyEmployed();
        this.numberMixedCompanyEmployedCourse = courseStatistics.getNumberMixedCompanyEmployed();
        this.historyYears = LocalDate.now().getYear() - 1973;

    }

    public int getNumberAcademyEmployedCourse() {
        return numberAcademyEmployedCourse;
    }

    public void setNumberAcademyEmployedCourse(int numberAcademyEmployedCourse) {
        this.numberAcademyEmployedCourse = numberAcademyEmployedCourse;
    }

    public int getNumberAlumniCourse() {
        return numberAlumniCourse;
    }

    public void setNumberAlumniCourse(int numberAlumniCourse) {
        this.numberAlumniCourse = numberAlumniCourse;
    }

    public int getNumberGovernmentEmployedCourse() {
        return numberGovernmentEmployedCourse;
    }

    public void setNumberGovernmentEmployedCourse(int numberGovernmentEmployedCourse) {
        this.numberGovernmentEmployedCourse = numberGovernmentEmployedCourse;
    }

    public int getNumberMatchedAlumniCourse() {
        return numberMatchedAlumniCourse;
    }

    public void setNumberMatchedAlumniCourse(int numberMappedAlumniCourse) {
        this.numberMatchedAlumniCourse = numberMappedAlumniCourse;
    }

    public int getNumberPrivateCompanyEmployedCourse() {
        return numberPrivateCompanyEmployedCourse;
    }

    public void setNumberPrivateCompanyEmployedCourse(int numberPrivateCompanyEmployedCourse) {
        this.numberPrivateCompanyEmployedCourse = numberPrivateCompanyEmployedCourse;
    }

    public int getNumberPublicCompanyEmployedCourse() {
        return numberPublicCompanyEmployedCourse;
    }

    public void setNumberPublicCompanyEmployedCourse(int numberPublicCompanyEmployedCourse) {
        this.numberPublicCompanyEmployedCourse = numberPublicCompanyEmployedCourse;
    }

    public int getNumberMixedCompanyEmployedCourse() {
        return numberMixedCompanyEmployedCourse;
    }

    public void setNumberMixedCompanyEmployedCourse(int numberMixedCompanyEmployedCourse) {
        this.numberMixedCompanyEmployedCourse = numberMixedCompanyEmployedCourse;
    }

    public int getHistoryYears() {
        return historyYears;
    }

    public void setHistoryYears(int historyYears) {
        this.historyYears = historyYears;
    }
}
