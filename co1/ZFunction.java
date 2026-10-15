package co1;

import java.util.*;

public class ZFunction {
    public static int[] computeZArray(String s) {
        int[] z = new int[s.length()];
        for (int i = 1, left = 0, right = 0; i < s.length(); i++) {
            if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
            while (i + z[i] < s.length() && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > right) { left = i; right = i + z[i] - 1; }
        }
        return z;
    }

    public static List<Integer> find(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) return result;
        int[] z = computeZArray(pattern + "$" + text);
        for (int i = pattern.length() + 1; i < z.length; i++)
            if (z[i] >= pattern.length()) result.add(i - pattern.length() - 1);
        return result;
    }

    public static void search(String text, String pattern) {
        for (int index : find(text, pattern)) System.out.println("Z-Function match at " + index);
    }
    public static void main(String[] args) {
        search("Route 123 delayed. Route 123 resumed.", "Route 123");
    }
}
