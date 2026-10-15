import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import co1.*;
import co2.*;
import co3.*;
import co4.*;
import co5.*;
import co6.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("METROMIND: INTELLIGENT PUBLIC TRANSPORT PLATFORM");
                System.out.println("[1] FIND STATION, SERVICE, OR TICKET INFORMATION");
                System.out.println("[2] SEARCH TRANSPORT DOCUMENTS AND ROUTES");
                System.out.println("[3] CORRECT NAMES AND PLAN STATION VISITS");
                System.out.println("[4] PLAN PASSENGER CAPACITY AND VEHICLE ASSIGNMENTS");
                System.out.println("[5] CHECK SCHEDULE FEASIBILITY AND CONFLICTS");
                System.out.println("[6] ANALYZE PASSENGER AND SERVICE DATA");
                System.out.println("[0] EXIT METROMIND");
                System.out.print("Enter module: ");
                String choice = input.nextLine().trim();
                if (choice.equals("0")) break;
                if (choice.equals("1")) stringMenu(input);
                else if (choice.equals("2")) suffixMenu(input);
                else if (choice.equals("3")) dynamicProgrammingMenu(input);
                else if (choice.equals("4")) networkFlowMenu(input);
                else if (choice.equals("5")) npMenu(input);
                else if (choice.equals("6")) passengerDataMenu(input);
                else System.out.println("Choose 1, 2, 3, 4, 5, 6, or 0.");
            }
        }
    }

    static void stringMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M1 STRING ALGORITHMS ---");
            System.out.println("1.1 Find routes serving a station");
            System.out.println("1.2 Find a ticket ID or route code in MetroMind records");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 1.1, 1.2, or 0.");
        }
    }

    static void suffixMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M2 SUFFIX STRUCTURES ---");
            System.out.println("2.1 Search the complaint archive by keyword");
            System.out.println("2.2 Find the longest text shared by two route descriptions");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 2.1, 2.2, or 0.");
        }
    }

    static void dynamicProgrammingMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M3 ADVANCED DYNAMIC PROGRAMMING ---");
            System.out.println("3.1 Find the closest station name to a misspelling");
            System.out.println("3.2 Plan a short tour through selected stations");
            System.out.println("3.3 Build a quick lookup order for frequently searched stations");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 3.1, 3.2, 3.3, or 0.");
        }
    }

    static void networkFlowMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M4 NETWORK FLOW ---");
            System.out.println("4.1 Calculate passenger capacity between two stations");
            System.out.println("4.2 Assign available vehicles to compatible services");
            System.out.println("4.3 Identify transport links limiting passenger flow");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 4.1, 4.2, 4.3, or 0.");
        }
    }

    static void npMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M5 NP-COMPLETENESS AND APPROXIMATION ---");
            System.out.println("5.1 Check whether service-scheduling rules can be satisfied");
            System.out.println("5.2 Analyze compatible choices in a small schedule");
            System.out.println("5.3 Choose services to reschedule to resolve conflicts");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 5.1, 5.2, 5.3, or 0.");
        }
    }

    static void passengerDataMenu(Scanner input) {
        while (true) {
            System.out.println("\n--- M6 RANDOMIZED AND PARALLEL ALGORITHMS ---");
            System.out.println("6.1 Sort services by passenger count, arrival time, or delay");
            System.out.println("6.2 Keep a representative sample of passenger events");
            System.out.println("6.3 Show running passenger totals by time interval");
            System.out.println("0. Back to Main Menu");
            String choice = input.nextLine().trim();
            if (choice.equals("0")) return;
            if (!service(choice, input)) System.out.println("Choose 6.1, 6.2, 6.3, or 0.");
        }
    }

    static boolean service(String choice, Scanner input) {
        switch (choice) {
            case "1.1": kmpLogSearch(input); break;
            case "1.2": ticketAudit(input); break;
            case "2.1": manualSearch(input); break;
            case "2.2": routeOverlap(input); break;
            case "3.1": typoCorrection(input); break;
            case "3.2": touristPass(input); break;
            case "3.3": quickStation(); break;
            case "4.1": dinicDemo(input); break;
            case "4.2": bipartiteMatchingDemo(input); break;
            case "4.3": minCutDemo(input); break;
            case "5.1": threeSatDemo(input); break;
            case "5.2": satToCliqueDemo(input); break;
            case "5.3": vertexCoverApproxDemo(input); break;
            case "6.1": rankServices(input); break;
            case "6.2": samplePassengerEvents(input); break;
            case "6.3": cumulativePassengerCounts(input); break;
            default: return false;
        }
        return true;
    }

    static void typoCorrection(Scanner input) {
        System.out.print("Typed station name: ");
        String typed = input.nextLine().trim();
        String actual = closestStation(typed);
        if (actual == null) {
            System.out.println("No station data found.");
            return;
        }
        int distance = Levenshtein.distance(typed.toUpperCase(Locale.ROOT), actual.toUpperCase(Locale.ROOT));
        System.out.println("Did you mean: " + actual + "?");
        System.out.println("Correction distance: " + distance);
    }

    static String closestStation(String typed) {
        try {
            String closest = null;
            int best = Integer.MAX_VALUE;
            List<String> lines = Files.readAllLines(Path.of("metro_details.csv"));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String station = line.split(",", 2)[0].trim();
                int distance = Levenshtein.distance(typed.toUpperCase(Locale.ROOT), station.toUpperCase(Locale.ROOT));
                if (distance < best) { best = distance; closest = station; }
            }
            return closest;
        } catch (Exception e) {
            return null;
        }
    }

    static void quickStation() {
        try {
            List<String> lines = Files.readAllLines(Path.of("metro_details.csv"));
            java.util.Map<String, Double> frequencyByStation = new java.util.TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            for (int row = 1; row < lines.size(); row++) {
                String[] columns = lines.get(row).split(",", -1);
                if (columns.length < 5) continue;
                String station = columns[0].trim();
                double frequency = Double.parseDouble(columns[4].trim());
                frequencyByStation.merge(station, frequency, Double::sum);
            }
            String[] stations = frequencyByStation.keySet().toArray(new String[0]);
            double[] frequencies = new double[stations.length];
            for (int index = 0; index < stations.length; index++) {
                frequencies[index] = frequencyByStation.get(stations[index]);
                System.out.println(stations[index] + " - Search frequency: " + frequencies[index]);
            }
            System.out.println("Optimal lookup tree (stations sorted by name):");
            OptimalBST.printTree(OptimalBST.buildOBST(stations, frequencies), "");
        } catch (Exception e) {
            System.out.println("Could not load station frequencies.");
        }
    }

    static void touristPass(Scanner input) {
        int n = readIntBetween(input, "Number of selected stations (2-15): ", 2, 15);
        String[] stations = new String[n];
        for (int station = 0; station < n; station++)
            stations[station] = readNonEmptyName(input, "Destination " + (station + 1) + " name: ");
        int[][] distances = new int[n][n];
        System.out.println("Enter each pair's travel time once, in minutes.");
        for (int first = 0; first < n; first++) {
            for (int second = first + 1; second < n; second++) {
                int distance = readIntBetween(input,
                        stations[first] + " to " + stations[second] + ": ", 0, Integer.MAX_VALUE);
                distances[first][second] = distance;
                distances[second][first] = distance;
            }
        }
        System.out.println(BitmaskDP.shortestTour(distances, stations));
    }

    static void kmpLogSearch(Scanner input) {
        String query = readNonEmptyName(input, "Station name (example: North): ").toLowerCase(Locale.ROOT);
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of("metro_details.csv"));
        } catch (Exception e) {
            System.out.println("Could not read metro_details.csv.");
            return;
        }

        Set<String> matchingRoutes = new LinkedHashSet<>();
        for (int row = 1; row < lines.size(); row++) {
            String[] columns = lines.get(row).split(",", -1);
            if (columns.length < 5) continue;
            String station = columns[0].trim();
            String route = columns[1].trim();
            if (!KMP.kmpSearch(station.toLowerCase(Locale.ROOT), query).isEmpty()) matchingRoutes.add(route);
        }

        if (matchingRoutes.isEmpty()) {
            System.out.println("No routes found for station search '" + query + "'.");
            return;
        }
        System.out.println("Routes serving stations matching '" + query + "':");
        for (String route : matchingRoutes) System.out.println("  " + route);
        System.out.println("Unique routes found: " + matchingRoutes.size());
    }

    static void ticketAudit(Scanner input) {
        String code = readNonEmptyName(input, "Ticket ID or service code to find (example: T999): ")
                .toLowerCase(Locale.ROOT);
        try {
            List<String> lines = Files.readAllLines(Path.of("metro_details.csv"));
            int matches = 0;
            for (int row = 1; row < lines.size(); row++) {
                String[] columns = lines.get(row).split(",", -1);
                if (columns.length < 5) continue;
                String ticket = columns[3].trim();
                String route = columns[1].trim();
                boolean exactTicket = ticket.length() == code.length()
                        && RabinKarp.rabinKarp(ticket.toLowerCase(Locale.ROOT), code) == 0;
                boolean exactRoute = route.length() == code.length()
                        && RabinKarp.rabinKarp(route.toLowerCase(Locale.ROOT), code) == 0;
                if (exactTicket || exactRoute) {
                    System.out.println(columns[0] + " | " + route + " | ticket " + ticket
                            + " | status " + columns[2]);
                    matches++;
                }
            }
            System.out.println(matches == 0 ? "No exact ticket/service code found." : "Matching records: " + matches);
        } catch (Exception e) {
            System.out.println("Could not read metro_details.csv.");
        }
    }

    static void routeOverlap(Scanner input) {
        List<String> first = readRouteStations(input, "First route station sequence: ");
        List<String> second = readRouteStations(input, "Second route station sequence: ");
        LCP.RouteMatch match = LCP.longestCommonRouteSegment(first, second);
        if (match.stations.isEmpty()) {
            System.out.println("These routes share no consecutive stations.");
            return;
        }
        System.out.println("Longest shared station segment: " + String.join(" -> ", match.stations));
        System.out.println("Shared stations: " + match.stations.size());
        System.out.println("Starts at station " + (match.firstStart + 1) + " in route 1 and station "
                + (match.secondStart + 1) + " in route 2.");
    }

    static List<String> readRouteStations(Scanner input, String prompt) {
        String route = readNonEmptyName(input, prompt + "(separate stations with ->, >, or commas): ");
        List<String> stations = new ArrayList<>();
        for (String station : route.split("\\s*(?:->|>|,)\\s*")) {
            String name = station.trim();
            if (!name.isEmpty()) stations.add(name);
        }
        if (stations.isEmpty()) throw new IllegalArgumentException("Enter at least one station name.");
        return stations;
    }

    static void manualSearch(Scanner input) {
        try {
            ComplaintArchive archive = ComplaintArchive.load(Path.of("complaints.csv"));
            System.out.println("Complaint archive indexed: " + archive.size() + " records.");
            System.out.print("Complaint keyword (0 to return): ");
            String keyword = input.nextLine().trim();
            if (keyword.equals("0")) return;
            if (keyword.isEmpty()) {
                System.out.println("Enter a non-empty keyword.");
                return;
            }

            List<ComplaintArchive.Complaint> matches = archive.search(keyword);
            if (matches.isEmpty()) {
                System.out.println("No complaints matched '" + keyword + "'.");
                return;
            }
            System.out.println("Matching complaints:");
            for (ComplaintArchive.Complaint complaint : matches) {
                System.out.println(complaint.id + " | " + complaint.station + " | " + complaint.route
                        + " | " + complaint.status + " | " + complaint.description);
            }
            System.out.println("Complaints found: " + matches.size());
        } catch (Exception e) {
            System.out.println("Could not load complaint archive: " + e.getMessage());
        }
    }

    static class CapacityLink {
        final int from;
        final int to;
        final int capacity;

        CapacityLink(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }
    }

    static class NetworkInput {
        final String[] stations;
        final FlowNetwork network;
        final List<CapacityLink> links;

        NetworkInput(String[] stations, FlowNetwork network, List<CapacityLink> links) {
            this.stations = stations;
            this.network = network;
            this.links = links;
        }
    }

    static NetworkInput readTransportNetwork(Scanner input) {
        int stationCount = readIntBetween(input, "Number of stations/interchanges (2-30): ", 2, 30);
        String[] stations = new String[stationCount];
        Set<String> usedNames = new LinkedHashSet<>();
        for (int station = 0; station < stationCount; station++)
            stations[station] = readUniqueName(input, "Station/interchange " + (station + 1) + " name: ", usedNames);

        int maxLinks = stationCount * (stationCount - 1);
        int linkCount = readIntBetween(input, "Number of directed transport links: ", 1, maxLinks);
        FlowNetwork network = new FlowNetwork(stationCount);
        List<CapacityLink> links = new ArrayList<>();
        System.out.println("Use station numbers from 1 to " + stationCount + ". Capacity means passengers per hour.");
        for (int link = 0; link < linkCount; link++) {
            int from = readIntBetween(input, "Link " + (link + 1) + " from station number: ", 1, stationCount) - 1;
            int to = readIntBetween(input, "Link " + (link + 1) + " to station number: ", 1, stationCount) - 1;
            if (from == to) throw new IllegalArgumentException("A transport link must connect different stations.");
            int capacity = readIntBetween(input, "Passenger capacity per hour (1-1000000): ", 1, 1000000);
            network.addEdge(from, to, capacity);
            links.add(new CapacityLink(from, to, capacity));
        }
        return new NetworkInput(stations, network, links);
    }

    static int[] readFlowEndpoints(Scanner input, String[] stations) {
        System.out.println("Stations: " + numberedNames(stations));
        int source = readIntBetween(input, "Passenger-flow source station number: ", 1, stations.length) - 1;
        int sink = readIntBetween(input, "Passenger-flow destination station number: ", 1, stations.length) - 1;
        if (source == sink) throw new IllegalArgumentException("Source and destination must differ.");
        return new int[]{source, sink};
    }

    static void dinicDemo(Scanner input) {
        NetworkInput inputNetwork = readTransportNetwork(input);
        int[] endpoints = readFlowEndpoints(input, inputNetwork.stations);
        int maximumFlow = Dinic.maxFlow(inputNetwork.network, endpoints[0], endpoints[1]);
        System.out.println("Maximum passenger flow from " + inputNetwork.stations[endpoints[0]] + " to "
                + inputNetwork.stations[endpoints[1]] + ": " + maximumFlow + " passengers/hour");
    }

    static void bipartiteMatchingDemo(Scanner input) {
        int vehicleCount = readIntBetween(input, "Number of available vehicles: ", 1, 100);
        int serviceCount = readIntBetween(input, "Number of transport services: ", 1, 100);
        String[] vehicles = readNames(input, vehicleCount, "Vehicle");
        String[] services = readNames(input, serviceCount, "Service/route");
        boolean[][] compatible = new boolean[vehicleCount][serviceCount];
        for (int vehicle = 0; vehicle < vehicleCount; vehicle++) {
            System.out.println("Compatible services for " + vehicles[vehicle] + ": " + numberedNames(services));
            int[] indexes = readIndexList(input, "Enter service numbers it can operate (blank for none): ", serviceCount);
            for (int service : indexes) compatible[vehicle][service] = true;
        }

        int[] assignment = BipartiteMatching.serviceForVehicles(compatible);
        int matched = 0;
        for (int service : assignment) if (service >= 0) matched++;
        System.out.println("Vehicles assigned: " + matched + " of " + Math.min(vehicleCount, serviceCount));
        for (int vehicle = 0; vehicle < assignment.length; vehicle++) {
            if (assignment[vehicle] >= 0)
                System.out.println(vehicles[vehicle] + " -> " + services[assignment[vehicle]]);
            else System.out.println(vehicles[vehicle] + " -> not assigned");
        }
    }

    static void minCutDemo(Scanner input) {
        NetworkInput inputNetwork = readTransportNetwork(input);
        int[] endpoints = readFlowEndpoints(input, inputNetwork.stations);
        int maximumFlow = Dinic.maxFlow(inputNetwork.network, endpoints[0], endpoints[1]);
        boolean[] sourceSide = MinCut.reachableAfterMaxFlow(inputNetwork.network, endpoints[0]);
        long cutCapacity = 0;
        System.out.println("Minimum-cut connections separating the source from the destination:");
        for (CapacityLink link : inputNetwork.links) {
            if (sourceSide[link.from] && !sourceSide[link.to]) {
                System.out.println(inputNetwork.stations[link.from] + " -> " + inputNetwork.stations[link.to]
                        + " (" + link.capacity + " passengers/hour)");
                cutCapacity += link.capacity;
            }
        }
        System.out.println("Maximum flow: " + maximumFlow + " passengers/hour");
        System.out.println("Cut capacity: " + cutCapacity + " passengers/hour");
        System.out.println("These links form a transport bottleneck between the selected stations.");
    }

    static String[] readNames(Scanner input, int count, String label) {
        String[] names = new String[count];
        Set<String> usedNames = new LinkedHashSet<>();
        for (int index = 0; index < count; index++)
            names[index] = readUniqueName(input, label + " " + (index + 1) + " name: ", usedNames);
        return names;
    }

    static void threeSatDemo(Scanner input) {
        System.out.println("Create a small timetable. Services sharing a vehicle, crew, or track must not use the same slot.");
        runSchedulePlanner(input, false);
    }

    static void satToCliqueDemo(Scanner input) {
        System.out.println("This reduction is exponential; use at most 6 services and 3 time slots.");
        runSchedulePlanner(input, true);
    }

    static void runSchedulePlanner(Scanner input, boolean useCliqueReduction) {
        int serviceLimit = useCliqueReduction ? 6 : 20;
        int slotLimit = 3;
        int serviceCount = readIntBetween(input, "Number of services (1-" + serviceLimit + "): ", 1, serviceLimit);
        int slotCount = readIntBetween(input, "Number of shared time slots (1-" + slotLimit + "): ", 1, slotLimit);

        String[] slots = new String[slotCount];
        Set<String> usedNames = new LinkedHashSet<>();
        for (int slot = 0; slot < slotCount; slot++) {
            slots[slot] = readUniqueName(input, "Time slot " + (slot + 1) + " (for example 08:00-09:00): ", usedNames);
        }

        String[] services = new String[serviceCount];
        boolean[][] availability = new boolean[serviceCount][slotCount];
        usedNames.clear();
        for (int service = 0; service < serviceCount; service++) {
            services[service] = readUniqueName(input, "Service/route " + (service + 1) + " name: ", usedNames);
            System.out.println("Available slots for " + services[service] + ": enter slot numbers separated by commas.");
            System.out.println("Slots: " + numberedNames(slots));
            int[] availableSlots = readIndexList(input, "Available slot numbers (blank means none): ", slotCount);
            for (int slot : availableSlots) availability[service][slot] = true;
        }

        int maximumConflicts = serviceCount * (serviceCount - 1) / 2;
        int conflictCount = readIntBetween(input, "Number of service pairs that cannot run together: ", 0, maximumConflicts);
        int[][] conflicts = new int[conflictCount][2];
        for (int conflict = 0; conflict < conflictCount; conflict++) {
            System.out.println("Services: " + numberedNames(services));
            System.out.print("Conflict pair " + (conflict + 1) + " (two service numbers): ");
            int[] pair = readNumbers(input.nextLine(), 2);
            if (pair[0] < 1 || pair[0] > serviceCount || pair[1] < 1 || pair[1] > serviceCount || pair[0] == pair[1])
                throw new IllegalArgumentException("Enter two different valid service numbers.");
            conflicts[conflict][0] = pair[0] - 1;
            conflicts[conflict][1] = pair[1] - 1;
        }

        int[] selectedSlots = ServiceSchedulePlanner.solve(services, slots, availability, conflicts, useCliqueReduction);
        if (selectedSlots == null) {
            System.out.println("No feasible timetable: the availability and conflict rules cannot all be satisfied.");
            return;
        }
        System.out.println("Feasible timetable:");
        for (int service = 0; service < services.length; service++)
            System.out.println("  " + services[service] + " -> " + slots[selectedSlots[service]]);
        if (useCliqueReduction) {
            int[][] clauses = ServiceSchedulePlanner.buildClauses(services, slots, availability, conflicts);
            System.out.println("SAT-to-clique graph: " + SatToClique.countCliqueGraphNodes(clauses)
                    + " literal nodes; found a clique with " + clauses.length + " choices.");
        }
    }

    static void vertexCoverApproxDemo(Scanner input) {
        int serviceCount = readIntBetween(input, "Number of services in the conflict map: ", 1, 200);
        String[] services = readNames(input, serviceCount, "Service/route");
        int maximumConflicts = serviceCount * (serviceCount - 1) / 2;
        int conflictCount = readIntBetween(input, "Number of conflicting service pairs: ", 0, maximumConflicts);
        int[][] conflicts = readConflictPairs(input, services, conflictCount);
        List<Integer> cover = VertexCoverApprox.cover(serviceCount, conflicts);
        System.out.println("Approximate services to reschedule to cover every conflict:");
        for (int service : cover) System.out.println("  " + services[service]);
        System.out.println("Conflicts covered: " + conflicts.length
                + ". This 2-approximation is fast but may not return the smallest set.");
    }

    static int[][] readConflictPairs(Scanner input, String[] services, int conflictCount) {
        int[][] conflicts = new int[conflictCount][2];
        for (int conflict = 0; conflict < conflictCount; conflict++) {
            System.out.println("Services: " + numberedNames(services));
            System.out.print("Conflict pair " + (conflict + 1) + " (two service numbers): ");
            int[] pair = readNumbers(input.nextLine(), 2);
            if (pair[0] < 1 || pair[0] > services.length || pair[1] < 1 || pair[1] > services.length
                    || pair[0] == pair[1])
                throw new IllegalArgumentException("Enter two different valid service numbers.");
            conflicts[conflict][0] = pair[0] - 1;
            conflicts[conflict][1] = pair[1] - 1;
        }
        return conflicts;
    }

    static String numberedNames(String[] names) {
        List<String> numbered = new ArrayList<>();
        for (int index = 0; index < names.length; index++) numbered.add((index + 1) + ". " + names[index]);
        return String.join(" | ", numbered);
    }

    static String readUniqueName(Scanner input, String prompt, Set<String> usedNames) {
        String name = readNonEmptyName(input, prompt);
        if (!usedNames.add(name.toLowerCase())) throw new IllegalArgumentException("Names must be unique.");
        return name;
    }

    static String readNonEmptyName(Scanner input, String prompt) {
        System.out.print(prompt);
        String name = input.nextLine().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be blank.");
        return name;
    }

    static int[] readIndexList(Scanner input, String prompt, int maximum) {
        System.out.print(prompt);
        String line = input.nextLine().trim();
        if (line.isEmpty()) return new int[0];
        String[] values = line.split("[ ,]+");
        int[] indexes = new int[values.length];
        boolean[] seen = new boolean[maximum];
        for (int index = 0; index < values.length; index++) {
            int value = Integer.parseInt(values[index]);
            if (value < 1 || value > maximum || seen[value - 1])
                throw new IllegalArgumentException("Enter unique slot numbers from 1 to " + maximum + ".");
            seen[value - 1] = true;
            indexes[index] = value - 1;
        }
        return indexes;
    }

    static int readIntBetween(Scanner input, String prompt, int minimum, int maximum) {
        int value = readInt(input, prompt);
        if (value < minimum || value > maximum)
            throw new IllegalArgumentException("Enter a value from " + minimum + " to " + maximum + ".");
        return value;
    }

    static void rankServices(Scanner input) {
        System.out.println("Sort metric: [1] Passenger count [2] Arrival time [3] Delay minutes");
        int metricChoice = readIntBetween(input, "Choose metric: ", 1, 3);
        PassengerDemandRanking.Metric metric = switch (metricChoice) {
            case 1 -> PassengerDemandRanking.Metric.PASSENGERS;
            case 2 -> PassengerDemandRanking.Metric.ARRIVAL_TIME;
            default -> PassengerDemandRanking.Metric.DELAY_MINUTES;
        };
        int count = readIntBetween(input, "Number of services to sort: ", 1, 10000);
        PassengerDemandRanking.ServiceRecord[] records = new PassengerDemandRanking.ServiceRecord[count];
        for (int index = 0; index < count; index++) {
            String name = readNonEmptyName(input, "Service/route name: ");
            if (metric == PassengerDemandRanking.Metric.PASSENGERS) {
                long passengers = readNonNegativeLong(input, "Passenger count: ");
                records[index] = new PassengerDemandRanking.ServiceRecord(name, passengers, passengers,
                        passengers + " passengers");
            } else if (metric == PassengerDemandRanking.Metric.ARRIVAL_TIME) {
                String arrivalTime = readNonEmptyName(input, "Scheduled arrival time (HH:mm): ");
                int minuteOfDay = parseClockMinutes(arrivalTime);
                records[index] = new PassengerDemandRanking.ServiceRecord(name, minuteOfDay, 0, arrivalTime);
            } else {
                long delayMinutes = readNonNegativeLong(input, "Delay in minutes: ");
                records[index] = new PassengerDemandRanking.ServiceRecord(name, delayMinutes, 0,
                        delayMinutes + " minutes late");
            }
        }
        PassengerDemandRanking.sort(records, metric);
        String order = metric == PassengerDemandRanking.Metric.ARRIVAL_TIME ? "earliest first" : "largest first";
        System.out.println("Service records sorted (" + order + "):");
        for (int index = 0; index < records.length; index++)
            System.out.println((index + 1) + ". " + records[index].service + " - " + records[index].displayValue);
    }

    static int parseClockMinutes(String time) {
        String[] parts = time.split(":", -1);
        if (parts.length != 2) throw new IllegalArgumentException("Enter arrival time as HH:mm.");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59)
            throw new IllegalArgumentException("Arrival time must be between 00:00 and 23:59.");
        return hours * 60 + minutes;
    }

    static void samplePassengerEvents(Scanner input) {
        int capacity = readIntBetween(input, "Maximum events to retain for a dashboard sample: ", 0, 100000);
        LivePassengerSampler sampler = new LivePassengerSampler(capacity);
        System.out.println("Paste passenger/ticket events as they arrive, one per line; type END when finished.");
        while (true) {
            System.out.print("Passenger event: ");
            String event = input.nextLine().trim();
            if (event.equalsIgnoreCase("END")) break;
            if (event.isEmpty()) continue;
            sampler.accept(event);
        }
        System.out.println("Events processed: " + sampler.eventsSeen());
        System.out.println("Representative sample for inspection: " + sampler.sample());
    }

    static void cumulativePassengerCounts(Scanner input) {
        int count = readIntBetween(input, "Number of time intervals: ", 1, 100000);
        String[] intervals = new String[count];
        long[] passengers = new long[count];
        for (int index = 0; index < count; index++) {
            intervals[index] = readNonEmptyName(input, "Interval " + (index + 1) + " (for example 08:00-09:00): ");
            passengers[index] = readNonNegativeLong(input, "Passengers in " + intervals[index] + ": ");
        }
        long[] cumulative = PassengerCountScan.inclusiveScan(passengers);
        System.out.println("Running station/route total for the reporting window:");
        for (int index = 0; index < count; index++)
            System.out.println(intervals[index] + " -> " + cumulative[index] + " passengers so far");
    }

    static int readInt(Scanner input, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(input.nextLine().trim());
    }

    static long readLong(Scanner input, String prompt) {
        System.out.print(prompt);
        return Long.parseLong(input.nextLine().trim());
    }

    static long readNonNegativeLong(Scanner input, String prompt) {
        long value = readLong(input, prompt);
        if (value < 0) throw new IllegalArgumentException("Value cannot be negative.");
        return value;
    }

    static int[] readNumbers(String line, int expected) {
        String[] values = line.trim().split("[ ,]+");
        if (expected > 0 && values.length != expected)
            throw new IllegalArgumentException("Expected " + expected + " numbers.");
        int[] numbers = new int[values.length];
        for (int i = 0; i < values.length; i++) numbers[i] = Integer.parseInt(values[i]);
        return numbers;
    }
}
