package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Objects;

public abstract class EmployerModel {

    private String linkedinId;
    private String name;
    private EmployerType type;
    private boolean isConsolidated;

    public EmployerModel(String linkedinId, String name, EmployerType type) {
        this.linkedinId = linkedinId;
        this.name = name;
        this.type = type;
        this.isConsolidated = this.checkIsConsolidated();
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public String getName() {
        return name;
    }

    public EmployerType getType() {
        return type;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(EmployerType type) {
        this.type = type;
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
        String[] splitedId = getLinkedinId().split("/");
        return splitedId[splitedId.length - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployerModel that = (EmployerModel) o;
        return Objects.equals(linkedinId, that.linkedinId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkedinId);
    }

    @Override
    public String toString() {
        return "EmployerModel{" +
                "linkedinId='" + linkedinId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isConsolidated='" + isConsolidated + '\'' +
                '}';
    }
}
