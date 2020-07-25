package br.edu.ufcg.computacao.alumni.core.models;

import java.util.Objects;

public class Grau {
    private Curso curso;
    private String semestreIngresso;
    private String semestreFormatura;

    public Grau(Curso curso, String semestreIngresso, String semestreFormatura) {
        this.curso = curso;
        this.semestreIngresso = semestreIngresso;
        this.semestreFormatura = semestreFormatura;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getSemestreIngresso() {
        return semestreIngresso;
    }

    public void setSemestreIngresso(String semestreIngresso) {
        this.semestreIngresso = semestreIngresso;
    }

    public String getSemestreFormatura() {
        return semestreFormatura;
    }

    public void setSemestreFormatura(String semestreFormatura) {
        this.semestreFormatura = semestreFormatura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grau grau = (Grau) o;
        return getCurso() == grau.getCurso() &&
                getSemestreIngresso().equals(grau.getSemestreIngresso()) &&
                getSemestreFormatura().equals(grau.getSemestreFormatura());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurso(), getSemestreIngresso(), getSemestreFormatura());
    }

    @Override
    public String toString() {
        return "Grau{" +
                "curso=" + curso +
                ", semestreIngresso='" + semestreIngresso + '\'' +
                ", semestreFormatura='" + semestreFormatura + '\'' +
                '}';
    }
}
