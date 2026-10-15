package co3;

import java.util.Arrays;

public class BitmaskDP {
    public static class Result {
        public int cost;
        public String order;
        Result(int cost, String order) { this.cost = cost; this.order = order; }
        public String toString() { return "Cost: " + cost + " mins | Order: " + order; }
    }

    public static Result shortestTour(int[][] distances) {
        String[] stationNames = new String[distances.length];
        for (int station = 0; station < stationNames.length; station++)
            stationNames[station] = "Station " + (station + 1);
        return shortestTour(distances, stationNames);
    }

    public static Result shortestTour(int[][] distances, String[] stationNames) {
        int n = distances.length, size = 1 << n, inf = Integer.MAX_VALUE / 4;
        if (n < 2 || n > 15 || stationNames.length != n)
            throw new IllegalArgumentException("Tour planning supports 2-15 named stations.");
        for (int[] row : distances)
            if (row == null || row.length != n) throw new IllegalArgumentException("Distance table must be square.");
        int[][] dp = new int[size][n];
        int[][] parent = new int[size][n];
        for (int mask = 0; mask < size; mask++) Arrays.fill(dp[mask], inf);
        dp[1][0] = 0;
        for (int mask = 1; mask < size; mask++)
            for (int last = 0; last < n; last++) if (dp[mask][last] < inf)
                for (int next = 0; next < n; next++) if ((mask & (1 << next)) == 0) {
                    int newMask = mask | (1 << next), cost = dp[mask][last] + distances[last][next];
                    if (cost < dp[newMask][next]) {
                        dp[newMask][next] = cost;
                        parent[newMask][next] = last;
                    }
                }
        int full = size - 1, last = 1, cost = inf;
        for (int i = 1; i < n; i++) if (dp[full][i] + distances[i][0] < cost) {
            cost = dp[full][i] + distances[i][0];
            last = i;
        }
        int[] path = new int[n + 1];
        path[n] = 0;
        for (int i = n - 1, mask = full; i >= 0; i--) {
            path[i] = last;
            int previous = parent[mask][last];
            mask ^= 1 << last;
            last = previous;
        }
        StringBuilder order = new StringBuilder();
        for (int station : path) order.append(stationNames[station]).append(" -> ");
        order.setLength(order.length() - 4);
        return new Result(cost, order.toString());
    }

}
