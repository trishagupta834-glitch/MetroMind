package co4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BipartiteMatching {
    public static int maxMatch(boolean[][] ok) {
        int[] serviceFor = new int[ok[0].length];
        Arrays.fill(serviceFor, -1);
        int matches = 0;
        for (int vehicle = 0; vehicle < ok.length; vehicle++)
            if (tryMatch(vehicle, ok, serviceFor, new boolean[ok[0].length])) matches++;
        return matches;
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
        int[] serviceFor = new int[ok[0].length];
        Arrays.fill(serviceFor, -1);
        for (int v = 0; v < ok.length; v++) tryMatch(v, ok, serviceFor, new boolean[ok[0].length]);
        List<String> ans = new ArrayList<>();
        for (int s = 0; s < serviceFor.length; s++) if (serviceFor[s] != -1) ans.add("Vehicle " + serviceFor[s] + " -> Service " + s);
        return ans;
    }
}
