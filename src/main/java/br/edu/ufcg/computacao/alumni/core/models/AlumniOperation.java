package br.edu.ufcg.computacao.alumni.core.models;

public enum AlumniOperation {
    GET_ALUMNI("getAlumni"),
    GET_ALUMNI_NAMES("getAlumniNames"),
    GET_ALUMNI_CURRENT_JOB("getAlumniCurrentJob"),
    GET_ALUMNI_PENDING_MATCHES("getAlumniPendingMatches"),
    SET_MATCH("setMatch"),
    RESET_MATCH("resetMatch"),
    GET_ALUMNI_MATCHES("getAlumniMatches"),
    GET_LINKEDIN_ALUMNI_DATA("getLinkedinAlumniData"),
    GET_LINKEDIN_NAME_PROFILE_PAIRS("getLinkedinNameProfilePairs"),
    GET_EMPLOYERS("getEmployers"),
    GET_EMPLOYERS_BY_TYPE("getEmployersByType"),
    GET_EMPLOYERS_UNDEFINED("getEmployersUndefined"),
    SET_EMPLOYER_TYPE_TO_UNDEFINED("setEmployerTypeToUndefined"),
    SET_EMPLOYER_TYPE("setEmployerType"),
    GET_STATISTICS("getStatistics");

    private String value;

    AlumniOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
