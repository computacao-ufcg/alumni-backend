package br.edu.ufcg.computacao.alumni.core.models;

public enum AlumniOperation {
    GET_ALUMNI("getAlumni"),
    GET_ALUMNI_NAMES("getAlumniNames"),
    GET_ALUMNI_CURRENT_JOB("getAlumniCurrentJob"),
    GET_LINKEDIN_ALUMNI_DATA("getLinkedinAlumniData"),
    GET_LINKEDIN_NAME_PROFILE_PAIRS("getLinkedinNameProfilePairs");

    private String value;

    AlumniOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
