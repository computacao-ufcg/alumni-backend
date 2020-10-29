package br.edu.ufcg.computacao.alumni.api.http.response;

public class Match {
    private String registration;
    private String linkedinId;

    public Match(String registration, String linkedinId){
        this.registration = registration;
        this.linkedinId = linkedinId;

    }

    public String getRegistration(){
        return this.registration;
    }

    public String getLinkedinId(){
        return this.linkedinId;
    }
}
