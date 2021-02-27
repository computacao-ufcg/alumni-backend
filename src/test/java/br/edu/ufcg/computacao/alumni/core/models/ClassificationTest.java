package br.edu.ufcg.computacao.alumni.core.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClassificationTest {

    private Classification classification;

    @Before
    public void setUp() {
        this.classification = new Classification(30, MatchClassification.UNLIKELY);
    }

    @Test
    public void testGetPriority() {
        Assert.assertEquals(MatchClassification.VERY_UNLIKELY.getPriority(), 0);
        Assert.assertEquals(MatchClassification.UNLIKELY.getPriority(), 1);
        Assert.assertEquals(MatchClassification.AVERAGE.getPriority(), 2);
        Assert.assertEquals(MatchClassification.LIKELY.getPriority(), 3);
        Assert.assertEquals(MatchClassification.VERY_LIKELY.getPriority(), 4);

        Assert.assertEquals(this.classification.getClassification().getPriority(), 1);
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals(MatchClassification.VERY_UNLIKELY.getValue(), "very-unlikely");
        Assert.assertEquals(MatchClassification.UNLIKELY.getValue(), "unlikely");
        Assert.assertEquals(MatchClassification.AVERAGE.getValue(), "average");
        Assert.assertEquals(MatchClassification.LIKELY.getValue(), "likely");
        Assert.assertEquals(MatchClassification.VERY_LIKELY.getValue(), "very-likely");

        Assert.assertEquals(this.classification.getClassification().getValue(), "unlikely");
    }

    @Test
    public void testGetClassification() {
        MatchClassification veryUnlikely = MatchClassification.getClassification("very-unlikely");
        MatchClassification unlikely = MatchClassification.getClassification("unlikely");
        MatchClassification average = MatchClassification.getClassification("average");
        MatchClassification likely = MatchClassification.getClassification("likely");
        MatchClassification veryLikely = MatchClassification.getClassification("very-likely");

        Assert.assertEquals(MatchClassification.VERY_UNLIKELY, veryUnlikely);
        Assert.assertEquals(MatchClassification.UNLIKELY, unlikely);
        Assert.assertEquals(MatchClassification.AVERAGE, average);
        Assert.assertEquals(MatchClassification.LIKELY, likely);
        Assert.assertEquals(MatchClassification.VERY_LIKELY, veryLikely);
    }

    @Test
    public void testGetInvalidClassification() {
        Assert.assertNull(MatchClassification.getClassification(null));
        Assert.assertNull(MatchClassification.getClassification("invalid"));
    }

    @Test
    public void testPriorityPrecedence() {
        Assert.assertTrue(MatchClassification.VERY_UNLIKELY.getPriority() < MatchClassification.UNLIKELY.getPriority());
        Assert.assertTrue(MatchClassification.UNLIKELY.getPriority() < MatchClassification.AVERAGE.getPriority());
        Assert.assertTrue(MatchClassification.AVERAGE.getPriority() < MatchClassification.LIKELY.getPriority());
        Assert.assertTrue(MatchClassification.LIKELY.getPriority() < MatchClassification.VERY_LIKELY.getPriority());
    }
}
