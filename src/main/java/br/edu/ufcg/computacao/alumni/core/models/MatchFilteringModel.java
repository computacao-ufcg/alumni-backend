package br.edu.ufcg.computacao.alumni.core.models;

public enum MatchFilteringModel {

    SOFT(1),
    MEDIUM(20),
    HARD(40),
    VERY_HARD(80);

    private int score;

    private MatchFilteringModel(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
