package br.edu.ufcg.computacao.alumni.constants;

public class ApiDocumentation {
    public static class ApiInfo {
        public static final String CONTACT_NAME = "Computação@UFCG";
        public static final String CONTACT_URL = "https://computacao.ufcg.edu.br/";
        public static final String CONTACT_EMAIL = "fubica@computacao.ufcg.edu.br";
        public static final String API_TITLE = "Computação@UFCG Alumni Service API";
        public static final String API_DESCRIPTION = "This API allows management of information about Computação@UFCG alumni.";
    }

    public static class Model {
        public static final String ALUMNI = "[\n" +
                "    \"alumnus1\",\n" +
                "    \"alumnus2\"\n" +
                "  ]\n";
        public static final String ALUMNUS_NAME = "John Smith";
        public static final String CURRENT_JOB = "John Smith & Sons";
        public static final String START_YEAR = "2000";
    }

    public static class Alumni {
        public static final String API = "Queries information about the alumni.";
        public static final String DESCRIPTION = "Lists information about the alumni.";
        public static final String GET_NAMES_OPERATION = "Lists the names of the alumni.";
        public static final String GET_CURRENT_JOB_OPERATION = "Lists names and current position of alumni.";
        public static final String GET_MATCHES_OPERATION = "Lists matches between alumni and linkedin alumnus";
        public static final String GET_PENDING_MATCHES_OPERATION = "Lists alumni pending matches";
        public static final String SET_MATCH_OPERATION = "set alumus matches";
        public static final String UNSET_MATCH_OPERATION = "revert setMatch operation";
    }

    public static class Version {
        public static final String API = "Queries the version of the service's API.";
        public static final String GET_OPERATION = "Returns the version of the service's API.";
    }

    public class Token {
        public static final String AUTHENTICATION_TOKEN = "Authentication token.";
    }

    public static class PublicKey {
        public static final String API = "Queries the public key of the service.";
        public static final String GET_OPERATION = "Returns the public key of the service.";
    }
}
