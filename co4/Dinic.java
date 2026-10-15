package co4;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Dinic {
    public static int maxFlow(FlowNetwork net, int s, int t) {
        int flow = 0;
        int[] level = new int[net.graph.length], ptr = new int[net.graph.length];
        while (bfs(net, s, t, level)) {
            Arrays.fill(ptr, 0);
            for (int pushed; (pushed = dfs(net, s, t, Integer.MAX_VALUE, ptr, level)) > 0;) flow += pushed;
        }
        return flow;
    }

    static boolean bfs(FlowNetwork net, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        level[s] = 0;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (FlowEdge e : net.graph[v]) if (e.cap > 0 && level[e.to] == -1) {
                level[e.to] = level[v] + 1;
                q.add(e.to);
            }
        }
        return level[t] != -1;
    }

    static int dfs(FlowNetwork net, int v, int t, int f, int[] ptr, int[] level) {
        if (v == t) return f;
        for (; ptr[v] < net.graph[v].size(); ptr[v]++) {
            FlowEdge e = net.graph[v].get(ptr[v]);
            if (e.cap > 0 && level[e.to] == level[v] + 1) {
                int pushed = dfs(net, e.to, t, Math.min(f, e.cap), ptr, level);
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
