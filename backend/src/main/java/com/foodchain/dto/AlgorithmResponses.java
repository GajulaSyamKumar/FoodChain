package com.foodchain.dto;

import java.util.List;
import java.util.Map;

public class AlgorithmResponses {

    public static class CO1Response {
        private String algorithm;
        private String text;
        private String pattern;
        private boolean found;
        private List<Integer> matchIndices;
        private int firstIndex = -1;
        private int matchLength = 0;
        private String executionTime;
        private String explanation;
        private int[] zArray;

        public CO1Response() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }

        public boolean isFound() { return found; }
        public void setFound(boolean found) { this.found = found; }

        public List<Integer> getMatchIndices() { return matchIndices; }
        public void setMatchIndices(List<Integer> matchIndices) { this.matchIndices = matchIndices; }

        public int getFirstIndex() { return firstIndex; }
        public void setFirstIndex(int firstIndex) { this.firstIndex = firstIndex; }

        public int getMatchLength() { return matchLength; }
        public void setMatchLength(int matchLength) { this.matchLength = matchLength; }

        public String getExecutionTime() { return executionTime; }
        public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public int[] getzArray() { return zArray; }
        public void setzArray(int[] zArray) { this.zArray = zArray; }
    }

    public static class SuffixEntry {
        private int index;
        private String suffix;
        private int rank;
        private int lcp;

        public SuffixEntry() {}
        public SuffixEntry(int index, String suffix, int rank, int lcp) {
            this.index = index;
            this.suffix = suffix;
            this.rank = rank;
            this.lcp = lcp;
        }

        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }

        public String getSuffix() { return suffix; }
        public void setSuffix(String suffix) { this.suffix = suffix; }

        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }

        public int getLcp() { return lcp; }
        public void setLcp(int lcp) { this.lcp = lcp; }
    }

    public static class CO2Response {
        private String algorithm;
        private String text;
        private int[] suffixArray;
        private int[] lcpArray;
        private List<SuffixEntry> suffixes;
        private int totalSuffixes;
        private boolean found;
        private String queryPattern;
        private String executionTime;
        private String explanation;

        public CO2Response() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public int[] getSuffixArray() { return suffixArray; }
        public void setSuffixArray(int[] suffixArray) { this.suffixArray = suffixArray; }

        public int[] getLcpArray() { return lcpArray; }
        public void setLcpArray(int[] lcpArray) { this.lcpArray = lcpArray; }

        public List<SuffixEntry> getSuffixes() { return suffixes; }
        public void setSuffixes(List<SuffixEntry> suffixes) { this.suffixes = suffixes; }

        public int getTotalSuffixes() { return totalSuffixes; }
        public void setTotalSuffixes(int totalSuffixes) { this.totalSuffixes = totalSuffixes; }

        public boolean isFound() { return found; }
        public void setFound(boolean found) { this.found = found; }

        public String getQueryPattern() { return queryPattern; }
        public void setQueryPattern(String queryPattern) { this.queryPattern = queryPattern; }

        public String getExecutionTime() { return executionTime; }
        public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    public static class CO3Response {
        private String algorithm;
        private long result;
        private int distance;
        private int[][] dpMatrix;
        private String executionTime;
        private String explanation;
        private Map<String, Object> details;

        public CO3Response() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public long getResult() { return result; }
        public void setResult(long result) { this.result = result; }

        public int getDistance() { return distance; }
        public void setDistance(int distance) { this.distance = distance; }

        public int[][] getDpMatrix() { return dpMatrix; }
        public void setDpMatrix(int[][] dpMatrix) { this.dpMatrix = dpMatrix; }

        public String getExecutionTime() { return executionTime; }
        public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    public static class FlowEdgeResult {
        private String fromName;
        private String toName;
        private int from;
        private int to;
        private int flow;
        private int capacity;

        public FlowEdgeResult() {}
        public FlowEdgeResult(String fromName, String toName, int from, int to, int flow, int capacity) {
            this.fromName = fromName;
            this.toName = toName;
            this.from = from;
            this.to = to;
            this.flow = flow;
            this.capacity = capacity;
        }

        public String getFromName() { return fromName; }
        public void setFromName(String fromName) { this.fromName = fromName; }

        public String getToName() { return toName; }
        public void setToName(String toName) { this.toName = toName; }

        public int getFrom() { return from; }
        public void setFrom(int from) { this.from = from; }

        public int getTo() { return to; }
        public void setTo(int to) { this.to = to; }

        public int getFlow() { return flow; }
        public void setFlow(int flow) { this.flow = flow; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
    }

    public static class CO4Response {
        private String algorithm;
        private int maxFlow;
        private List<String> path;
        private String capacitySummary;
        private List<FlowEdgeResult> edges;
        private List<String> minCutEdges;
        private String executionTime;
        private String explanation;

        public CO4Response() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public int getMaxFlow() { return maxFlow; }
        public void setMaxFlow(int maxFlow) { this.maxFlow = maxFlow; }

        public List<String> getPath() { return path; }
        public void setPath(List<String> path) { this.path = path; }

        public String getCapacitySummary() { return capacitySummary; }
        public void setCapacitySummary(String capacitySummary) { this.capacitySummary = capacitySummary; }

        public List<FlowEdgeResult> getEdges() { return edges; }
        public void setEdges(List<FlowEdgeResult> edges) { this.edges = edges; }

        public List<String> getMinCutEdges() { return minCutEdges; }
        public void setMinCutEdges(List<String> minCutEdges) { this.minCutEdges = minCutEdges; }

        public String getExecutionTime() { return executionTime; }
        public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }
}
