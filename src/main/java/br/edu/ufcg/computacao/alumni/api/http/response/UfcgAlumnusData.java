package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.core.models.Degree;

import java.util.Arrays;
import java.util.Objects;

public class UfcgAlumnusData {
    private String registration;
    private String fullName;
    private Degree[] graus;

    public UfcgAlumnusData(String registration, String fullName, Degree[] graus) {
        this.fullName = fullName;
        this.graus = graus;
        this.registration = registration;
    }

    public UfcgAlumnusData(String fullName, Degree[] graus) {
        this.fullName = fullName;
        this.graus = graus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Degree[] getGraus() {
        return graus;
    }

    public void setGraus(Degree[] graus) {
        this.graus = graus;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UfcgAlumnusData that = (UfcgAlumnusData) o;
        return getRegistration().equals(that.getRegistration()) &&
                getFullName().equals(that.getFullName()) &&
                Arrays.equals(getGraus(), that.getGraus());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getRegistration(), getFullName());
        result = 31 * result + Arrays.hashCode(getGraus());
        return result;
    }

    @Override
    public String toString() {
        return "UfcgAlumnusData{" +
                "registration='" + registration + '\'' +
                ", fullName='" + fullName + '\'' +
                ", graus=" + Arrays.toString(graus) +
                '}';
    }
}
