package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;

public class StatisticsResponse {
    private int numberAlumniCourse;
    private int numberMappedAlumniCourse;
    private int numberAcademyEmployedCourse;
    private int numberGovernmentEmployedCourse;
    private int numberOngEmployedCourse;
    private int numberPublicCompanyEmployedCourse;
    private int numberPrivateCompanyEmployedCourse;
    private int numberMixedCompanyEmployedCourse;

    private int numberAlumniLevel;
    private int numberMappedAlumniLevel;
    private int numberAcademyEmployedLevel;
    private int numberIndustryEmployedLevel;
    private int numberGovernmentEmployedLevel;
    private int numberOngEmployedLevel;
    private int numberPublicCompanyEmployedLevel;
    private int numberPrivateCompanyEmployedLevel;
    private int numberMixedCompanyEmployedLevel;

    public StatisticsResponse(StatisticsModel statisticsCourse, StatisticsModel statisticsLevel) {
        this.numberAlumniCourse = statisticsCourse.getNumberAlumni();
        this.numberMappedAlumniCourse = statisticsCourse.getNumberMappedAlumni();
        this.numberAcademyEmployedCourse = statisticsCourse.getNumberAcademyEmployed();
        this.numberGovernmentEmployedCourse = statisticsCourse.getNumberGovernmentEmployed();
        this.numberOngEmployedCourse = statisticsCourse.getNumberOngEmployed();
        this.numberPublicCompanyEmployedCourse = statisticsCourse.getNumberPublicCompanyEmployed();
        this.numberPrivateCompanyEmployedCourse = statisticsCourse.getNumberPrivateCompanyEmployed();
        this.numberMixedCompanyEmployedCourse = statisticsCourse.getNumberMixedCompanyEmployed();

        this.numberAlumniLevel = statisticsLevel.getNumberAlumni();
        this.numberMappedAlumniLevel = statisticsLevel.getNumberMappedAlumni();
        this.numberAcademyEmployedLevel = statisticsLevel.getNumberAcademyEmployed();
        this.numberGovernmentEmployedLevel = statisticsLevel.getNumberGovernmentEmployed();
        this.numberOngEmployedLevel = statisticsLevel.getNumberOngEmployed();
        this.numberPublicCompanyEmployedLevel = statisticsLevel.getNumberPublicCompanyEmployed();
        this.numberPrivateCompanyEmployedLevel = statisticsLevel.getNumberPrivateCompanyEmployed();
        this.numberMixedCompanyEmployedLevel = statisticsLevel.getNumberMixedCompanyEmployed();
    }

    public int getNumberAlumniCourse() {
        return numberAlumniCourse;
    }

    public void setNumberAlumniCourse(int numberAlumniCourse) {
        this.numberAlumniCourse = numberAlumniCourse;
    }

    public int getNumberMappedAlumniCourse() {
        return numberMappedAlumniCourse;
    }

    public void setNumberMappedAlumniCourse(int numberMappedAlumniCourse) {
        this.numberMappedAlumniCourse = numberMappedAlumniCourse;
    }

    public int getNumberAcademyEmployedCourse() {
        return numberAcademyEmployedCourse;
    }

    public void setNumberAcademyEmployedCourse(int numberAcademyEmployedCourse) {
        this.numberAcademyEmployedCourse = numberAcademyEmployedCourse;
    }

    public int getNumberGovernmentEmployedCourse() {
        return numberGovernmentEmployedCourse;
    }

    public void setNumberGovernmentEmployedCourse(int numberGovernmentEmployedCourse) {
        this.numberGovernmentEmployedCourse = numberGovernmentEmployedCourse;
    }

    public int getNumberOngEmployedCourse() {
        return numberOngEmployedCourse;
    }

    public void setNumberOngEmployedCourse(int numberOngEmployedCourse) {
        this.numberOngEmployedCourse = numberOngEmployedCourse;
    }

    public int getNumberAlumniLevel() {
        return numberAlumniLevel;
    }

    public void setNumberAlumniLevel(int numberAlumniLevel) {
        this.numberAlumniLevel = numberAlumniLevel;
    }

    public int getNumberMappedAlumniLevel() {
        return numberMappedAlumniLevel;
    }

    public void setNumberMappedAlumniLevel(int numberMappedAlumniLevel) {
        this.numberMappedAlumniLevel = numberMappedAlumniLevel;
    }

    public int getNumberAcademyEmployedLevel() {
        return numberAcademyEmployedLevel;
    }

    public void setNumberAcademyEmployedLevel(int numberAcademyEmployedLevel) {
        this.numberAcademyEmployedLevel = numberAcademyEmployedLevel;
    }

    public int getNumberIndustryEmployedLevel() {
        return numberIndustryEmployedLevel;
    }

    public void setNumberIndustryEmployedLevel(int numberIndustryEmployedLevel) {
        this.numberIndustryEmployedLevel = numberIndustryEmployedLevel;
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

    public int getNumberPublicCompanyEmployedCourse() {
        return numberPublicCompanyEmployedCourse;
    }

    public void setNumberPublicCompanyEmployedCourse(int numberPublicCompanyEmployedCourse) {
        this.numberPublicCompanyEmployedCourse = numberPublicCompanyEmployedCourse;
    }

    public int getNumberPrivateCompanyEmployedCourse() {
        return numberPrivateCompanyEmployedCourse;
    }

    public void setNumberPrivateCompanyEmployedCourse(int numberPrivateCompanyEmployedCourse) {
        this.numberPrivateCompanyEmployedCourse = numberPrivateCompanyEmployedCourse;
    }

    public int getNumberMixedCompanyEmployedCourse() {
        return numberMixedCompanyEmployedCourse;
    }

    public void setNumberMixedCompanyEmployedCourse(int numberMixedCompanyEmployedCourse) {
        this.numberMixedCompanyEmployedCourse = numberMixedCompanyEmployedCourse;
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

    @Override
    public String toString() {
        return "StatisticsResponse{" +
                "numberAlumniCourse=" + numberAlumniCourse +
                ", numberMappedAlumniCourse=" + numberMappedAlumniCourse +
                ", numberAcademyEmployedCourse=" + numberAcademyEmployedCourse +
                ", numberGovernmentEmployedCourse=" + numberGovernmentEmployedCourse +
                ", numberOngEmployedCourse=" + numberOngEmployedCourse +
                ", numberPublicCompanyEmployedCourse=" + numberPublicCompanyEmployedCourse +
                ", numberPrivateCompanyEmployedCourse=" + numberPrivateCompanyEmployedCourse +
                ", numberMixedCompanyEmployedCourse=" + numberMixedCompanyEmployedCourse +
                ", numberAlumniLevel=" + numberAlumniLevel +
                ", numberMappedAlumniLevel=" + numberMappedAlumniLevel +
                ", numberAcademyEmployedLevel=" + numberAcademyEmployedLevel +
                ", numberIndustryEmployedLevel=" + numberIndustryEmployedLevel +
                ", numberGovernmentEmployedLevel=" + numberGovernmentEmployedLevel +
                ", numberOngEmployedLevel=" + numberOngEmployedLevel +
                ", numberPublicCompanyEmployedLevel=" + numberPublicCompanyEmployedLevel +
                ", numberPrivateCompanyEmployedLevel=" + numberPrivateCompanyEmployedLevel +
                ", numberMixedCompanyEmployedLevel=" + numberMixedCompanyEmployedLevel +
                '}';
    }
}
