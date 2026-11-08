package test;

import dp.ED;
import dp.LCS;
import sm.StringMatchers;
import advanced.KasaiLCP;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


public class AlgorithmCorrectnessTest {

    @Test
    public void testLCS_IdenticalStrings() {
        assertEquals(5, LCS.findLCS("ABCDE", "ABCDE"));
    }

    @Test
    public void testLCS_CompletelyDifferentStrings() {
        assertEquals(0, LCS.findLCS("ABC", "XYZ"));
    }

    @Test
    public void testLCS_EmptyString() {
        assertEquals(0, LCS.findLCS("", "ABCDE"));
        assertEquals(0, LCS.findLCS("ABCDE", ""));
        assertEquals(0, LCS.findLCS("", ""));
    }

    @Test
    public void testEditDistance_IdenticalStrings() {
        assertEquals(0, ED.computeED("KITTEN", "KITTEN"));
    }

    @Test
    public void testEditDistance_EmptyString() {
        assertEquals(3, ED.computeED("", "CAT"));
        assertEquals(4, ED.computeED("WORD", ""));
        assertEquals(0, ED.computeED("", ""));
    }

    @Test
    public void testEditDistance_Operations() {
        assertEquals(1, ED.computeED("CAT", "HAT"));

        assertEquals(1, ED.computeED("CAT", "CATS"));

        assertEquals(1, ED.computeED("CATS", "CAT"));

        assertEquals(3, ED.computeED("kitten", "sitting"));
    }

    private void assertAllMatchersEqual(String text, String pattern, List<Integer> expected) {
        List<Integer> naiveRes = StringMatchers.naiveSearch(text, pattern);
        List<Integer> kmpRes = StringMatchers.kmpSearch(text, pattern);
        List<Integer> rkRes = StringMatchers.rabinKarpSearch(text, pattern);

        assertEquals(expected, naiveRes, "Naive failed for: " + pattern);
        assertEquals(expected, kmpRes, "KMP failed for: " + pattern);
        assertEquals(expected, rkRes, "Rabin-Karp failed for: " + pattern);
    }

    @Test
    public void testStringMatching_Positions() {
        assertAllMatchersEqual("HELLO WORLD", "HELLO", List.of(0));
        assertAllMatchersEqual("HELLO BEAUTIFUL WORLD", "BEAUTIFUL", List.of(6));
        assertAllMatchersEqual("HELLO WORLD", "WORLD", List.of(6));
    }

    @Test
    public void testStringMatching_MultipleAndOverlappingOccurrences() {
        assertAllMatchersEqual("AAAAA", "AA", List.of(0, 1, 2, 3));
        assertAllMatchersEqual("A B A B A", "B", List.of(2, 6));
    }

    @Test
    public void testStringMatching_Absent() {
        assertAllMatchersEqual("ABCDEF", "XYZ", List.of());
    }

    @Test
    public void testStringMatching_PatternLongerThanText() {
        assertAllMatchersEqual("SHORT", "VERY_LONG_STRING", List.of());
    }

    @Test
    public void testStringMatching_EmptyPattern() {
        assertAllMatchersEqual("ANY TEXT", "", List.of());
    }

    @Test
    public void testKasaiLCP_Banana() {
        String s = "banana";
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        int[] expectedLCP = {0, 1, 3, 0, 0, 2};

        int[] actualLCP = KasaiLCP.buildLCP(s, suffixArray);
        assertArrayEquals(expectedLCP, actualLCP);
    }
}