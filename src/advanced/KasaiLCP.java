package advanced;

public class KasaiLCP {

    public static int[] buildLCP(String s, int[] suffixArray) {
        int n = s.length();
        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int k = suffixArray[rank[i] - 1];
                while (i + h < n && k + h < n && s.charAt(i + h) == s.charAt(k + h)) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                lcp[0] = 0;
            }
        }
        return lcp;
    }

    public static int[] buildSuffixArrayNaive(String s) {
        int n = s.length();
        Integer[] sa = new Integer[n];
        for (int i = 0; i < n; i++) sa[i] = i;
        java.util.Arrays.sort(sa, (a, b) -> s.substring(a).compareTo(s.substring(b)));
        return java.util.Arrays.stream(sa).mapToInt(i -> i).toArray();
    }
}