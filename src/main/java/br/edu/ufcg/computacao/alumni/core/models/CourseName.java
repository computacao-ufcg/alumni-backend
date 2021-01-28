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
        for (CourseName courseName : CourseName.values()) {
            if (courseName.getValue().equals(value))
                return courseName;
        }

        return UNDEFINED;
    }
}
