package co4;

import java.util.ArrayList;
import java.util.List;

public class FlowNetwork {
    List<FlowEdge>[] graph;

    @SuppressWarnings("unchecked")
    public FlowNetwork(int n) {
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    }

    public void addEdge(int from, int to, int cap) {
        FlowEdge a = new FlowEdge(to, graph[to].size(), cap);
        FlowEdge b = new FlowEdge(from, graph[from].size(), 0);
        graph[from].add(a);
        graph[to].add(b);
    }
}
