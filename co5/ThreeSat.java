package co5;

public class ThreeSat {
    public static boolean satisfiable(int vars, int[][] clauses) {
        return solve(vars, clauses) != null;
    }

    public static boolean[] solve(int vars, int[][] clauses) {
        if (vars < 0) throw new IllegalArgumentException("Variable count cannot be negative.");
        if (clauses == null) throw new IllegalArgumentException("Clauses cannot be null.");
        for (int[] clause : clauses) {
            if (clause == null) throw new IllegalArgumentException("A clause cannot be null.");
            for (int literal : clause) {
                long variable = Math.abs((long) literal);
                if (literal == 0 || variable > vars)
                    throw new IllegalArgumentException("Each literal must reference a declared variable.");
            }
        }

        byte[] assignment = new byte[vars];
        java.util.Arrays.fill(assignment, (byte) -1);
        return solve(clauses, assignment);
    }

    private static boolean[] solve(int[][] clauses, byte[] assignment) {
        if (!propagate(clauses, assignment)) return null;

        int variable = chooseVariable(clauses, assignment);
        if (variable < 0) {
            boolean[] result = new boolean[assignment.length];
            for (int i = 0; i < assignment.length; i++) result[i] = assignment[i] == 1;
            return result;
        }

        for (byte value = 1; value >= 0; value--) {
            byte[] branch = assignment.clone();
            branch[variable] = value;
            boolean[] result = solve(clauses, branch);
            if (result != null) return result;
        }
        return null;
    }

    private static boolean propagate(int[][] clauses, byte[] assignment) {
        boolean changed;
        do {
            changed = false;
            for (int[] clause : clauses) {
                boolean satisfied = false;
                int unassignedCount = 0;
                int lastUnassigned = 0;

                for (int literal : clause) {
                    int variable = Math.abs(literal) - 1;
                    byte value = assignment[variable];
                    if (value < 0) {
                        unassignedCount++;
                        lastUnassigned = literal;
                    } else if ((literal > 0 && value == 1) || (literal < 0 && value == 0)) {
                        satisfied = true;
                        break;
                    }
                }

                if (satisfied) continue;
                if (unassignedCount == 0) return false;
                if (unassignedCount == 1) {
                    int variable = Math.abs(lastUnassigned) - 1;
                    byte required = (byte) (lastUnassigned > 0 ? 1 : 0);
                    if (assignment[variable] >= 0 && assignment[variable] != required) return false;
                    if (assignment[variable] < 0) {
                        assignment[variable] = required;
                        changed = true;
                    }
                }
            }
        } while (changed);
        return true;
    }

    private static int chooseVariable(int[][] clauses, byte[] assignment) {
        int bestVariable = -1;
        int shortestClause = Integer.MAX_VALUE;
        for (int[] clause : clauses) {
            boolean satisfied = false;
            int unassignedCount = 0;
            int firstUnassigned = -1;
            for (int literal : clause) {
                int variable = Math.abs(literal) - 1;
                byte value = assignment[variable];
                if (value < 0) {
                    unassignedCount++;
                    if (firstUnassigned < 0) firstUnassigned = variable;
                } else if ((literal > 0 && value == 1) || (literal < 0 && value == 0)) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied && unassignedCount > 0 && unassignedCount < shortestClause) {
                shortestClause = unassignedCount;
                bestVariable = firstUnassigned;
            }
        }
        return bestVariable;
    }
}
