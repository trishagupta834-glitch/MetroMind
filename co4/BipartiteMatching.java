package co4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BipartiteMatching {
    public static int maxMatch(boolean[][] ok) {
        int matches = 0;
        for (int service : serviceForVehicles(ok)) if (service >= 0) matches++;
        return matches;
    }

    public static int[] serviceForVehicles(boolean[][] compatibility) {
        validate(compatibility);
        int serviceCount = compatibility[0].length;
        int[] vehicleForService = new int[serviceCount];
        Arrays.fill(vehicleForService, -1);
        for (int vehicle = 0; vehicle < compatibility.length; vehicle++)
            tryMatch(vehicle, compatibility, vehicleForService, new boolean[serviceCount]);

        int[] serviceForVehicle = new int[compatibility.length];
        Arrays.fill(serviceForVehicle, -1);
        for (int service = 0; service < serviceCount; service++)
            if (vehicleForService[service] >= 0) serviceForVehicle[vehicleForService[service]] = service;
        return serviceForVehicle;
    }

    private static void validate(boolean[][] compatibility) {
        if (compatibility == null || compatibility.length == 0 || compatibility[0] == null
                || compatibility[0].length == 0)
            throw new IllegalArgumentException("At least one vehicle and one service are required.");
        int serviceCount = compatibility[0].length;
        for (boolean[] row : compatibility)
            if (row == null || row.length != serviceCount)
                throw new IllegalArgumentException("Compatibility table must be rectangular.");
    }

    static boolean tryMatch(int vehicle, boolean[][] ok, int[] serviceFor, boolean[] seen) {
        for (int service = 0; service < ok[vehicle].length; service++) {
            if (!ok[vehicle][service] || seen[service]) continue;
            seen[service] = true;
            if (serviceFor[service] == -1 || tryMatch(serviceFor[service], ok, serviceFor, seen)) {
                serviceFor[service] = vehicle;
                return true;
            }
        }
        return false;
    }

    public static List<String> assignments(boolean[][] ok) {
        int[] serviceForVehicle = serviceForVehicles(ok);
        List<String> ans = new ArrayList<>();
        for (int vehicle = 0; vehicle < serviceForVehicle.length; vehicle++)
            if (serviceForVehicle[vehicle] >= 0)
                ans.add("Vehicle " + vehicle + " -> Service " + serviceForVehicle[vehicle]);
        return ans;
    }
}
