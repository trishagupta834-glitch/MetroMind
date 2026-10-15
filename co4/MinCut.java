package co4;

import java.util.ArrayDeque;
import java.util.Queue;

public class MinCut {
    public static boolean[] reachableAfterMaxFlow(FlowNetwork net, int s) {
        boolean[] seen = new boolean[net.graph.length];
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        seen[s] = true;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (FlowEdge e : net.graph[v]) if (e.cap > 0 && !seen[e.to]) {
                seen[e.to] = true;
                q.add(e.to);
            }
        }
        return seen;
    }
}
