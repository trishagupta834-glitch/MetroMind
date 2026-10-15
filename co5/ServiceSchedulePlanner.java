package co5;

public class ServiceSchedulePlanner {
    public static int[] solve(String[] services, String[] slots, boolean[][] availability,
                              int[][] conflicts, boolean useCliqueReduction) {
        int[][] clauses = buildClauses(services, slots, availability, conflicts);
        int variableCount = services.length * slots.length;
        boolean[] assignment = useCliqueReduction
                ? SatToClique.findSatisfyingAssignment(variableCount, clauses)
                : ThreeSat.solve(variableCount, clauses);
        if (assignment == null) return null;

        int[] selectedSlots = new int[services.length];
        for (int service = 0; service < services.length; service++) {
            selectedSlots[service] = -1;
            for (int slot = 0; slot < slots.length; slot++) {
                if (assignment[variable(service, slot, slots.length) - 1]) {
                    selectedSlots[service] = slot;
                    break;
                }
            }
            if (selectedSlots[service] < 0) throw new IllegalStateException("Solver returned an incomplete schedule.");
        }
        return selectedSlots;
    }

    public static int[][] buildClauses(String[] services, String[] slots, boolean[][] availability,
                                       int[][] conflicts) {
        if (services == null || slots == null || availability == null || conflicts == null)
            throw new IllegalArgumentException("Schedule inputs cannot be null.");
        if (slots.length == 0 || slots.length > 3 || availability.length != services.length)
            throw new IllegalArgumentException("3-SAT scheduling requires 1-3 shared slots for every service.");

        java.util.List<int[]> clauses = new java.util.ArrayList<>();
        for (int service = 0; service < services.length; service++) {
            if (availability[service] == null || availability[service].length != slots.length)
                throw new IllegalArgumentException("Availability must have one value per service and slot.");
            int availableCount = 0;
            for (boolean available : availability[service]) if (available) availableCount++;
            int[] atLeastOne = new int[availableCount];
            int position = 0;
            for (int slot = 0; slot < slots.length; slot++)
                if (availability[service][slot]) atLeastOne[position++] = variable(service, slot, slots.length);
            clauses.add(atLeastOne);

            for (int firstSlot = 0; firstSlot < slots.length; firstSlot++) {
                if (!availability[service][firstSlot]) continue;
                for (int secondSlot = firstSlot + 1; secondSlot < slots.length; secondSlot++) {
                    if (availability[service][secondSlot])
                        clauses.add(new int[]{-variable(service, firstSlot, slots.length),
                                -variable(service, secondSlot, slots.length)});
                }
            }
        }

        for (int[] conflict : conflicts) {
            if (conflict == null || conflict.length != 2 || conflict[0] < 0 || conflict[0] >= services.length
                    || conflict[1] < 0 || conflict[1] >= services.length || conflict[0] == conflict[1])
                throw new IllegalArgumentException("Each conflict must connect two different valid services.");
            for (int slot = 0; slot < slots.length; slot++) {
                if (availability[conflict[0]][slot] && availability[conflict[1]][slot])
                    clauses.add(new int[]{-variable(conflict[0], slot, slots.length),
                            -variable(conflict[1], slot, slots.length)});
            }
        }
        return clauses.toArray(new int[0][]);
    }

    private static int variable(int service, int slot, int slotCount) {
        return service * slotCount + slot + 1;
    }
}