package co2;

import java.util.*;

public class LCP {
    public static class Match {
        public final int firstStart;
        public final int secondStart;
        public final String text;

        private Match(int firstStart, int secondStart, String text) {
            this.firstStart = firstStart;
            this.secondStart = secondStart;
            this.text = text;
        }
    }

    public static class RouteMatch {
        public final int firstStart;
        public final int secondStart;
        public final List<String> stations;

        private RouteMatch(int firstStart, int secondStart, List<String> stations) {
            this.firstStart = firstStart;
            this.secondStart = secondStart;
            this.stations = List.copyOf(stations);
        }
    }

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

    public static Match longestCommonSubstring(String first, String second) {
        String combined = first + '\u0000' + second + '\u0001';
        int[] suffixArray = SuffixArray.build(combined);
        int[] lcp = build(combined, suffixArray);
        int secondStart = first.length() + 1;
        int bestLength = 0;
        int bestFirstStart = -1;
        int bestSecondStart = -1;

        for (int index = 0; index + 1 < suffixArray.length; index++) {
            int leftStart = suffixArray[index];
            int rightStart = suffixArray[index + 1];
            boolean leftInFirst = leftStart < first.length();
            boolean rightInFirst = rightStart < first.length();
            if (leftInFirst == rightInFirst) continue;
            if (leftStart >= first.length() && leftStart < secondStart) continue;
            if (rightStart >= first.length() && rightStart < secondStart) continue;

            int firstStart = leftInFirst ? leftStart : rightStart;
            int secondPosition = leftInFirst ? rightStart : leftStart;
            int secondIndex = secondPosition - secondStart;
            if (secondIndex < 0 || secondIndex >= second.length()) continue;
            int commonLength = Math.min(lcp[index], Math.min(first.length() - firstStart,
                    second.length() - secondIndex));
            if (commonLength > bestLength) {
                bestLength = commonLength;
                bestFirstStart = firstStart;
                bestSecondStart = secondIndex;
            }
        }

        if (bestLength == 0) return new Match(-1, -1, "");
        String commonText = first.substring(bestFirstStart, bestFirstStart + bestLength);
        int leadingWhitespace = 0;
        while (leadingWhitespace < commonText.length()
            && Character.isWhitespace(commonText.charAt(leadingWhitespace))) leadingWhitespace++;
        int end = commonText.length();
        while (end > leadingWhitespace && Character.isWhitespace(commonText.charAt(end - 1))) end--;
        commonText = commonText.substring(leadingWhitespace, end);
        return commonText.isEmpty()
            ? new Match(-1, -1, "")
            : new Match(bestFirstStart + leadingWhitespace, bestSecondStart + leadingWhitespace, commonText);
    }

    public static RouteMatch longestCommonRouteSegment(List<String> first, List<String> second) {
        if (first == null || second == null) throw new IllegalArgumentException("Route station lists cannot be null.");
        if (first.isEmpty() || second.isEmpty()) return new RouteMatch(-1, -1, List.of());

        Map<String, Character> stationCodes = new HashMap<>();
        StringBuilder combined = new StringBuilder(first.size() + second.size() + 1);
        appendEncodedRoute(first, stationCodes, combined);
        combined.append('\u0000');
        int secondRouteStart = combined.length();
        appendEncodedRoute(second, stationCodes, combined);

        String encodedRoutes = combined.toString();
        int[] suffixArray = SuffixArray.build(encodedRoutes);
        int[] lcp = build(encodedRoutes, suffixArray);
        int bestLength = 0;
        int bestFirstStart = -1;
        int bestSecondStart = -1;

        for (int index = 0; index + 1 < suffixArray.length; index++) {
            int leftStart = suffixArray[index];
            int rightStart = suffixArray[index + 1];
            boolean leftInFirst = leftStart < first.size();
            boolean rightInFirst = rightStart < first.size();
            if (leftInFirst == rightInFirst) continue;

            int firstStart = leftInFirst ? leftStart : rightStart;
            int secondEncodedStart = leftInFirst ? rightStart : leftStart;
            int secondStart = secondEncodedStart - secondRouteStart;
            if (secondStart < 0 || secondStart >= second.size()) continue;

            int commonLength = Math.min(lcp[index], Math.min(first.size() - firstStart,
                    second.size() - secondStart));
            if (commonLength > bestLength) {
                bestLength = commonLength;
                bestFirstStart = firstStart;
                bestSecondStart = secondStart;
            }
        }

        if (bestLength == 0) return new RouteMatch(-1, -1, List.of());
        return new RouteMatch(bestFirstStart, bestSecondStart,
                first.subList(bestFirstStart, bestFirstStart + bestLength));
    }

    private static void appendEncodedRoute(List<String> route, Map<String, Character> stationCodes,
                                          StringBuilder encoded) {
        for (String station : route) {
            if (station == null || station.isBlank())
                throw new IllegalArgumentException("Route station names cannot be blank.");
            String normalized = station.trim().toUpperCase(Locale.ROOT);
            Character code = stationCodes.get(normalized);
            if (code == null) {
                if (stationCodes.size() >= Character.MAX_VALUE - 1)
                    throw new IllegalArgumentException("Too many unique station names to index.");
                code = (char) (stationCodes.size() + 2);
                stationCodes.put(normalized, code);
            }
            encoded.append(code);
        }
    }

    public static void main(String[] args) {
        String text = "banana";
        System.out.println(Arrays.toString(build(text, SuffixArray.build(text))));
    }
}
