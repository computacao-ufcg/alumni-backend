package br.edu.ufcg.computacao.alumni.core.models;

public class MatchData {
    private String registration;
    private String name;
    private String linkedinId;
    private String admission;
    private String graduation;

    public MatchData(String registration, String name, String linkedinId, String admission, String graduation) {
        this.registration = registration;
        this.name = name;
        this.linkedinId = linkedinId;
        this.admission = admission;
        this.graduation = graduation;
    }

    public String getRegistration() {
        return this.registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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
