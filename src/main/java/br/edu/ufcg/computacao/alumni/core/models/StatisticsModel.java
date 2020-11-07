package br.edu.ufcg.computacao.alumni.core.models;

public class StatisticsModel {
    private int numberAlumni;
    private int numberMappedAlumni;
    private int numberAcademyEmployed;
    private int numberIndustryEmployed;
    private int numberGovernmentEmployed;
    private int numberOngEmployed;
    private int numberOthersEmployed;

    public StatisticsModel(int numberAlumni, int numberMappedAlumni, int numberAcademyEmployed, int numberIndustryEmployed,
                           int numberGovernmentEmployed, int numberOngEmployed, int numberOthersEmployed) {
        this.numberAlumni = numberAlumni;
        this.numberMappedAlumni = numberMappedAlumni;
        this.numberAcademyEmployed = numberAcademyEmployed;
        this.numberIndustryEmployed = numberIndustryEmployed;
        this.numberGovernmentEmployed = numberGovernmentEmployed;
        this.numberOngEmployed = numberOngEmployed;
        this.numberOthersEmployed = numberOthersEmployed;
    }

    public int getNumberAlumni() {
        return numberAlumni;
    }

    public int getNumberMappedAlumni() {
        return numberMappedAlumni;
    }

    public int getNumberAcademyEmployed() {
        return numberAcademyEmployed;
    }

    public int getNumberIndustryEmployed() {
        return numberIndustryEmployed;
    }

    public int getNumberGovernmentEmployed() {
        return numberGovernmentEmployed;
    }

    public int getNumberOngEmployed() {
        return numberOngEmployed;
    }

    public int getNumberOthersEmployed() {
        return numberOthersEmployed;
    }

    public void setNumberAlumni(int numberAlumni) {
        this.numberAlumni = numberAlumni;
    }

    public void setNumberMappedAlumni(int numberMappedAlumni) {
        this.numberMappedAlumni = numberMappedAlumni;
    }

    public void setNumberAcademyEmployed(int numberAcademyEmployed) {
        this.numberAcademyEmployed = numberAcademyEmployed;
    }

    public void setNumberIndustryEmployed(int numberIndustryEmployed) {
        this.numberIndustryEmployed = numberIndustryEmployed;
    }

    public void setNumberGovernmentEmployed(int numberGovernmentEmployed) {
        this.numberGovernmentEmployed = numberGovernmentEmployed;
    }

    public void setNumberOngEmployed(int numberOngEmployed) {
        this.numberOngEmployed = numberOngEmployed;
    }

    public void setNumberOthersEmployed(int numberOthersEmployed) {
        this.numberOthersEmployed = numberOthersEmployed;
    }

    @Override
    public String toString() {
        return "StatisticsResponse{" +
                "numberAlumni=" + numberAlumni +
                ", numberMappedAlumni=" + numberMappedAlumni +
                ", numberAcademyEmployed=" + numberAcademyEmployed +
                ", numberIndustryEmployed=" + numberIndustryEmployed +
                ", numberGovernmentEmployed=" + numberGovernmentEmployed +
                ", numberOngEmployed=" + numberOngEmployed +
                ", numberOthersEmployed=" + numberOthersEmployed +
                '}';
    }
}
