package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.EmployerType;

public class EmployerTypeResponse {

    private String value;
    private String description;

    public EmployerTypeResponse(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public EmployerTypeResponse(EmployerType type) {
        this(type.getValue(), type.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EmployerTypeResponse{" +
                "value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
