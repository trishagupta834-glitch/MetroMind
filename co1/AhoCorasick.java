package co1;

import java.util.*;

public class AhoCorasick {
    static class Node {
        Map<Character, Node> next = new HashMap<>();
        Node fail;
        List<String> out = new ArrayList<>();
    }

    public static Map<String, List<Integer>> scanEmergency(String text, List<String> words) {
        Node root = new Node();
        Map<String, List<Integer>> result = new LinkedHashMap<>();
        for (String word : words) {
            result.put(word, new ArrayList<>());
            Node node = root;
            for (char c : word.toCharArray()) node = node.next.computeIfAbsent(c, key -> new Node());
            node.out.add(word);
        }
        Queue<Node> queue = new ArrayDeque<>();
        root.fail = root;
        for (Node node : root.next.values()) { node.fail = root; queue.add(node); }
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            for (Map.Entry<Character, Node> edge : node.next.entrySet()) {
                Node fail = node.fail;
                while (fail != root && !fail.next.containsKey(edge.getKey())) fail = fail.fail;
                if (fail.next.containsKey(edge.getKey()) && fail.next.get(edge.getKey()) != edge.getValue())
                    fail = fail.next.get(edge.getKey());
                edge.getValue().fail = fail;
                edge.getValue().out.addAll(fail.out);
                queue.add(edge.getValue());
            }
        }
        Node node = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (node != root && !node.next.containsKey(c)) node = node.fail;
            if (node.next.containsKey(c)) node = node.next.get(c);
            for (String word : node.out) result.get(word).add(i - word.length() + 1);
        }
        result.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return result;
    }

    public static void search(String text, String[] keywords) {
        for (Map.Entry<String, List<Integer>> entry : scanEmergency(text, Arrays.asList(keywords)).entrySet())
            for (int index : entry.getValue())
                System.out.println("Aho-Corasick match '" + entry.getKey() + "' at " + index);
    }
}
