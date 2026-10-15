package co2;

import java.util.*;

public class SuffixArray {
    public static int[] build(String text) {
        int length = text.length();
        Integer[] order = new Integer[length];
        int[] rank = new int[length];
        for (int index = 0; index < length; index++) {
            order[index] = index;
            rank[index] = text.charAt(index);
        }

        for (int offset = 1; offset < length; offset *= 2) {
            int currentOffset = offset;
            int[] currentRank = rank;
            Arrays.sort(order, (first, second) -> {
                int firstRank = currentRank[first];
                int secondRank = currentRank[second];
                if (firstRank != secondRank) return Integer.compare(firstRank, secondRank);
                int firstNext = first + currentOffset < length ? currentRank[first + currentOffset] : -1;
                int secondNext = second + currentOffset < length ? currentRank[second + currentOffset] : -1;
                return Integer.compare(firstNext, secondNext);
            });

            int[] nextRank = new int[length];
            for (int index = 1; index < length; index++) {
                int previous = order[index - 1];
                int current = order[index];
                boolean different = rank[previous] != rank[current]
                        || (previous + currentOffset < length ? rank[previous + currentOffset] : -1)
                        != (current + currentOffset < length ? rank[current + currentOffset] : -1);
                nextRank[current] = nextRank[previous] + (different ? 1 : 0);
            }
            rank = nextRank;
            if (rank[order[length - 1]] == length - 1 || offset > length / 2) break;
        }

        int[] suffixArray = new int[length];
        for (int index = 0; index < length; index++) suffixArray[index] = order[index];
        return suffixArray;
    }

    public static int[] search(String text, String pattern) {
        return search(text, build(text), pattern);
    }

    public static int[] search(String text, int[] suffixArray, String pattern) {
        if (pattern.isEmpty() || text.isEmpty()) return new int[0];
        if (suffixArray.length != text.length())
            throw new IllegalArgumentException("Suffix array must index the supplied text.");
        int first = lowerBound(text, pattern, suffixArray);
        int last = upperBound(text, pattern, suffixArray, first);
        int[] matches = new int[last - first];
        for (int index = first; index < last; index++) matches[index - first] = suffixArray[index];
        Arrays.sort(matches);
        return matches;
    }

    private static int lowerBound(String text, String pattern, int[] suffixArray) {
        int low = 0, high = suffixArray.length;
        while (low < high) {
            int middle = (low + high) >>> 1;
            if (compareSuffixToPattern(text, suffixArray[middle], pattern) < 0) low = middle + 1;
            else high = middle;
        }
        return low;
    }

    private static int upperBound(String text, String pattern, int[] suffixArray, int low) {
        int high = suffixArray.length;
        while (low < high) {
            int middle = (low + high) >>> 1;
            if (compareSuffixToPattern(text, suffixArray[middle], pattern) <= 0) low = middle + 1;
            else high = middle;
        }
        return low;
    }

    private static int compareSuffixToPattern(String text, int suffixStart, String pattern) {
        int comparedLength = Math.min(text.length() - suffixStart, pattern.length());
        for (int offset = 0; offset < comparedLength; offset++) {
            int comparison = Character.compare(text.charAt(suffixStart + offset), pattern.charAt(offset));
            if (comparison != 0) return comparison;
        }
        if (comparedLength == pattern.length()) return 0;
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(build("banana")));
    }
}
