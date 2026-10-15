package co3;

public class Levenshtein {
    public static int distance(String a, String b) {
        int[] dp = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) dp[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            int previous = dp[0];
            dp[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int old = dp[j];
                dp[j] = a.charAt(i - 1) == b.charAt(j - 1) ? previous
                        : 1 + Math.min(previous, Math.min(dp[j], dp[j - 1]));
                previous = old;
            }
        }
        return dp[b.length()];
    }

    public static void main(String[] args) {
        System.out.println(distance("Central", "Centril"));
    }
}
