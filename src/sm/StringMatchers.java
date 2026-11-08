package sm;

import java.util.ArrayList;
import java.util.List;

public class StringMatchers {

    public static List<Integer> naiveSearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return occurrences;
        }

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                occurrences.add(i);
            }
        }
        return occurrences;
    }

    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return occurrences;
        }

        int[] lps = computeLPS(pattern);
        int n = text.length();
        int m = pattern.length();
        int i = 0;
        int j = 0;

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                occurrences.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return occurrences;
    }

    private static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }


    public static List<Integer> rabinKarpSearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return occurrences;
        }

        int n = text.length();
        int m = pattern.length();
        int prime = 101;
        int alphabetSize = 256;

        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * alphabetSize) % prime;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (alphabetSize * patternHash + pattern.charAt(i)) % prime;
            textHash = (alphabetSize * textHash + text.charAt(i)) % prime;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    occurrences.add(i);
                }
            }
            if (i < n - m) {
                textHash = (alphabetSize * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % prime;
                if (textHash < 0) {
                    textHash = (textHash + prime);
                }
            }
        }
        return occurrences;
    }
}