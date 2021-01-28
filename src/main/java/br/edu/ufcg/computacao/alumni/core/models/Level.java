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
        for (Level level : Level.values()) {
            if (level.getValue().equals(value))
                return level;
        }
        return UNDEFINED;
    }
}
