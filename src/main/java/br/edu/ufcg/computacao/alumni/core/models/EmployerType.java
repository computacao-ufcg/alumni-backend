package br.edu.ufcg.computacao.alumni.core.models;

import br.edu.ufcg.computacao.alumni.constants.EmployerTypeDescription;

public enum EmployerType {
    ACADEMY("academy", EmployerTypeDescription.ACADEMY),
    GOVERNMENT("government", EmployerTypeDescription.GOVERNMENT),
    ONG("ong", EmployerTypeDescription.ONG),
    PUBLIC_COMPANY("public-company", EmployerTypeDescription.PUBLIC_COMPANY),
    PRIVATE_COMPANY("private-company", EmployerTypeDescription.PRIVATE_COMPANY),
    MIXED_COMPANY("mixed-company", EmployerTypeDescription.MIXED_ECONOMY_COMPANY),
    UNDEFINED("undefined", EmployerTypeDescription.UNDEFINED);

    private String value;
    private String description;

    EmployerType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static EmployerType getType(String value) {
        for (EmployerType employerType : EmployerType.values()) {
            if (employerType.getValue().equals(value))
                return employerType;
        }
        return UNDEFINED;
    }
}
