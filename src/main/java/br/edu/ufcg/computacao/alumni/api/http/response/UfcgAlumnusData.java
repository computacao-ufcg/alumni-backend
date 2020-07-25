package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.Grau;

import java.util.Arrays;
import java.util.Objects;

public class UfcgAlumnusData {
    private String linkedinId;
    private String fullName;
    private Grau[] graus;

    public UfcgAlumnusData(String linkedinId, String fullName, Grau[] graus) {
        this.fullName = fullName;
        this.graus = graus;
        this.linkedinId = linkedinId;
    }

    public UfcgAlumnusData(String fullName, Grau[] graus) {
        this.fullName = fullName;
        this.graus = graus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Grau[] getGraus() {
        return graus;
    }

    public void setGraus(Grau[] graus) {
        this.graus = graus;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UfcgAlumnusData that = (UfcgAlumnusData) o;
        return getLinkedinId().equals(that.getLinkedinId()) &&
                getFullName().equals(that.getFullName()) &&
                Arrays.equals(getGraus(), that.getGraus());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getLinkedinId(), getFullName());
        result = 31 * result + Arrays.hashCode(getGraus());
        return result;
    }

    @Override
    public String toString() {
        return "UfcgAlumnusData{" +
                "linkedinId='" + linkedinId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", graus=" + Arrays.toString(graus) +
                '}';
    }
}
