package co1;

public class RabinKarp {
    public static int rabinKarp(String stream, String badTicket) {
        if (badTicket.isEmpty() || badTicket.length() > stream.length()) return -1;
        int m = badTicket.length(), hash = 0, window = 0, power = 1;
        for (int i = 0; i < m - 1; i++) power = (power * 256) % 101;
        for (int i = 0; i < m; i++) {
            hash = (hash * 256 + badTicket.charAt(i)) % 101;
            window = (window * 256 + stream.charAt(i)) % 101;
        }
        for (int i = 0; i <= stream.length() - m; i++) {
            if (hash == window && stream.regionMatches(i, badTicket, 0, m)) return i;
            if (i < stream.length() - m) {
                window = (256 * (window - stream.charAt(i) * power) + stream.charAt(i + m)) % 101;
                if (window < 0) window += 101;
            }
        }
        return -1;
    }

    public static void search(String text, String pattern) {
        int index = rabinKarp(text, pattern);
        if (index >= 0) System.out.println("Rabin-Karp match at " + index);
    }
    public static void main(String[] args) {
        search("Tickets T123 and T456 are valid.", "T123");
    }
}
