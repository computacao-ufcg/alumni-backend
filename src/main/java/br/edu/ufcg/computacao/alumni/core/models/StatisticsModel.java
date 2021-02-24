package br.edu.ufcg.computacao.alumni.core.models;

public class StatisticsModel {
    private int numberAlumni;
    private int numberMatchedAlumni;
    private int numberAcademyEmployed;
    private int numberGovernmentEmployed;
    private int numberOngEmployed;
    private int numberPublicCompanyEmployed;
    private int numberPrivateCompanyEmployed;
    private int numberMixedCompanyEmployed;

    public StatisticsModel(int numberAlumni, int numberMatchedAlumni, int numberAcademyEmployed,
                           int numberGovernmentEmployed, int numberOngEmployed, int numberPublicCompanyEmployed,
                           int numberPrivateCompanyEmployed, int numberMixedCompanyEmployed) {
        this.numberAlumni = numberAlumni;
        this.numberMatchedAlumni = numberMatchedAlumni;
        this.numberAcademyEmployed = numberAcademyEmployed;
        this.numberGovernmentEmployed = numberGovernmentEmployed;
        this.numberOngEmployed = numberOngEmployed;
        this.numberPublicCompanyEmployed = numberPublicCompanyEmployed;
        this.numberPrivateCompanyEmployed = numberPrivateCompanyEmployed;
        this.numberMixedCompanyEmployed = numberMixedCompanyEmployed;
    }

    public int getNumberAlumni() {
        return numberAlumni;
    }

    public int getNumberMatchedAlumni() {
        return numberMatchedAlumni;
    }

    public int getNumberAcademyEmployed() {
        return numberAcademyEmployed;
    }

    public int getNumberGovernmentEmployed() {
        return numberGovernmentEmployed;
    }

    public int getNumberOngEmployed() {
        return numberOngEmployed;
    }

    public int getNumberPublicCompanyEmployed() {
        return numberPublicCompanyEmployed;
    }

    public int getNumberPrivateCompanyEmployed() {
        return numberPrivateCompanyEmployed;
    }

    public int getNumberMixedCompanyEmployed() {
        return numberMixedCompanyEmployed;
    }

    public void setNumberAlumni(int numberAlumni) {
        this.numberAlumni = numberAlumni;
    }

    public void setNumberMatchedAlumni(int numberMatchedAlumni) {
        this.numberMatchedAlumni = numberMatchedAlumni;
    }

    public void setNumberAcademyEmployed(int numberAcademyEmployed) {
        this.numberAcademyEmployed = numberAcademyEmployed;
    }

    public void setNumberGovernmentEmployed(int numberGovernmentEmployed) {
        this.numberGovernmentEmployed = numberGovernmentEmployed;
    }

    public void setNumberOngEmployed(int numberOngEmployed) {
        this.numberOngEmployed = numberOngEmployed;
    }

    public void setNumberPublicCompanyEmployed(int numberPublicCompanyEmployed) {
        this.numberPublicCompanyEmployed = numberPublicCompanyEmployed;
    }

    public void setNumberPrivateCompanyEmployed(int numberPrivateCompanyEmployed) {
        this.numberPrivateCompanyEmployed = numberPrivateCompanyEmployed;
    }

    public void setNumberMixedCompanyEmployed(int numberMixedCompanyEmployed) {
        this.numberMixedCompanyEmployed = numberMixedCompanyEmployed;
    }

    @Override
    public String toString() {
        return "StatisticsModel{" +
                "numberAlumni=" + numberAlumni +
                ", numberMatchedAlumni=" + numberMatchedAlumni +
                ", numberAcademyEmployed=" + numberAcademyEmployed +
                ", numberGovernmentEmployed=" + numberGovernmentEmployed +
                ", numberOngEmployed=" + numberOngEmployed +
                ", numberPublicCompanyEmployed=" + numberPublicCompanyEmployed +
                ", numberPrivateCompanyEmployed=" + numberPrivateCompanyEmployed +
                ", numberMixedCompanyEmployed=" + numberMixedCompanyEmployed +
                '}';
    }
}
