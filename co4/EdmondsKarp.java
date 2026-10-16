package co4;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class EdmondsKarp {
    public static int maxFlow(FlowNetwork net, int s, int t) {
        int flow = 0;
        while (true) {
            int[] parent = new int[net.graph.length], edge = new int[net.graph.length];
            Arrays.fill(parent, -1);
            Queue<Integer> q = new ArrayDeque<>();
            q.add(s);
            parent[s] = s;
            while (!q.isEmpty() && parent[t] == -1) {
                int v = q.poll();
                for (int i = 0; i < net.graph[v].size(); i++) {
                    FlowEdge e = net.graph[v].get(i);
                    if (parent[e.to] == -1 && e.cap > 0) {
                        parent[e.to] = v;
                        edge[e.to] = i;
                        q.add(e.to);
                    }
                }
            }
            if (parent[t] == -1) return flow;
            int add = Integer.MAX_VALUE;
            for (int v = t; v != s; v = parent[v]) add = Math.min(add, net.graph[parent[v]].get(edge[v]).cap);
            for (int v = t; v != s; v = parent[v]) {
                FlowEdge e = net.graph[parent[v]].get(edge[v]);
                e.cap -= add;
                net.graph[e.to].get(e.rev).cap += add;
            }
            flow += add;
        }
    }
}
