package br.edu.ufcg.computacao.alumni.api.http.response;

import br.edu.ufcg.computacao.alumni.constants.ApiDocumentation;
import br.edu.ufcg.computacao.alumni.constants.CourseNameCode;
import br.edu.ufcg.computacao.alumni.constants.LevelCode;
import br.edu.ufcg.computacao.alumni.core.models.CourseName;
import br.edu.ufcg.computacao.alumni.core.models.Degree;
import br.edu.ufcg.computacao.alumni.core.models.Level;
import br.edu.ufcg.computacao.eureca.backend.api.http.response.AlumniPerStudentSummary;
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

    public UfcgAlumnusData(AlumniPerStudentSummary alumnus) {
        this.registration = alumnus.getRegistration();
        this.fullName = alumnus.getName();

        CourseName courseName = null;
        switch (Integer.toString(alumnus.getCourse())) {
            case CourseNameCode.DATA_PROCESSING:
                courseName = CourseName.DATA_PROCESSING;
                break;
            case CourseNameCode.COMPUTING_SCIENCE:
                courseName = CourseName.COMPUTING_SCIENCE;
                break;
            case CourseNameCode.INFORMATICS:
                courseName = CourseName.INFORMATICS;
                break;
            default:
                break;
        }

        Level level = null;
        switch (Integer.toString(alumnus.getLevel())) {
            case LevelCode.UNDERGRADUATE:
                level = Level.UNDERGRADUATE;
                break;
            case LevelCode.MASTER:
                level = Level.MASTER;
                break;
            case LevelCode.DOCTORATE:
                level = Level.DOCTORATE;
                break;
            default:
                break;
        }

        String admission = alumnus.getAdmissionTerm();
        String graduation = alumnus.getGraduationTerm();

        this.degrees = new Degree[1];
        this.degrees[0] = new Degree(courseName, level, admission, graduation);
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
