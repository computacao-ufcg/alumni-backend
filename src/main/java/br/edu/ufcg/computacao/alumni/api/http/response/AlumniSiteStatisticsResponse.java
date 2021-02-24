package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.holders.AlumniHolder;
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
    private int numberAlumniGraduatedInTheLastYear;

    private int numberAlumniLevel;
    private int numberMatchedAlumniLevel;
    private int numberAcademyEmployedLevel;
    private int numberGovernmentEmployedLevel;
    private int numberOngEmployedLevel;
    private int numberPublicCompanyEmployedLevel;
    private int numberPrivateCompanyEmployedLevel;
    private int numberMixedCompanyEmployedLevel;

    public AlumniSiteStatisticsResponse(StatisticsModel courseStatistics, StatisticsModel levelStatistics) {
        this.numberAcademyEmployedCourse = courseStatistics.getNumberAcademyEmployed();
        this.numberAlumniCourse = courseStatistics.getNumberAlumni();
        this.numberGovernmentEmployedCourse = courseStatistics.getNumberGovernmentEmployed();
        this.numberMatchedAlumniCourse = courseStatistics.getNumberMatchedAlumni();
        this.numberPrivateCompanyEmployedCourse = courseStatistics.getNumberPrivateCompanyEmployed();
        this.numberPublicCompanyEmployedCourse = courseStatistics.getNumberPublicCompanyEmployed();
        this.numberMixedCompanyEmployedCourse = courseStatistics.getNumberMixedCompanyEmployed();

        this.historyYears = LocalDate.now().getYear() - 1973;
        this.numberAlumniGraduatedInTheLastYear = AlumniHolder.getInstance().getNumberAlumniGraduatedInTheLastYear();

        this.numberAlumniLevel = levelStatistics.getNumberAlumni();
        this.numberMatchedAlumniLevel = levelStatistics.getNumberMatchedAlumni();
        this.numberAcademyEmployedLevel = levelStatistics.getNumberAcademyEmployed();
        this.numberGovernmentEmployedLevel = levelStatistics.getNumberGovernmentEmployed();
        this.numberOngEmployedLevel = levelStatistics.getNumberOngEmployed();
        this.numberPublicCompanyEmployedLevel = levelStatistics.getNumberPublicCompanyEmployed();
        this.numberPrivateCompanyEmployedLevel = levelStatistics.getNumberPrivateCompanyEmployed();
        this.numberMixedCompanyEmployedLevel = levelStatistics.getNumberMixedCompanyEmployed();

    }

    public int getNumberAlumniLevel() {
        return numberAlumniLevel;
    }

    public void setNumberAlumniLevel(int numberAlumniLevel) {
        this.numberAlumniLevel = numberAlumniLevel;
    }

    public int getNumberMatchedAlumniLevel() {
        return numberMatchedAlumniLevel;
    }

    public void setNumberMatchedAlumniLevel(int numberMatchedAlumniLevel) {
        this.numberMatchedAlumniLevel = numberMatchedAlumniLevel;
    }

    public int getNumberAcademyEmployedLevel() {
        return numberAcademyEmployedLevel;
    }

    public void setNumberAcademyEmployedLevel(int numberAcademyEmployedLevel) {
        this.numberAcademyEmployedLevel = numberAcademyEmployedLevel;
    }

    public int getNumberGovernmentEmployedLevel() {
        return numberGovernmentEmployedLevel;
    }

    public void setNumberGovernmentEmployedLevel(int numberGovernmentEmployedLevel) {
        this.numberGovernmentEmployedLevel = numberGovernmentEmployedLevel;
    }

    public int getNumberOngEmployedLevel() {
        return numberOngEmployedLevel;
    }

    public void setNumberOngEmployedLevel(int numberOngEmployedLevel) {
        this.numberOngEmployedLevel = numberOngEmployedLevel;
    }

    public int getNumberPublicCompanyEmployedLevel() {
        return numberPublicCompanyEmployedLevel;
    }

    public void setNumberPublicCompanyEmployedLevel(int numberPublicCompanyEmployedLevel) {
        this.numberPublicCompanyEmployedLevel = numberPublicCompanyEmployedLevel;
    }

    public int getNumberPrivateCompanyEmployedLevel() {
        return numberPrivateCompanyEmployedLevel;
    }

    public void setNumberPrivateCompanyEmployedLevel(int numberPrivateCompanyEmployedLevel) {
        this.numberPrivateCompanyEmployedLevel = numberPrivateCompanyEmployedLevel;
    }

    public int getNumberMixedCompanyEmployedLevel() {
        return numberMixedCompanyEmployedLevel;
    }

    public void setNumberMixedCompanyEmployedLevel(int numberMixedCompanyEmployedLevel) {
        this.numberMixedCompanyEmployedLevel = numberMixedCompanyEmployedLevel;
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

    public int getNumberAlumniGraduatedInTheLastYear() {
        return numberAlumniGraduatedInTheLastYear;
    }

    public void setNumberAlumniGraduatedInTheLastYear(int numberAlumniGraduatedInTheLastYear) {
        this.numberAlumniGraduatedInTheLastYear = numberAlumniGraduatedInTheLastYear;
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
