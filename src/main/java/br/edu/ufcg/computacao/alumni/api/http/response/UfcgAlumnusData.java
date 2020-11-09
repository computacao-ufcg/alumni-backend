package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.core.models.Degree;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Objects;

public class UfcgAlumnusData {
    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.REGISTRATION)
    private String registration;
    @ApiModelProperty(position = 1, example = ApiDocumentation.Model.FULL_NAME)
    private String fullName;
    private Degree[] degrees;

    public UfcgAlumnusData(String registration, String fullName, Degree[] degrees) {
        this.fullName = fullName;
        this.degrees = degrees;
        this.registration = registration;
    }

    public UfcgAlumnusData(String fullName, Degree[] degrees) {
        this.fullName = fullName;
        this.degrees = degrees;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Degree[] getDegrees() {
        return degrees;
    }

    public void setDegrees(Degree[] degrees) {
        this.degrees = degrees;
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
                Arrays.equals(getDegrees(), that.getDegrees());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getRegistration(), getFullName());
        result = 31 * result + Arrays.hashCode(getDegrees());
        return result;
    }

    @Override
    public String toString() {
        return "UfcgAlumnusData{" +
                "registration='" + registration + '\'' +
                ", fullName='" + fullName + '\'' +
                ", degrees=" + Arrays.toString(degrees) +
                '}';
    }
}
