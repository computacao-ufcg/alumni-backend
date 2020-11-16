package br.edu.ufcg.computacao.alumni.core.models;

public enum CourseName {
    DATA_PROCESSING("data-processing"),
    COMPUTING_SCIENCE("computing-science"),
    INFORMATICS("informatics"),
    UNDEFINED("undefined");

    private String value;

    CourseName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CourseName getCourseName(String value) {
        if (value.equals(DATA_PROCESSING.value)) {
            return DATA_PROCESSING;
        } else if (value.equals(COMPUTING_SCIENCE.value)) {
            return COMPUTING_SCIENCE;
        } else {
            return INFORMATICS;
        }
    }
}
