package co2;

import java.util.*;

public class SuffixAutomaton {
    static class State {
        int link = -1, length;
        Map<Character, Integer> next = new HashMap<>();
    }

    static ArrayList<State> states = new ArrayList<>();
    static int last;

    public static void build(String text) {
        states.clear();
        states.add(new State());
        last = 0;
        for (char c : text.toCharArray()) extend(c);
    }

    static void extend(char c) {
        int current = states.size();
        states.add(new State());
        states.get(current).length = states.get(last).length + 1;
        int p = last;
        while (p != -1 && !states.get(p).next.containsKey(c)) {
            states.get(p).next.put(c, current);
            p = states.get(p).link;
        }
        if (p == -1) states.get(current).link = 0;
        else {
            int q = states.get(p).next.get(c);
            if (states.get(p).length + 1 == states.get(q).length) states.get(current).link = q;
            else {
                int clone = states.size();
                states.add(new State());
                states.get(clone).length = states.get(p).length + 1;
                states.get(clone).next.putAll(states.get(q).next);
                states.get(clone).link = states.get(q).link;
                while (p != -1 && states.get(p).next.getOrDefault(c, -1) == q) {
                    states.get(p).next.put(c, clone);
                    p = states.get(p).link;
                }
                states.get(q).link = states.get(current).link = clone;
            }
        }
        last = current;
    }

    public static boolean contains(String pattern) {
        int state = 0;
        for (char c : pattern.toCharArray()) {
            if (!states.get(state).next.containsKey(c)) return false;
            state = states.get(state).next.get(c);
        }
        return true;
    }

    public static boolean checkPathAndSuggest(String subPath) {
        return contains(subPath);
    }

    public static void main(String[] args) {
        build("banana");
        System.out.println(contains("nan"));
    }
}
