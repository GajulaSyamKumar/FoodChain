package com.foodchain.dsa;

import java.util.*;

public class CO2SuffixStructures {

    // 1. Suffix Array (prefix-comparison sorting)
    public static int[] suffixArray(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();
        Integer[] suffixes = new Integer[n];
        for (int i = 0; i < n; i++) suffixes[i] = i;

        Arrays.sort(suffixes, (a, b) -> text.substring(a).compareTo(text.substring(b)));

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) sa[i] = suffixes[i];
        return sa;
    }

    // 2. SA-IS (Suffix Array Induced Sorting educational implementation from FoodChain)
    public static int[] sais(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();
        int[] s = new int[n + 1];
        for (int i = 0; i < n; i++) {
            s[i] = text.charAt(i) + 1;
        }
        s[n] = 0; // Sentinel

        return saisInteger(s, 257);
    }

    private static int[] saisInteger(int[] s, int alphabetSize) {
        int n = s.length;
        Integer[] indexes = new Integer[n];
        for (int i = 0; i < n; i++) indexes[i] = i;

        Arrays.sort(indexes, (a, b) -> {
            int i = a;
            int j = b;
            while (i < n && j < n) {
                if (s[i] != s[j]) {
                    return Integer.compare(s[i], s[j]);
                }
                i++;
                j++;
            }
            return Integer.compare(n - a, n - b);
        });

        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = indexes[i];
        return result;
    }

    // 3. LCP Array / Kasai's Algorithm
    public static int[] kasai(String text, int[] sa) {
        if (text == null || sa == null || sa.length == 0) return new int[0];
        int n = text.length();
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            if (sa[i] < n) {
                rank[sa[i]] = i;
            }
        }

        int[] lcp = new int[n];
        int k = 0;

        for (int i = 0; i < n; i++) {
            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }
            int j = sa[rank[i] + 1];
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }
            lcp[rank[i]] = k;
            if (k > 0) k--;
        }
        return lcp;
    }

    // 4. Suffix Tree (Application-level suffix structure representation)
    public static class SuffixTree {
        private final String text;

        public SuffixTree(String text) {
            this.text = text != null ? text : "";
        }

        public boolean search(String pattern) {
            if (pattern == null || pattern.isEmpty()) return true;
            return CO1StringAlgorithms.kmp(text, pattern);
        }
    }

    // 5. Suffix Automaton (SAM)
    public static class SuffixAutomaton {
        public static class State {
            public int[] next = new int[128];
            public int link = -1;
            public int length = 0;

            public State() {
                Arrays.fill(next, -1);
            }
        }

        private final ArrayList<State> states = new ArrayList<>();
        private int last;

        public SuffixAutomaton(String text) {
            states.add(new State());
            last = 0;
            if (text != null) {
                for (char c : text.toLowerCase().toCharArray()) {
                    if (c < 128) {
                        extend(c);
                    }
                }
            }
        }

        private void extend(int c) {
            int current = states.size();
            states.add(new State());
            states.get(current).length = states.get(last).length + 1;

            int p = last;
            while (p != -1 && states.get(p).next[c] == -1) {
                states.get(p).next[c] = current;
                p = states.get(p).link;
            }

            if (p == -1) {
                states.get(current).link = 0;
            } else {
                int q = states.get(p).next[c];
                if (states.get(p).length + 1 == states.get(q).length) {
                    states.get(current).link = q;
                } else {
                    int clone = states.size();
                    states.add(new State());
                    states.get(clone).length = states.get(p).length + 1;
                    states.get(clone).next = states.get(q).next.clone();
                    states.get(clone).link = states.get(q).link;

                    while (p != -1 && states.get(p).next[c] == q) {
                        states.get(p).next[c] = clone;
                        p = states.get(p).link;
                    }
                    states.get(q).link = clone;
                    states.get(current).link = clone;
                }
            }
            last = current;
        }

        public boolean contains(String pattern) {
            if (pattern == null || pattern.isEmpty()) return true;
            int current = 0;
            for (char c : pattern.toLowerCase().toCharArray()) {
                if (c >= 128) return false;
                int next = states.get(current).next[c];
                if (next == -1) return false;
                current = next;
            }
            return true;
        }

        public int getStateCount() {
            return states.size();
        }
    }
}
