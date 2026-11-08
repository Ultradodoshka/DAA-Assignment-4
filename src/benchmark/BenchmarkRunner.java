package benchmark;

import dp.ED;
import dp.LCS;
import sm.StringMatchers;
import advanced.KasaiLCP;

import java.util.Random;

public class BenchmarkRunner {
    private static final int RUNS = 5;
    private static final long SEED = 42L;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Random rng = new Random(SEED);

        System.out.println("=== PART A: DYNAMIC PROGRAMMING (LCS & Edit Distance) ===");
        int[] dpSizes = {100, 500, 1000};
        System.out.println("Size\tLCS Avg Time (ms)\tEdit Distance Avg Time (ms)");
        for (int size : dpSizes) {
            String s1 = generateRandomString(size, rng);
            String s2 = generateRandomString(size, rng);

            LCS.findLCS(s1.substring(0, 50), s2.substring(0, 50));
            ED.computeED(s1.substring(0, 50), s2.substring(0, 50));

            long totalLCS = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                LCS.findLCS(s1, s2);
                totalLCS += (System.nanoTime() - start);
            }
            double avgLCS = (totalLCS / (double) RUNS) / 1_000_000.0;

            long totalED = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                ED.computeED(s1, s2);
                totalED += (System.nanoTime() - start);
            }
            double avgED = (totalED / (double) RUNS) / 1_000_000.0;

            System.out.printf("%d\t%.4f ms\t\t%.4f ms\n", size, avgLCS, avgED);
        }

        System.out.println("\n=== PART B: STRING MATCHING ===");
        int[] textSizes = {10000, 50000, 100000};
        int patternLen = 20;
        System.out.println("TextLen\tPattern Status\tNaive (ms)\tKMP (ms)\tRabin-Karp (ms)");

        for (int tSize : textSizes) {
            String text = generateRandomString(tSize, rng);

            int startIdx = tSize / 2;
            String presentPattern = text.substring(startIdx, startIdx + patternLen);
            benchmarkMatchers(text, presentPattern, tSize, "Present");

            String absentPattern = "1234567890abcdefghij";
            benchmarkMatchers(text, absentPattern, tSize, "Absent");
        }

        System.out.println("\n=== PART C: KASAI'S ALGORITHM ===");
        int[] kasaiSizes = {1000, 5000, 10000};
        System.out.println("Size\tKasai LCP Avg Time (ms)");
        for (int size : kasaiSizes) {
            String s = generateRandomString(size, rng);
            int[] sa = KasaiLCP.buildSuffixArrayNaive(s);

            long totalKasai = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                KasaiLCP.buildLCP(s, sa);
                totalKasai += (System.nanoTime() - start);
            }
            double avgKasai = (totalKasai / (double) RUNS) / 1_000_000.0;
            System.out.printf("%d\t%.4f ms\n", size, avgKasai);
        }
    }

    private static void benchmarkMatchers(String text, String pattern, int tSize, String status) {
        long totalNaive = 0, totalKMP = 0, totalRK = 0;

        for (int r = 0; r < RUNS; r++) {
            long s1 = System.nanoTime();
            StringMatchers.naiveSearch(text, pattern);
            totalNaive += (System.nanoTime() - s1);

            long s2 = System.nanoTime();
            StringMatchers.kmpSearch(text, pattern);
            totalKMP += (System.nanoTime() - s2);

            long s3 = System.nanoTime();
            StringMatchers.rabinKarpSearch(text, pattern);
            totalRK += (System.nanoTime() - s3);
        }

        double avgNaive = (totalNaive / (double) RUNS) / 1_000_000.0;
        double avgKMP = (totalKMP / (double) RUNS) / 1_000_000.0;
        double avgRK = (totalRK / (double) RUNS) / 1_000_000.0;

        System.out.printf("%d\t%s\t\t%.4f ms\t%.4f ms\t%.4f ms\n", tSize, status, avgNaive, avgKMP, avgRK);
    }

    private static String generateRandomString(int length, Random rng) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(rng.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}