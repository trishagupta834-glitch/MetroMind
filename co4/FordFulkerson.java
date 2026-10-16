package co4;

public class FordFulkerson {
    public static int maxFlow(FlowNetwork net, int s, int t) {
        int flow = 0;
        boolean[] seen;
        int pushed;
        do {
            seen = new boolean[net.graph.length];
            pushed = dfs(net, s, t, Integer.MAX_VALUE, seen);
            flow += pushed;
        } while (pushed > 0);
        return flow;
    }

    static int dfs(FlowNetwork net, int v, int t, int f, boolean[] seen) {
        if (v == t) return f;
        seen[v] = true;
        for (FlowEdge e : net.graph[v]) {
            if (!seen[e.to] && e.cap > 0) {
                int pushed = dfs(net, e.to, t, Math.min(f, e.cap), seen);
                if (pushed > 0) {
                    e.cap -= pushed;
                    net.graph[e.to].get(e.rev).cap += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }
}
