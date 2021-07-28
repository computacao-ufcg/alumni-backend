package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.StatisticsModel;

public class StatisticsResponse {
    private int numberAlumni;
    private int numberMatchedAlumni;
    private int numberAcademyEmployed;
    private int numberGovernmentEmployed;
    private int numberOngEmployed;
    private int numberPublicCompanyEmployed;
    private int numberPrivateCompanyEmployed;
    private int numberMixedCompanyEmployed;
    private int numberConsolidatedEmployers;
    private int numberTotalEmployers;

    public StatisticsResponse(StatisticsModel statistics) {
        this.numberAlumni = statistics.getNumberAlumni();
        this.numberMatchedAlumni = statistics.getNumberMatchedAlumni();
        this.numberAcademyEmployed = statistics.getNumberAcademyEmployed();
        this.numberGovernmentEmployed = statistics.getNumberGovernmentEmployed();
        this.numberOngEmployed = statistics.getNumberOngEmployed();
        this.numberPublicCompanyEmployed = statistics.getNumberPublicCompanyEmployed();
        this.numberPrivateCompanyEmployed = statistics.getNumberPrivateCompanyEmployed();
        this.numberMixedCompanyEmployed = statistics.getNumberMixedCompanyEmployed();
        this.numberConsolidatedEmployers = statistics.getNumberConsolidatedEmployers();
        this.numberTotalEmployers = statistics.getNumberTotalEmployers();
    }

    public int getNumberAlumni() {
        return numberAlumni;
    }

    public void setNumberAlumni(int numberAlumni) {
        this.numberAlumni = numberAlumni;
    }

    public int getNumberMatchedAlumni() {
        return numberMatchedAlumni;
    }

    public void setNumberMatchedAlumni(int numberMatchedAlumni) {
        this.numberMatchedAlumni = numberMatchedAlumni;
    }

    public int getNumberAcademyEmployed() {
        return numberAcademyEmployed;
    }

    public void setNumberAcademyEmployed(int numberAcademyEmployed) {
        this.numberAcademyEmployed = numberAcademyEmployed;
    }

    public int getNumberGovernmentEmployed() {
        return numberGovernmentEmployed;
    }

    public void setNumberGovernmentEmployed(int numberGovernmentEmployed) {
        this.numberGovernmentEmployed = numberGovernmentEmployed;
    }

    public int getNumberOngEmployed() {
        return numberOngEmployed;
    }

    public void setNumberOngEmployed(int numberOngEmployed) {
        this.numberOngEmployed = numberOngEmployed;
    }

    public int getNumberPublicCompanyEmployed() {
        return numberPublicCompanyEmployed;
    }

    public void setNumberPublicCompanyEmployed(int numberPublicCompanyEmployed) {
        this.numberPublicCompanyEmployed = numberPublicCompanyEmployed;
    }

    public int getNumberPrivateCompanyEmployed() {
        return numberPrivateCompanyEmployed;
    }

    public void setNumberPrivateCompanyEmployed(int numberPrivateCompanyEmployed) {
        this.numberPrivateCompanyEmployed = numberPrivateCompanyEmployed;
    }

    public int getNumberMixedCompanyEmployed() {
        return numberMixedCompanyEmployed;
    }

    public void setNumberMixedCompanyEmployed(int numberMixedCompanyEmployed) {
        this.numberMixedCompanyEmployed = numberMixedCompanyEmployed;
    }

    public int getNumberTotalEmployers() {
        return numberTotalEmployers;
    }

    public int getNumberConsolidatedEmployers() {
        return numberConsolidatedEmployers;
    }

    @Override
    public String toString() {
        return "StatisticsResponse{" +
                "numberAlumni=" + numberAlumni +
                ", numberMatchedAlumni=" + numberMatchedAlumni +
                ", numberAcademyEmployed=" + numberAcademyEmployed +
                ", numberGovernmentEmployed=" + numberGovernmentEmployed +
                ", numberOngEmployed=" + numberOngEmployed +
                ", numberPublicCompanyEmployed=" + numberPublicCompanyEmployed +
                ", numberPrivateCompanyEmployed=" + numberPrivateCompanyEmployed +
                ", numberMixedCompanyEmployed=" + numberMixedCompanyEmployed +
                ", numberConsolidatedEmployers=" + numberConsolidatedEmployers +
                ", numberTotalEmployers=" + numberTotalEmployers +
                '}';
    }
}
