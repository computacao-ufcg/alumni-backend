package br.edu.ufcg.computacao.alumni.core.models;

public enum CourseName {
    DATA_PROCESSING("data-processing"),
    COMPUTING_SCIENCE("computing-science"),
    INFORMATICS("informatics");

    private String value;

    CourseName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
