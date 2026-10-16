package com.foodchain.dsa;

import java.util.*;

public class CO1StringAlgorithms {

    // 1. KMP (Knuth-Morris-Pratt)
    public static int[] buildLPS(String pattern) {
        if (pattern == null || pattern.isEmpty()) return new int[0];
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (Character.toLowerCase(pattern.charAt(i)) == Character.toLowerCase(pattern.charAt(length))) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static boolean kmp(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;
        if (text == null || text.length() < pattern.length()) return false;

        int[] lps = buildLPS(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            char textChar = Character.toLowerCase(text.charAt(i));
            char patternChar = Character.toLowerCase(pattern.charAt(j));

            if (textChar == patternChar) {
                i++;
                j++;
                if (j == pattern.length()) {
                    return true;
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    public static List<Integer> kmpFindAll(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || text == null || text.length() < pattern.length()) {
            return indices;
        }

        int[] lps = buildLPS(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (Character.toLowerCase(text.charAt(i)) == Character.toLowerCase(pattern.charAt(j))) {
                i++;
                j++;
                if (j == pattern.length()) {
                    indices.add(i - j);
                    j = lps[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return indices;
    }

    // 2. Z-FUNCTION
    public static int[] zFunction(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        int n = text.length();
        int[] z = new int[n];
        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {
            if (i <= right) {
                z[i] = Math.min(right - i + 1, z[i - left]);
            }
            while (i + z[i] < n && Character.toLowerCase(text.charAt(z[i])) == Character.toLowerCase(text.charAt(i + z[i]))) {
                z[i]++;
            }
            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }
        return z;
    }

    // 3. RABIN-KARP
    public static boolean rabinKarp(String text, String pattern) {
        List<Integer> matches = rabinKarpFindAll(text, pattern);
        return !matches.isEmpty();
    }

    public static List<Integer> rabinKarpFindAll(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) return matches;
        if (text == null || text.length() < pattern.length()) return matches;

        int n = text.length();
        int m = pattern.length();
        final int BASE = 256;
        final int PRIME = 101;

        int patternHash = 0;
        int textHash = 0;
        int highestPower = 1;

        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + Character.toLowerCase(pattern.charAt(i))) % PRIME;
            textHash = (BASE * textHash + Character.toLowerCase(text.charAt(i))) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                if (text.regionMatches(true, i, pattern, 0, m)) {
                    matches.add(i);
                }
            }
            if (i < n - m) {
                textHash = (BASE * (textHash - Character.toLowerCase(text.charAt(i)) * highestPower)
                        + Character.toLowerCase(text.charAt(i + m))) % PRIME;
                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }
        return matches;
    }

    // 4. AHO-CORASICK
    public static class AhoCorasick {
        public static class Node {
            public int[] next = new int[128];
            public int fail = 0;
            public ArrayList<String> output = new ArrayList<>();

            public Node() {
                Arrays.fill(next, -1);
            }
        }

        private final ArrayList<Node> trie = new ArrayList<>();

        public AhoCorasick(String[] patterns) {
            trie.add(new Node());
            for (String pattern : patterns) {
                if (pattern != null && !pattern.isBlank()) {
                    insert(pattern.trim());
                }
            }
            buildFailureLinks();
        }

        public void insert(String word) {
            int current = 0;
            for (char c : word.toLowerCase().toCharArray()) {
                int index = c;
                if (index >= 128) continue;
                if (trie.get(current).next[index] == -1) {
                    trie.get(current).next[index] = trie.size();
                    trie.add(new Node());
                }
                current = trie.get(current).next[index];
            }
            trie.get(current).output.add(word);
        }

        public void buildFailureLinks() {
            Queue<Integer> queue = new LinkedList<>();
            for (int c = 0; c < 128; c++) {
                int next = trie.get(0).next[c];
                if (next != -1) {
                    trie.get(next).fail = 0;
                    queue.add(next);
                } else {
                    trie.get(0).next[c] = 0;
                }
            }

            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int c = 0; c < 128; c++) {
                    int next = trie.get(current).next[c];
                    if (next != -1) {
                        int failure = trie.get(current).fail;
                        while (failure > 0 && trie.get(failure).next[c] == -1) {
                            failure = trie.get(failure).fail;
                        }
                        if (trie.get(failure).next[c] != -1 && trie.get(failure).next[c] != next) {
                            failure = trie.get(failure).next[c];
                        }
                        trie.get(next).fail = failure;
                        trie.get(next).output.addAll(trie.get(failure).output);
                        queue.add(next);
                    }
                }
            }
        }

        public ArrayList<String> search(String text) {
            ArrayList<String> found = new ArrayList<>();
            if (text == null) return found;

            int current = 0;
            for (char c : text.toLowerCase().toCharArray()) {
                if (c >= 128) {
                    current = 0;
                    continue;
                }
                while (current > 0 && trie.get(current).next[c] == -1) {
                    current = trie.get(current).fail;
                }
                current = trie.get(current).next[c];
                if (current == -1) current = 0;

                if (!trie.get(current).output.isEmpty()) {
                    found.addAll(trie.get(current).output);
                }
            }
            return found;
        }
    }
}
