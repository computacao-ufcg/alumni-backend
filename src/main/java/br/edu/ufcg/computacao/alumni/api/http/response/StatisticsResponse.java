package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;

public class StatisticsResponse {
    private int numberAlumniCourse;
    private int numberMappedAlumniCourse;
    private int numberAcademyEmployedCourse;
    private int numberIndustryEmployedCourse;
    private int numberGovernmentEmployedCourse;
    private int numberOngEmployedCourse;
    private int numberOthersEmployedCourse;
    private int numberAlumniLevel;
    private int numberMappedAlumniLevel;
    private int numberAcademyEmployedLevel;
    private int numberIndustryEmployedLevel;
    private int numberGovernmentEmployedLevel;
    private int numberOngEmployedLevel;
    private int numberOthersEmployedLevel;

    public StatisticsResponse(StatisticsModel statisticsCourse, StatisticsModel statisticsLevel) {
        this.numberAlumniCourse = statisticsCourse.getNumberAlumni();
        this.numberMappedAlumniCourse = statisticsCourse.getNumberMappedAlumni();
        this.numberAcademyEmployedCourse = statisticsCourse.getNumberAcademyEmployed();
        this.numberIndustryEmployedCourse = statisticsCourse.getNumberIndustryEmployed();
        this.numberGovernmentEmployedCourse = statisticsCourse.getNumberGovernmentEmployed();
        this.numberOngEmployedCourse = statisticsCourse.getNumberOngEmployed();
        this.numberOthersEmployedCourse = statisticsCourse.getNumberPublicCompanyEmployed();
        this.numberAlumniLevel = statisticsLevel.getNumberAlumni();
        this.numberMappedAlumniLevel = statisticsLevel.getNumberMappedAlumni();
        this.numberAcademyEmployedLevel = statisticsLevel.getNumberAcademyEmployed();
        this.numberIndustryEmployedLevel = statisticsLevel.getNumberIndustryEmployed();
        this.numberGovernmentEmployedLevel = statisticsLevel.getNumberGovernmentEmployed();
        this.numberOngEmployedLevel = statisticsLevel.getNumberOngEmployed();
        this.numberOthersEmployedLevel = statisticsLevel.getNumberPublicCompanyEmployed();
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

    public int getNumberIndustryEmployedCourse() {
        return numberIndustryEmployedCourse;
    }

    public void setNumberIndustryEmployedCourse(int numberIndustryEmployedCourse) {
        this.numberIndustryEmployedCourse = numberIndustryEmployedCourse;
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

    public int getNumberOthersEmployedCourse() {
        return numberOthersEmployedCourse;
    }

    public void setNumberOthersEmployedCourse(int numberOthersEmployedCourse) {
        this.numberOthersEmployedCourse = numberOthersEmployedCourse;
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

    public int getNumberOthersEmployedLevel() {
        return numberOthersEmployedLevel;
    }

    public void setNumberOthersEmployedLevel(int numberOthersEmployedLevel) {
        this.numberOthersEmployedLevel = numberOthersEmployedLevel;
    }

    @Override
    public String toString() {
        return "StatisticsResponse2{" +
                "numberAlumniCourse=" + numberAlumniCourse +
                ", numberMappedAlumniCourse=" + numberMappedAlumniCourse +
                ", numberAcademyEmployedCourse=" + numberAcademyEmployedCourse +
                ", numberIndustryEmployedCourse=" + numberIndustryEmployedCourse +
                ", numberGovernmentEmployedCourse=" + numberGovernmentEmployedCourse +
                ", numberOngEmployedCourse=" + numberOngEmployedCourse +
                ", numberOthersEmployedCourse=" + numberOthersEmployedCourse +
                ", numberAlumniLevel=" + numberAlumniLevel +
                ", numberMappedAlumniLevel=" + numberMappedAlumniLevel +
                ", numberAcademyEmployedLevel=" + numberAcademyEmployedLevel +
                ", numberIndustryEmployedLevel=" + numberIndustryEmployedLevel +
                ", numberGovernmentEmployedLevel=" + numberGovernmentEmployedLevel +
                ", numberOngEmployedLevel=" + numberOngEmployedLevel +
                ", numberOthersEmployedLevel=" + numberOthersEmployedLevel +
                '}';
    }
}
