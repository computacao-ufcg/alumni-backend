package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.api.http.response.LinkedinAlumnusData;
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

public class PossibleMatch {

    private Classification classification;
    private LinkedinAlumnusData linkedinProfile;

    public PossibleMatch(int score, LinkedinAlumnusData profile) {
        this.classification = new Classification(score, MatchClassification.VERY_UNLIKELY);
        this.linkedinProfile = profile;
    }

    public PossibleMatch(int score, MatchClassification matchClassification, LinkedinAlumnusData profile) {
        this.classification = new Classification(score, matchClassification);
        this.linkedinProfile = profile;
    }

    public PossibleMatch(Classification classification, LinkedinAlumnusData linkedinProfile) {
        this.classification = classification;
        this.linkedinProfile = linkedinProfile;
    }

    public MatchClassification getMatchClassification() {
        return classification.getClassification();
    }

    public void setMatchClassification(MatchClassification matchClassification) {
        this.classification.setClassification(matchClassification);
    }

    public int getScore() {
        return classification.getScore();
    }

    public void setScore(int score) {
        this.classification.setScore(score);
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
