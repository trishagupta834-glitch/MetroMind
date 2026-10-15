package co3;

public class OptimalBST {
    static class Node {
        String key;
        Node left, right;
        Node(String key) { this.key = key; }
    }

    public static Node buildOBST(String[] stations, double[] frequencies) {
        int n = stations.length;
        double[][] dp = new double[n][n];
        int[][] root = new int[n][n];
        for (int i = 0; i < n; i++) { dp[i][i] = frequencies[i]; root[i][i] = i; }
        for (int length = 2; length <= n; length++)
            for (int i = 0; i + length <= n; i++) {
                int j = i + length - 1;
                double sum = 0, best = Double.MAX_VALUE;
                for (int k = i; k <= j; k++) sum += frequencies[k];
                for (int k = i; k <= j; k++) {
                    double left = k == i ? 0 : dp[i][k - 1], right = k == j ? 0 : dp[k + 1][j];
                    if (left + right < best) { best = left + right; root[i][j] = k; }
                }
                dp[i][j] = best + sum;
            }
        return n == 0 ? null : makeTree(stations, root, 0, n - 1);
    }

    static Node makeTree(String[] stations, int[][] root, int i, int j) {
        if (i > j) return null;
        int k = root[i][j];
        Node node = new Node(stations[k]);
        node.left = makeTree(stations, root, i, k - 1);
        node.right = makeTree(stations, root, k + 1, j);
        return node;
    }

    public static void printTree(Node node, String indent) {
        if (node == null) return;
        System.out.println(indent + node.key);
        printTree(node.left, indent + "  ");
        printTree(node.right, indent + "  ");
    }

    public static int minCost(int[] frequency) {
        int n = frequency.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) dp[i][i] = frequency[i];
        for (int length = 2; length <= n; length++)
            for (int i = 0; i + length <= n; i++) {
                int j = i + length - 1, sum = 0;
                for (int k = i; k <= j; k++) sum += frequency[k];
                dp[i][j] = Integer.MAX_VALUE;
                for (int root = i; root <= j; root++) {
                    int left = root == i ? 0 : dp[i][root - 1];
                    int right = root == j ? 0 : dp[root + 1][j];
                    dp[i][j] = Math.min(dp[i][j], left + right + sum);
                }
            }
        return n == 0 ? 0 : dp[0][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(minCost(new int[]{3, 2, 4}));
    }
}
