package co1;

import java.util.*;

public class KMP {
    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern.isEmpty() || pattern.length() > text.length()) return matches;
        int[] lps = new int[pattern.length()];
        for (int i = 1, len = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else i++;
        }
        for (int i = 0, j = 0; i < text.length();) {
            if (text.charAt(i) == pattern.charAt(j)) { i++; j++; }
            if (j == pattern.length()) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {
                if (j > 0) j = lps[j - 1]; else i++;
            }
        }
        return matches;
    }

    public static void search(String text, String pattern) {
        for (int index : kmpSearch(text, pattern)) System.out.println("KMP match at " + index);
    }
    public static void main(String[] args) {
        search("Station A has a service alert.", "alert");
    }
}
