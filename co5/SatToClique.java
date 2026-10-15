package co5;

import java.util.ArrayList;
import java.util.List;

public class SatToClique {
    private static class LiteralNode {
        final int clauseIndex;
        final int literal;

        LiteralNode(int clauseIndex, int literal) {
            this.clauseIndex = clauseIndex;
            this.literal = literal;
        }
    }

    public static int countCliqueGraphNodes(int[][] clauses) {
        int nodes = 0;
        for (int[] clause : clauses) nodes += clause.length;
        return nodes;
    }

    public static boolean compatible(int a, int b) {
        return a != -b;
    }

    public static boolean[] findSatisfyingAssignment(int vars, int[][] clauses) {
        if (vars < 0) throw new IllegalArgumentException("Variable count cannot be negative.");
        if (clauses == null) throw new IllegalArgumentException("Clauses cannot be null.");
        if (clauses.length == 0) return new boolean[vars];

        List<LiteralNode> nodes = new ArrayList<>();
        for (int clauseIndex = 0; clauseIndex < clauses.length; clauseIndex++) {
            int[] clause = clauses[clauseIndex];
            if (clause == null || clause.length == 0) return null;
            for (int literal : clause) {
                long variable = Math.abs((long) literal);
                if (literal == 0 || variable > vars)
                    throw new IllegalArgumentException("Each literal must reference a declared variable.");
                nodes.add(new LiteralNode(clauseIndex, literal));
            }
        }

        if (nodes.size() > 512)
            throw new IllegalArgumentException("SAT-to-clique input is too large for this interactive demo.");

        boolean[][] adjacent = new boolean[nodes.size()][nodes.size()];
        for (int first = 0; first < nodes.size(); first++) {
            LiteralNode firstNode = nodes.get(first);
            for (int second = first + 1; second < nodes.size(); second++) {
                LiteralNode secondNode = nodes.get(second);
                if (firstNode.clauseIndex != secondNode.clauseIndex
                        && compatible(firstNode.literal, secondNode.literal)) {
                    adjacent[first][second] = true;
                    adjacent[second][first] = true;
                }
            }
        }

        List<Integer> candidates = new ArrayList<>();
        for (int node = 0; node < nodes.size(); node++) candidates.add(node);
        List<Integer> clique = new ArrayList<>();
        List<Integer> solution = findClique(nodes, adjacent, clauses.length, candidates, clique);
        if (solution == null) return null;

        boolean[] assignment = new boolean[vars];
        for (int node : solution) {
            int literal = nodes.get(node).literal;
            if (literal > 0) assignment[literal - 1] = true;
        }
        return assignment;
    }

    private static List<Integer> findClique(List<LiteralNode> nodes, boolean[][] adjacent,
                                             int targetSize, List<Integer> candidates,
                                             List<Integer> clique) {
        if (clique.size() == targetSize) return new ArrayList<>(clique);
        if (clique.size() + candidates.size() < targetSize) return null;

        for (int index = 0; index < candidates.size(); index++) {
            if (clique.size() + candidates.size() - index < targetSize) return null;
            int candidate = candidates.get(index);
            List<Integer> nextCandidates = new ArrayList<>();
            for (int next = index + 1; next < candidates.size(); next++) {
                int other = candidates.get(next);
                if (adjacent[candidate][other]) nextCandidates.add(other);
            }
            clique.add(candidate);
            List<Integer> result = findClique(nodes, adjacent, targetSize, nextCandidates, clique);
            clique.remove(clique.size() - 1);
            if (result != null) return result;
        }
        return null;
    }
}
