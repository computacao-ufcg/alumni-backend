package br.edu.ufcg.computacao.alumni.core;

import br.edu.ufcg.computacao.alumni.api.http.response.ConsolidatedEmployer;
import br.edu.ufcg.computacao.alumni.core.holders.EmployersHolder;
import br.edu.ufcg.computacao.alumni.core.models.UnknownEmployer;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployerFinder {
    private static Logger LOGGER = Logger.getLogger(EmployerFinder.class);

    private static EmployerFinder instance;

    private EmployerFinder() {};

    public static EmployerFinder getInstance() {
        synchronized (EmployerFinder.class) {
            if (instance == null) {
                instance = new EmployerFinder();
            }
            return instance;
        }
    }

    public Set<ConsolidatedEmployer> getPossibleEmployers(UnknownEmployer unknownEmployer) {
        Set<ConsolidatedEmployer> possibleEmployers = new HashSet<>();
        Collection<ConsolidatedEmployer> unclassifiedEmployers = EmployersHolder.getInstance().getUnclassifiedEmployers();

        Pattern namePattern = Pattern.compile(unknownEmployer.getName(), Pattern.CASE_INSENSITIVE);
        for (ConsolidatedEmployer consolidatedEmployer : unclassifiedEmployers) {
            Matcher nameMatcher = namePattern.matcher(consolidatedEmployer.getName());

            if (nameMatcher.find()) {
                LOGGER.info(String.format("%s is a possible match for %s\n", consolidatedEmployer.getName(), unknownEmployer.getName()));
                possibleEmployers.add(consolidatedEmployer);
            }
        }
        return possibleEmployers;
    }
}
