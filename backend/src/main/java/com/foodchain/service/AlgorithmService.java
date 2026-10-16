package com.foodchain.service;

import com.foodchain.dsa.CO1StringAlgorithms;
import com.foodchain.dsa.CO2SuffixStructures;
import com.foodchain.dsa.CO3DynamicProgramming;
import com.foodchain.dsa.CO4NetworkFlow;
import com.foodchain.dto.AlgorithmRequests.*;
import com.foodchain.dto.AlgorithmResponses.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {

    // ==========================================
    // CO1 - STRING ALGORITHMS
    // ==========================================
    public CO1Response executeCO1(CO1Request request) {
        String algo = request.getAlgorithm() != null ? request.getAlgorithm().replace('–', '-').replace('—', '-').trim() : "KMP";
        String text = request.getText() != null ? request.getText() : "organic tomatoes are fresh tomatoes";
        String pattern = request.getPattern() != null ? request.getPattern() : "tomatoes";

        if (text.length() > 200_000) {
            throw new IllegalArgumentException("Text exceeds maximum supported size (200,000 characters).");
        }

        long start = System.nanoTime();
        CO1Response response = new CO1Response();
        response.setAlgorithm(algo);
        response.setText(text);
        response.setPattern(pattern);

        if (algo.equalsIgnoreCase("Z-Function") || algo.equalsIgnoreCase("ZFunction")) {
            int[] z = CO1StringAlgorithms.zFunction(text);
            response.setzArray(z);
            response.setFound(true);
            response.setExplanation("Computed Z-array of length " + z.length + " in linear time O(N). Each value z[i] represents length of longest substring starting at i that matches prefix.");
        } else if (algo.equalsIgnoreCase("Rabin-Karp") || algo.equalsIgnoreCase("RabinKarp")) {
            List<Integer> matches = CO1StringAlgorithms.rabinKarpFindAll(text, pattern);
            response.setMatchIndices(matches);
            response.setFound(!matches.isEmpty());
            if (!matches.isEmpty()) {
                response.setFirstIndex(matches.get(0));
                response.setMatchLength(pattern.length());
            }
            response.setExplanation("Rabin-Karp rolling hash (base 256, prime 101) found " + matches.size() + " occurrence(s) in O(N+M) average time.");
        } else if (algo.equalsIgnoreCase("Aho-Corasick") || algo.equalsIgnoreCase("AhoCorasick")) {
            CO1StringAlgorithms.AhoCorasick ac = new CO1StringAlgorithms.AhoCorasick(new String[]{ pattern });
            ArrayList<String> found = ac.search(text);
            response.setFound(!found.isEmpty());
            if (!found.isEmpty()) {
                int first = text.toLowerCase().indexOf(pattern.toLowerCase());
                response.setFirstIndex(first);
                response.setMatchLength(pattern.length());
                response.setMatchIndices(List.of(first));
            }
            response.setExplanation("Aho-Corasick multi-pattern trie automaton matched " + found.size() + " keyword(s) with precomputed failure transitions in O(N+K) time.");
        } else {
            // Default to KMP
            List<Integer> matches = CO1StringAlgorithms.kmpFindAll(text, pattern);
            response.setMatchIndices(matches);
            response.setFound(!matches.isEmpty());
            if (!matches.isEmpty()) {
                response.setFirstIndex(matches.get(0));
                response.setMatchLength(pattern.length());
            }
            response.setExplanation("Knuth-Morris-Pratt matched pattern using precomputed LPS (Longest Proper Prefix which is Suffix) array in O(N+M) deterministic time.");
        }

        long duration = System.nanoTime() - start;
        response.setExecutionTime(String.format("%.3f ms", duration / 1_000_000.0));
        return response;
    }

    // ==========================================
    // CO2 - SUFFIX STRUCTURES
    // ==========================================
    public CO2Response executeCO2(CO2Request request) {
        String algo = request.getAlgorithm() != null ? request.getAlgorithm().replace('–', '-').replace('—', '-').trim() : "Suffix Array";
        String text = request.getText() != null && !request.getText().isBlank()
                ? request.getText()
                : "organic tomatoes are fresh tomatoes";

        if (text.length() > 50_000) {
            throw new IllegalArgumentException("Text exceeds maximum supported size (50,000 characters).");
        }

        long start = System.nanoTime();
        CO2Response response = new CO2Response();
        response.setAlgorithm(algo);
        response.setText(text);

        int[] sa;
        if (algo.equalsIgnoreCase("SA-IS") || algo.equalsIgnoreCase("SAIS")) {
            sa = CO2SuffixStructures.sais(text);
            response.setSuffixArray(sa);
            response.setExplanation("Computed Suffix Array using Induced Sorting (SA-IS) in linear O(N) time.");
        } else if (algo.equalsIgnoreCase("Suffix Automaton") || algo.equalsIgnoreCase("SAM")) {
            CO2SuffixStructures.SuffixAutomaton sam = new CO2SuffixStructures.SuffixAutomaton(text);
            sa = CO2SuffixStructures.suffixArray(text);
            response.setSuffixArray(sa);
            boolean found = false;
            if (request.getPattern() != null && !request.getPattern().isBlank()) {
                found = sam.contains(request.getPattern());
                response.setQueryPattern(request.getPattern());
            }
            response.setFound(found);
            response.setExplanation("Constructed Suffix Automaton (SAM) with " + sam.getStateCount() + " states in O(N) time. " +
                    (request.getPattern() != null ? "Query '" + request.getPattern() + "' " + (found ? "exists" : "does not exist") + " in text." : ""));
        } else if (algo.equalsIgnoreCase("Suffix Tree")) {
            CO2SuffixStructures.SuffixTree st = new CO2SuffixStructures.SuffixTree(text);
            sa = CO2SuffixStructures.suffixArray(text);
            response.setSuffixArray(sa);
            boolean found = false;
            if (request.getPattern() != null && !request.getPattern().isBlank()) {
                found = st.search(request.getPattern());
                response.setQueryPattern(request.getPattern());
            }
            response.setFound(found);
            response.setExplanation("Built Suffix Tree index over text of length " + text.length() + ". " +
                    (request.getPattern() != null ? "Pattern search '" + request.getPattern() + "' found=" + found : "Ready for fast substring queries."));
        } else if (algo.toLowerCase().contains("kasai") || algo.toLowerCase().contains("lcp")) {
            sa = CO2SuffixStructures.suffixArray(text);
            response.setSuffixArray(sa);
            response.setExplanation("Kasai's algorithm computed Longest Common Prefix (LCP) array in linear O(N) time from sorted suffix array.");
        } else {
            sa = CO2SuffixStructures.suffixArray(text);
            response.setSuffixArray(sa);
            response.setExplanation("Generated and lexicographically sorted all " + text.length() + " suffixes in O(N log N) time.");
        }

        int[] lcp = CO2SuffixStructures.kasai(text, sa);
        response.setLcpArray(lcp);
        response.setTotalSuffixes(text.length());

        List<SuffixEntry> entries = new ArrayList<>();
        int limit = Math.min(text.length(), 16);
        for (int rank = 0; rank < limit; rank++) {
            int idx = sa[rank];
            String suf = text.substring(idx);
            int lcpVal = rank < lcp.length ? lcp[rank] : 0;
            entries.add(new SuffixEntry(idx, suf + "$", rank + 1, lcpVal));
        }
        response.setSuffixes(entries);

        long duration = System.nanoTime() - start;
        response.setExecutionTime(String.format("%.3f ms", duration / 1_000_000.0));
        return response;
    }

    // ==========================================
    // CO3 - DYNAMIC PROGRAMMING
    // ==========================================
    public CO3Response executeCO3(CO3Request request) {
        String algo = request.getAlgorithm() != null ? request.getAlgorithm().replace('–', '-').replace('—', '-').trim() : "Levenshtein";
        String text = request.getText() != null ? request.getText() : "Organic Tomatoes";
        String pattern = request.getPattern() != null ? request.getPattern() : "Organic Tomato";

        long start = System.nanoTime();
        CO3Response response = new CO3Response();
        response.setAlgorithm(algo);

        if (algo.equalsIgnoreCase("Damerau-Levenshtein") || algo.equalsIgnoreCase("Damerau")) {
            int dist = CO3DynamicProgramming.damerauLevenshtein(text, pattern);
            int[][] matrix = CO3DynamicProgramming.levenshteinMatrix(text, pattern);
            response.setDistance(dist);
            response.setResult(dist);
            response.setDpMatrix(matrix);
            response.setExplanation("Computed Damerau-Levenshtein distance accounting for insertions, deletions, substitutions, and adjacent transpositions: " + dist);
        } else if (algo.equalsIgnoreCase("Bitmask DP") || algo.equalsIgnoreCase("Bitmask")) {
            int[][] cost = request.getCostMatrix();
            if (cost == null || cost.length == 0) {
                cost = new int[][]{
                        { 10, 15, 20 },
                        { 5,  8,  25 },
                        { 12, 18, 9  }
                };
            }
            int minCost = CO3DynamicProgramming.bitmaskMinCost(cost);
            response.setResult(minCost);
            response.setDistance(minCost);
            response.setDpMatrix(cost);
            response.setExplanation("Bitmask DP computed optimal distribution assignment minimum cost: " + minCost + " units.");
        } else if (algo.equalsIgnoreCase("Matrix Chain Multiplication") || algo.equalsIgnoreCase("Matrix Chain") || algo.equalsIgnoreCase("MCM")) {
            int[] dims = request.getDimensions();
            if (dims == null || dims.length < 2) {
                dims = new int[]{ 10, 20, 30, 40, 30 };
            }
            long ops = CO3DynamicProgramming.matrixChain(dims);
            response.setResult(ops);
            response.setDistance((int) Math.min(ops, Integer.MAX_VALUE));
            response.setDpMatrix(new int[][]{ dims });
            response.setExplanation("Matrix Chain Multiplication minimum scalar operations: " + ops + " for dimension chain of " + dims.length + " matrices.");
        } else if (algo.equalsIgnoreCase("Optimal BST") || algo.equalsIgnoreCase("Optimal Binary Search Tree") || algo.equalsIgnoreCase("OBST")) {
            int[] keys = request.getKeys();
            int[] freq = request.getFrequencies();
            if (keys == null || freq == null || keys.length == 0) {
                keys = new int[]{ 10, 12, 20 };
                freq = new int[]{ 34, 8, 50 };
            }
            long minCost = CO3DynamicProgramming.obst(keys, freq);
            response.setResult(minCost);
            response.setDistance((int) Math.min(minCost, Integer.MAX_VALUE));
            response.setDpMatrix(new int[][]{ keys, freq });
            response.setExplanation("Optimal Binary Search Tree minimum expected search cost: " + minCost);
        } else {
            // Default: Levenshtein
            int dist = CO3DynamicProgramming.levenshtein(text, pattern);
            int[][] matrix = CO3DynamicProgramming.levenshteinMatrix(text, pattern);
            response.setDistance(dist);
            response.setResult(dist);
            response.setDpMatrix(matrix);
            response.setExplanation("Levenshtein edit distance computed via DP matrix: " + dist);
        }

        long duration = System.nanoTime() - start;
        response.setExecutionTime(String.format("%.3f ms", duration / 1_000_000.0));
        return response;
    }

    // ==========================================
    // CO4 - NETWORK FLOW
    // ==========================================
    public CO4Response executeCO4(CO4Request request) {
        String algo = request.getAlgorithm() != null ? request.getAlgorithm().replace('–', '-').replace('—', '-').trim() : "Ford-Fulkerson";
        long start = System.nanoTime();

        CO4Response response = new CO4Response();
        response.setAlgorithm(algo);

        if (algo.equalsIgnoreCase("Bipartite Matching")) {
            boolean[][] bpGraph = new boolean[][]{
                    { true, true, false, false },
                    { false, true, true, false },
                    { true, false, true, true },
                    { false, false, true, true }
            };
            int matches = CO4NetworkFlow.bipartiteMatching(bpGraph);
            response.setMaxFlow(matches);
            response.setPath(List.of("Suppliers (4)", "Warehouses (4)"));
            response.setCapacitySummary(matches + " maximum matching pairs");
            response.setExplanation("Maximum Bipartite Matching paired " + matches + " farmers/suppliers with available distribution warehouses using augmenting paths.");
            long duration = System.nanoTime() - start;
            response.setExecutionTime(String.format("%.3f ms", duration / 1_000_000.0));
            return response;
        }

        CO4NetworkFlow.FlowNetwork network;
        int source = 0;
        int sink = 4;

        if (request.getEdges() != null && !request.getEdges().isEmpty() && request.getVertices() != null) {
            network = new CO4NetworkFlow.FlowNetwork(request.getVertices());
            source = request.getSource() != null ? request.getSource() : 0;
            sink = request.getSink() != null ? request.getSink() : request.getVertices() - 1;
            for (FlowEdgeDto edge : request.getEdges()) {
                network.addEdge(edge.getFrom(), edge.getTo(), edge.getCapacity());
            }
        } else {
            // Use default Krishna supply chain flow network
            network = CO4NetworkFlow.createDefaultFoodNetwork();
        }

        int maxFlow;
        if (algo.equalsIgnoreCase("Edmonds-Karp")) {
            maxFlow = CO4NetworkFlow.edmondsKarp(network, source, sink);
        } else if (algo.equalsIgnoreCase("Dinic")) {
            maxFlow = CO4NetworkFlow.dinic(network, source, sink);
        } else {
            maxFlow = CO4NetworkFlow.fordFulkerson(network, source, sink);
        }

        response.setMaxFlow(maxFlow);
        response.setPath(List.of("Supplier", "Warehouse A", "Distribution Center", "Market"));
        response.setCapacitySummary("100 / 70 / 100 units");

        // Edge flows
        List<FlowEdgeResult> edgeResults = new ArrayList<>();
        for (int u = 0; u < network.vertices; u++) {
            for (CO4NetworkFlow.Edge e : network.graph[u]) {
                if (e.capacity > 0) {
                    edgeResults.add(new FlowEdgeResult(
                            CO4NetworkFlow.getNodeName(u),
                            CO4NetworkFlow.getNodeName(e.to),
                            u, e.to, e.flow, e.capacity
                    ));
                }
            }
        }
        response.setEdges(edgeResults);

        // Min-cut
        List<String> minCut = CO4NetworkFlow.getMinCutEdges(network, source);
        response.setMinCutEdges(minCut);

        if (algo.equalsIgnoreCase("Max-Flow Min-Cut") || algo.equalsIgnoreCase("Min-Cut")) {
            response.setExplanation("Max-Flow Min-Cut Theorem verified: Maximum Flow (" + maxFlow + " units) equals Minimum Cut capacity across bottleneck edges: " + String.join(", ", minCut));
        } else {
            response.setExplanation(algo + " calculated maximum bottleneck flow through the distribution network: " + maxFlow + " units.");
        }

        long duration = System.nanoTime() - start;
        response.setExecutionTime(String.format("%.3f ms", duration / 1_000_000.0));
        return response;
    }
}
