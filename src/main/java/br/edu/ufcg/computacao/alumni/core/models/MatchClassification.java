package br.edu.ufcg.computacao.alumni.core.models;

public enum MatchClassification {
    VERY_UNLIKELY("very-unlikely", 0),
    UNLIKELY("unlikely", 1),
    AVERAGE("average", 2),
    LIKELY("likely", 3),
    VERY_LIKELY("very-likely", 4);

    private String value;
    private int priority;

    MatchClassification(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

    public static MatchClassification getClassification(String value) {
        for (MatchClassification matchClassification : MatchClassification.values()) {
            if (matchClassification.getValue().equals(value))
                return matchClassification;
        }
        return null;
    }
}
