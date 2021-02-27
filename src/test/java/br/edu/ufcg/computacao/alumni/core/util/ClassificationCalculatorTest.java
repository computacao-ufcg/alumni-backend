package br.edu.ufcg.computacao.alumni.core.util;

import br.edu.ufcg.computacao.alumni.core.models.MatchClassification;
import br.edu.ufcg.computacao.alumni.core.models.PendingMatch;
import br.edu.ufcg.computacao.alumni.core.models.PossibleMatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClassificationCalculatorTest {

    private ClassificationCalculator calculator;
    private List<PendingMatch> pendingMatches;

    @Before
    public void setUp() {
        this.calculator = ClassificationCalculator.getInstance();
        this.pendingMatches = Arrays.asList(createFakePendingMatch(20, 30, 40),
                createFakePendingMatch(20, 30, 80),
                createFakePendingMatch(50, 90, 200),
                createFakePendingMatch(50, 60, 70),
                createFakePendingMatch(40, 90, 150));
        // the scores array will be: [20, 20, 30, 30, 40, 40, 50, 50, 60, 70, 80, 90, 90, 150, 200], median = 50
        // VERY_UNLIKELY: <= 25% ( scores[(15 * 0.25) - 1] = scores[2] = 30) : score <= 30
        // UNLIKELY: 25% - 50% ( scores[(15 * 0.50) - 1] = scores[6] = 50) : 30 < score <= 50
        // AVERAGE: 50% - 75% ( scores[(15 * 0.75) - 1] = scores[10] = 80) : 50 < score <= 80
        // LIKELY: 75% - 90% ( scores[(15 * 0.9) - 1] = scores[12] = 90) : 80 < score <= 90
        // VERY_LIKELY: >= 90% : score > 90
    }

    @Test
    public void testVeryUnlikelyClassification() {
        PossibleMatch possibleMatchToClassificate = createFakePossibleMatch(20);
        MatchClassification classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.VERY_UNLIKELY, classification);

        possibleMatchToClassificate = createFakePossibleMatch(30);
        classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.VERY_UNLIKELY, classification);
    }

    @Test
    public void testUnlikelyClassification() {
        PossibleMatch possibleMatchToClassificate = createFakePossibleMatch(40);
        MatchClassification classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.UNLIKELY, classification);

        possibleMatchToClassificate = createFakePossibleMatch(50);
        classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.UNLIKELY, classification);
    }

    @Test
    public void testAverageClassification() {
        PossibleMatch possibleMatchToClassificate = createFakePossibleMatch(60);
        MatchClassification classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.AVERAGE, classification);

        possibleMatchToClassificate = createFakePossibleMatch(80);
        classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.AVERAGE, classification);
    }

    @Test
    public void testLikelyClassification() {
        PossibleMatch possibleMatchToClassificate = createFakePossibleMatch(85);
        MatchClassification classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.LIKELY, classification);

        possibleMatchToClassificate = createFakePossibleMatch(90);
        classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.LIKELY, classification);
    }

    @Test
    public void testVeryLikelyClassification() {
        PossibleMatch possibleMatchToClassificate = createFakePossibleMatch(200);
        MatchClassification classification = calculator.getClassification(possibleMatchToClassificate, pendingMatches);

        Assert.assertEquals(MatchClassification.VERY_LIKELY, classification);
    }

    // creates a fake possible match
    private PossibleMatch createFakePossibleMatch(int score) {
        return new PossibleMatch(score, null);
    }

    // creates a fake pending match with 3 possible matches
    private PendingMatch createFakePendingMatch(int score1, int score2, int score3) {
        Collection<PossibleMatch> fakePossibleMatches = Arrays.asList(createFakePossibleMatch(score1), createFakePossibleMatch(score2),
                createFakePossibleMatch(score3));
        return new PendingMatch(null, fakePossibleMatches);
    }
}
