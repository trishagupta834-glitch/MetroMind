package co2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ComplaintArchive {
    private static final char RECORD_SEPARATOR = '\u0001';

    public static final class Complaint {
        public final String id;
        public final String station;
        public final String route;
        public final String status;
        public final String description;

        private Complaint(String id, String station, String route, String status, String description) {
            this.id = id;
            this.station = station;
            this.route = route;
            this.status = status;
            this.description = description;
        }
    }

    private final List<Complaint> complaints;
    private final String searchableText;
    private final int[] suffixArray;
    private final int[] descriptionStarts;
    private final int[] descriptionEnds;

    private ComplaintArchive(List<Complaint> complaints, String searchableText,
                             int[] descriptionStarts, int[] descriptionEnds) {
        this.complaints = complaints;
        this.searchableText = searchableText;
        this.suffixArray = SuffixArray.build(searchableText);
        this.descriptionStarts = descriptionStarts;
        this.descriptionEnds = descriptionEnds;
    }

    public static ComplaintArchive load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty()) throw new IllegalArgumentException("Complaint CSV is empty.");

        List<Complaint> complaints = new ArrayList<>();
        StringBuilder searchableText = new StringBuilder();
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();

        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isBlank()) continue;
            List<String> columns = parseCsvLine(line);
            if (lineNumber == 0 && !columns.isEmpty()
                    && columns.get(0).trim().equalsIgnoreCase("ComplaintID")) continue;
            if (columns.size() != 5)
                throw new IllegalArgumentException("Expected 5 columns on CSV line " + (lineNumber + 1) + ".");

            String id = columns.get(0).trim();
            String station = columns.get(1).trim();
            String route = columns.get(2).trim();
            String status = columns.get(3).trim();
            String description = columns.get(4).trim();
            if (id.isEmpty() || description.isEmpty())
                throw new IllegalArgumentException("Complaint ID and description are required on CSV line "
                        + (lineNumber + 1) + ".");

            complaints.add(new Complaint(id, station, route, status, description));
            starts.add(searchableText.length());
            searchableText.append(description.toLowerCase(Locale.ROOT));
            ends.add(searchableText.length());
            searchableText.append(RECORD_SEPARATOR);
        }

        int[] descriptionStarts = new int[starts.size()];
        int[] descriptionEnds = new int[ends.size()];
        for (int index = 0; index < starts.size(); index++) {
            descriptionStarts[index] = starts.get(index);
            descriptionEnds[index] = ends.get(index);
        }
        return new ComplaintArchive(complaints, searchableText.toString(), descriptionStarts, descriptionEnds);
    }

    public int size() {
        return complaints.size();
    }

    public List<Complaint> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        int[] positions = SuffixArray.search(searchableText, suffixArray, normalizedKeyword);
        boolean[] matched = new boolean[complaints.size()];

        for (int position : positions) {
            int complaintIndex = complaintAt(position, normalizedKeyword.length());
            if (complaintIndex >= 0) matched[complaintIndex] = true;
        }

        List<Complaint> results = new ArrayList<>();
        for (int index = 0; index < matched.length; index++)
            if (matched[index]) results.add(complaints.get(index));
        return results;
    }

    private int complaintAt(int position, int matchLength) {
        int low = 0;
        int high = descriptionStarts.length;
        while (low < high) {
            int middle = (low + high) >>> 1;
            if (position < descriptionStarts[middle]) high = middle;
            else if (position >= descriptionEnds[middle]) low = middle + 1;
            else return position + matchLength <= descriptionEnds[middle] ? middle : -1;
        }
        return -1;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    value.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                columns.add(value.toString());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted field in complaint CSV.");
        columns.add(value.toString());
        return columns;
    }
}
