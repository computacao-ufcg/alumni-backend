package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;

import java.util.Objects;

public class PossibleMatch {

    private int score;
    private LinkedinAlumnusData linkedinProfile;

    public PossibleMatch(int score, LinkedinAlumnusData profile) {
        this.score = score;
        this.linkedinProfile = profile;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LinkedinAlumnusData getProfile() {
        return linkedinProfile;
    }

    public void setProfile(LinkedinAlumnusData profile) {
        this.linkedinProfile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PossibleMatch that = (PossibleMatch) o;
        return linkedinProfile.equals(that.linkedinProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkedinProfile);
    }

}
