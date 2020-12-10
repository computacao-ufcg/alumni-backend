package br.edu.ufcg.computacao.alumni.core.models;

public enum SortMethod {

    NAME("name"),
    NUMBER_OF_MATCHES("matches");

    private String strategy;

    private SortMethod(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public static SortMethod toEnum(String strategy) {
        if (strategy == null) {
            return null;
        }

        for (SortMethod sortMethod : SortMethod.values()) {
            if (sortMethod.getStrategy().equals(strategy)) {
                return sortMethod;
            }
        }
        return null;
    }
}
