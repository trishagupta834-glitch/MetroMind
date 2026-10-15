package co2;

import java.util.*;

public class SAIS {
    public static int[] build(String text) {
        int[] input = new int[text.length() + 1];
        int upper = 0;
        for (int i = 0; i < text.length(); i++) {
            input[i] = text.charAt(i) + 1;
            upper = Math.max(upper, input[i]);
        }
        int[] all = saIs(input, upper);
        return Arrays.copyOfRange(all, 1, all.length);
    }

    static int[] saIs(int[] s, int upper) {
        int n = s.length;
        if (n <= 1) return new int[]{0};
        boolean[] type = new boolean[n];
        type[n - 1] = true;
        for (int i = n - 2; i >= 0; i--) type[i] = s[i] == s[i + 1] ? type[i + 1] : s[i] < s[i + 1];
        int[] sumL = new int[upper + 1], sumS = new int[upper + 1];
        for (int i = 0; i < n; i++) if (type[i]) sumL[s[i]]++; else sumS[s[i]]++;
        for (int i = 0; i <= upper; i++) {
            sumS[i] += sumL[i];
            if (i < upper) sumL[i + 1] += sumS[i];
        }
        int[] lmsMap = new int[n + 1];
        Arrays.fill(lmsMap, -1);
        int m = 0;
        for (int i = 1; i < n; i++) if (!type[i - 1] && type[i]) lmsMap[i] = m++;
        int[] lms = new int[m];
        for (int i = 1, j = 0; i < n; i++) if (!type[i - 1] && type[i]) lms[j++] = i;
        int[] sa = induce(s, type, sumL, sumS, lms);
        if (m > 0) {
            int[] sorted = new int[m];
            for (int i = 0, j = 0; i < n; i++) if (lmsMap[sa[i]] != -1) sorted[j++] = sa[i];
            int[] reduced = new int[m];
            int name = 0;
            reduced[lmsMap[sorted[0]]] = 0;
            for (int i = 1; i < m; i++) {
                int left = sorted[i - 1], right = sorted[i];
                int leftEnd = nextLms(lms, left), rightEnd = nextLms(lms, right);
                if (leftEnd - left != rightEnd - right) name++;
                else for (int k = 0; k < rightEnd - right; k++)
                    if (s[left + k] != s[right + k] || type[left + k] != type[right + k]) { name++; break; }
                reduced[lmsMap[right]] = name;
            }
            int[] ordered = saIs(reduced, name);
            for (int i = 0; i < m; i++) sorted[i] = lms[ordered[i]];
            sa = induce(s, type, sumL, sumS, sorted);
        }
        return sa;
    }

    static int nextLms(int[] lms, int value) {
        for (int item : lms) if (item > value) return item;
        return lms[lms.length - 1] + 1;
    }

    static int[] induce(int[] s, boolean[] type, int[] sumL, int[] sumS, int[] lms) {
        int[] sa = new int[s.length];
        Arrays.fill(sa, -1);
        int[] buffer = sumS.clone();
        for (int i = lms.length - 1; i >= 0; i--) sa[--buffer[s[lms[i]]]] = lms[i];
        buffer = sumL.clone();
        sa[buffer[s[s.length - 1]]++] = s.length - 1;
        for (int i = 0; i < s.length; i++) if (sa[i] >= 1 && !type[sa[i] - 1]) sa[buffer[s[sa[i] - 1]]++] = sa[i] - 1;
        buffer = sumS.clone();
        for (int i = s.length - 1; i >= 0; i--) if (sa[i] >= 1 && type[sa[i] - 1]) sa[--buffer[s[sa[i] - 1]]] = sa[i] - 1;
        return sa;
    }
}
