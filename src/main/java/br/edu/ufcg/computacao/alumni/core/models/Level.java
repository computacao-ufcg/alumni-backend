package br.edu.ufcg.computacao.alumni.core.models;

public enum Level {
    UNDERGRADUATE("undergraduate"),
    MASTER("master"),
    DOCTORATE("doctorate"),
    UNDEFINED("undefined");

    private String value;

    Level(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Level getLevel(String value) {
        if (value.equals(UNDERGRADUATE.value)) {
            return UNDERGRADUATE;
        } else if (value.equals(MASTER.value)) {
            return MASTER;
        } else if(value.equals(DOCTORATE.value)){
            return DOCTORATE;
        } else {
            return UNDEFINED;
        }
    }
}
