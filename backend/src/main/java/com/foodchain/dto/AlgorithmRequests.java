package com.foodchain.dto;

import java.util.List;

public class AlgorithmRequests {

    public static class CO1Request {
        private String algorithm = "KMP";
        private String text;
        private String pattern;

        public CO1Request() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
    }

    public static class CO2Request {
        private String algorithm = "Suffix Array";
        private String text;
        private String pattern;

        public CO2Request() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
    }

    public static class CO3Request {
        private String algorithm = "Levenshtein";
        private String text;
        private String pattern;
        private int[][] costMatrix;
        private int[] dimensions;
        private int[] keys;
        private int[] frequencies;

        public CO3Request() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }

        public int[][] getCostMatrix() { return costMatrix; }
        public void setCostMatrix(int[][] costMatrix) { this.costMatrix = costMatrix; }

        public int[] getDimensions() { return dimensions; }
        public void setDimensions(int[] dimensions) { this.dimensions = dimensions; }

        public int[] getKeys() { return keys; }
        public void setKeys(int[] keys) { this.keys = keys; }

        public int[] getFrequencies() { return frequencies; }
        public void setFrequencies(int[] frequencies) { this.frequencies = frequencies; }
    }

    public static class FlowEdgeDto {
        private int from;
        private int to;
        private int capacity;

        public FlowEdgeDto() {}
        public FlowEdgeDto(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        public int getFrom() { return from; }
        public void setFrom(int from) { this.from = from; }

        public int getTo() { return to; }
        public void setTo(int to) { this.to = to; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
    }

    public static class CO4Request {
        private String algorithm = "Ford-Fulkerson";
        private Integer vertices;
        private Integer source;
        private Integer sink;
        private List<FlowEdgeDto> edges;

        public CO4Request() {}

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public Integer getVertices() { return vertices; }
        public void setVertices(Integer vertices) { this.vertices = vertices; }

        public Integer getSource() { return source; }
        public void setSource(Integer source) { this.source = source; }

        public Integer getSink() { return sink; }
        public void setSink(Integer sink) { this.sink = sink; }

        public List<FlowEdgeDto> getEdges() { return edges; }
        public void setEdges(List<FlowEdgeDto> edges) { this.edges = edges; }
    }
}
