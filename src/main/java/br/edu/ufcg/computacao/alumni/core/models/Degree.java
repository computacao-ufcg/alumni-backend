package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Objects;

public class Degree {
    private CourseName courseName;
    private Level level;
    private String admission;
    private String graduation;

    public Degree(CourseName curso, Level level, String admission, String graduation) {
        this.courseName = curso;
        this.level = level;
        this.admission = admission;
        this.graduation = graduation;
    }

    public CourseName getCourseName() {
        return courseName;
    }

    public void setCourseName(CourseName courseName) {
        this.courseName = courseName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Degree degree = (Degree) o;
        return getCourseName() == degree.getCourseName() &&
                getLevel() == degree.getLevel() &&
                getAdmission().equals(degree.getAdmission()) &&
                getGraduation().equals(degree.getGraduation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseName(), getLevel(), getAdmission(), getGraduation());
    }

    @Override
    public String toString() {
        return "Degree{" +
                "courseName=" + courseName +
                "level=" + level +
                ", admission='" + admission + '\'' +
                ", graduation='" + graduation + '\'' +
                '}';
    }
}
