package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.EmployerType;

import java.util.Objects;

public class EmployerResponse {
    private String name;
    private String linkedinId;
    private EmployerType type;
    private boolean isConsolidated;

    public EmployerResponse(String name, String linkedinId, EmployerType type) {
        this.name = name;
        this.linkedinId = linkedinId;
        this.type = type;
        this.isConsolidated = checkIsConsolidated();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkedinId() {
        return this.linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    public boolean isConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    private boolean checkIsConsolidated() {
        return !getId().contains("?keywords");
    }

    private String getId() {
        String[] splitedId = this.linkedinId.split("/");
        return splitedId[splitedId.length - 1];
    }

    public EmployerType getType() {
        return this.type;
    }

    public void setType(EmployerType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployerResponse that = (EmployerResponse) o;
        return name.equals(that.name) &&
                linkedinId.equals(that.linkedinId) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, linkedinId, type);
    }

    @Override
    public String toString() {
        return "EmployerResponse{" +
                "name='" + name + '\'' +
                ", linkedinId='" + linkedinId + '\'' +
                ", type=" + type +
                '}';
    }
}
