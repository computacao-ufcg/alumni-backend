package br.edu.ufcg.computacao.alumni.core.models;

public class Classification {

    private int score;
    private MatchClassification classification;

    public Classification(int score, MatchClassification classification) {
        this.score = score;
        this.classification = classification;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public MatchClassification getClassification() {
        return classification;
    }

    public void setClassification(MatchClassification classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Classification{" +
                "score=" + score +
                ", classification=" + classification +
                '}';
    }
}
