package co2;

import java.util.*;

public class LCP {
    public static int[] build(String text, int[] suffixArray) {
        int n = text.length(), rank[] = new int[n], lcp[] = new int[n];
        for (int i = 0; i < n; i++) rank[suffixArray[i]] = i;
        for (int i = 0, k = 0; i < n; i++) {
            if (rank[i] == n - 1) { k = 0; continue; }
            int j = suffixArray[rank[i] + 1];
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) k++;
            lcp[rank[i]] = k;
            if (k > 0) k--;
        }
        return lcp;
    }

    public static void main(String[] args) {
        String text = "banana";
        System.out.println(Arrays.toString(build(text, SuffixArray.build(text))));
    }
}
