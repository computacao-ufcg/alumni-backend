package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.MatchClassification;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.models.PossibleMatch;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassificationCalculator {
    private Logger LOGGER = Logger.getLogger(ClassificationCalculator.class);

    private static ClassificationCalculator instance;

    private ClassificationCalculator() {
    }

    public static ClassificationCalculator getInstance() {
        synchronized (ClassificationCalculator.class) {
            if (instance == null) {
                instance = new ClassificationCalculator();
            }
            return instance;
        }
    }

    private int[] getScoresArray(List<PendingMatch> pendingMatches) {
        List<Integer> scoresList = new ArrayList<>();
        for (PendingMatch pendingMatch : pendingMatches) {
            List<Integer> pendingMatchScores = pendingMatch.
                    getPossibleMatches()
                    .stream()
                    .map(PossibleMatch::getScore)
                    .collect(Collectors.toList());

            scoresList.addAll(pendingMatchScores);
        }

        scoresList.sort(Integer::compareTo);

        int[] scores = new int[scoresList.size()];
        for (int i = 0; i < scoresList.size(); i++)
            scores[i] = scoresList.get(i);

        return scores;
    }

    public MatchClassification getClassification(PossibleMatch possibleMatch, List<PendingMatch> pendingMatches) {
        int profileScore = possibleMatch.getScore();
        int[] scores = getScoresArray(pendingMatches);
        int totalProfiles = scores.length;

        int veryUnlikelyMaxBorder = scores[(int) Math.floor(totalProfiles * 0.25) - 1];
        int unlikelyMaxBorder = scores[(int) Math.floor(totalProfiles * 0.5) - 1];
        int averageMaxBorder = scores[(int) Math.floor(totalProfiles * 0.75) - 1];
        int likelyMaxBorder = scores[(int) Math.floor(totalProfiles * 0.9) - 1];

        if (profileScore <= veryUnlikelyMaxBorder) {
            return MatchClassification.VERY_UNLIKELY;
        } else if (profileScore <= unlikelyMaxBorder) {
            return MatchClassification.UNLIKELY;
        } else if (profileScore <= averageMaxBorder) {
            return MatchClassification.AVERAGE;
        } else if (profileScore <= likelyMaxBorder) {
            return MatchClassification.LIKELY;
        } else {
            return MatchClassification.VERY_LIKELY;
        }
    }
}
