package br.edu.ufcg.computacao.alumni.core.models;

public class MatchData {
    private String name;
    private String linkedinId;
    private String admission;
    private String graduation;

    public MatchData(String name, String linkedinId, String admission, String graduation) {
        this.name = name;
        this.linkedinId = linkedinId;
        this.admission = admission;
        this.graduation = graduation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }
}
