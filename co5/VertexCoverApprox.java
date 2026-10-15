package co5;

import java.util.ArrayList;
import java.util.List;

public class VertexCoverApprox {
    public static List<Integer> cover(int[][] edges) {
        int vertexCount = 0;
        for (int[] edge : edges) {
            if (edge == null || edge.length != 2) throw new IllegalArgumentException("Each link must have two endpoints.");
            if (edge[0] < 0 || edge[1] < 0) throw new IllegalArgumentException("Station indexes cannot be negative.");
            vertexCount = Math.max(vertexCount, Math.max(edge[0], edge[1]) + 1);
        }
        return cover(vertexCount, edges);
    }

    public static List<Integer> cover(int vertexCount, int[][] edges) {
        if (vertexCount < 0) throw new IllegalArgumentException("Station count cannot be negative.");
        if (edges == null) throw new IllegalArgumentException("Links cannot be null.");
        boolean[] coveredEdges = new boolean[edges.length];
        boolean[] selected = new boolean[vertexCount];
        List<Integer> cover = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == null || edges[i].length != 2) throw new IllegalArgumentException("Each link must have two endpoints.");
            int a = edges[i][0], b = edges[i][1];
            if (a < 0 || a >= vertexCount || b < 0 || b >= vertexCount)
                throw new IllegalArgumentException("Link endpoint is outside the station list.");
            if (coveredEdges[i]) continue;
            addStation(a, selected, cover);
            addStation(b, selected, cover);
            for (int j = i; j < edges.length; j++)
                if (edges[j][0] == a || edges[j][1] == a || edges[j][0] == b || edges[j][1] == b)
                    coveredEdges[j] = true;
        }
        return cover;
    }

    private static void addStation(int station, boolean[] selected, List<Integer> cover) {
        if (!selected[station]) {
            selected[station] = true;
            cover.add(station);
        }
    }
}
