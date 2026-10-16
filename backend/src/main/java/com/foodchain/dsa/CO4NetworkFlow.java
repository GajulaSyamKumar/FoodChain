package com.foodchain.dsa;

import java.util.*;

public class CO4NetworkFlow {

    public static class Edge {
        public int to;
        public int capacity;
        public int flow;
        public Edge reverse;

        public Edge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int residualCapacity() {
            return capacity - flow;
        }
    }

    public static class FlowNetwork {
        public int vertices;
        public ArrayList<Edge>[] graph;

        @SuppressWarnings("unchecked")
        public FlowNetwork(int vertices) {
            this.vertices = vertices;
            graph = new ArrayList[vertices];
            for (int i = 0; i < vertices; i++) {
                graph[i] = new ArrayList<>();
            }
        }

        public void addEdge(int from, int to, int capacity) {
            Edge forward = new Edge(to, capacity);
            Edge backward = new Edge(from, 0);
            forward.reverse = backward;
            backward.reverse = forward;
            graph[from].add(forward);
            graph[to].add(backward);
        }

        public void resetFlows() {
            for (int i = 0; i < vertices; i++) {
                for (Edge edge : graph[i]) {
                    edge.flow = 0;
                }
            }
        }
    }

    // Standard FoodChain Network generator:
    // 0 = Supplier, 1 = Warehouse A, 2 = Warehouse B, 3 = Distribution Center, 4 = Market
    public static FlowNetwork createDefaultFoodNetwork() {
        FlowNetwork network = new FlowNetwork(5);
        network.addEdge(0, 1, 100);
        network.addEdge(0, 2, 80);
        network.addEdge(1, 3, 70);
        network.addEdge(2, 3, 60);
        network.addEdge(3, 4, 100);
        return network;
    }

    public static String getNodeName(int index) {
        return switch (index) {
            case 0 -> "Supplier";
            case 1 -> "Warehouse A";
            case 2 -> "Warehouse B";
            case 3 -> "Distribution Center";
            case 4 -> "Market";
            default -> "Node " + index;
        };
    }

    // 1. FORD-FULKERSON (DFS)
    public static int fordFulkerson(FlowNetwork network, int source, int sink) {
        network.resetFlows();
        int maxFlow = 0;

        while (true) {
            boolean[] visited = new boolean[network.vertices];
            Edge[] parent = new Edge[network.vertices];

            boolean found = dfsFlow(network, source, sink, visited, parent);
            if (!found) break;

            int pathFlow = Integer.MAX_VALUE;
            int current = sink;
            while (current != source) {
                Edge edge = parent[current];
                pathFlow = Math.min(pathFlow, edge.residualCapacity());
                current = edge.reverse.to;
            }

            current = sink;
            while (current != source) {
                Edge edge = parent[current];
                edge.flow += pathFlow;
                edge.reverse.flow -= pathFlow;
                current = edge.reverse.to;
            }

            maxFlow += pathFlow;
        }
        return maxFlow;
    }

    private static boolean dfsFlow(FlowNetwork network, int current, int sink, boolean[] visited, Edge[] parent) {
        visited[current] = true;
        if (current == sink) return true;

        for (Edge edge : network.graph[current]) {
            if (!visited[edge.to] && edge.residualCapacity() > 0) {
                parent[edge.to] = edge;
                if (dfsFlow(network, edge.to, sink, visited, parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 2. EDMONDS-KARP (BFS)
    public static int edmondsKarp(FlowNetwork network, int source, int sink) {
        network.resetFlows();
        int maxFlow = 0;

        while (true) {
            Edge[] parent = new Edge[network.vertices];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);

            while (!queue.isEmpty() && parent[sink] == null) {
                int current = queue.poll();
                for (Edge edge : network.graph[current]) {
                    if (edge.to != source && parent[edge.to] == null && edge.residualCapacity() > 0) {
                        parent[edge.to] = edge;
                        queue.add(edge.to);
                    }
                }
            }

            if (parent[sink] == null) break;

            int pathFlow = Integer.MAX_VALUE;
            int current = sink;
            while (current != source) {
                Edge edge = parent[current];
                pathFlow = Math.min(pathFlow, edge.residualCapacity());
                current = edge.reverse.to;
            }

            current = sink;
            while (current != source) {
                Edge edge = parent[current];
                edge.flow += pathFlow;
                edge.reverse.flow -= pathFlow;
                current = edge.reverse.to;
            }

            maxFlow += pathFlow;
        }
        return maxFlow;
    }

    // 3. DINIC'S ALGORITHM (BFS Level graph + DFS blocking flow)
    public static int dinic(FlowNetwork network, int source, int sink) {
        network.resetFlows();
        int maxFlow = 0;
        int[] level = new int[network.vertices];

        while (dinicBFS(network, source, sink, level)) {
            int[] ptr = new int[network.vertices];
            while (true) {
                int pushed = dinicDFS(network, source, sink, Integer.MAX_VALUE, level, ptr);
                if (pushed == 0) break;
                maxFlow += pushed;
            }
        }
        return maxFlow;
    }

    private static boolean dinicBFS(FlowNetwork network, int source, int sink, int[] level) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (Edge edge : network.graph[current]) {
                if (edge.residualCapacity() > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[current] + 1;
                    queue.add(edge.to);
                }
            }
        }
        return level[sink] != -1;
    }

    private static int dinicDFS(FlowNetwork network, int current, int sink, int pushed, int[] level, int[] ptr) {
        if (pushed == 0 || current == sink) return pushed;

        for (int cid = ptr[current]; cid < network.graph[current].size(); cid = ++ptr[current]) {
            Edge edge = network.graph[current].get(cid);
            if (level[edge.to] == level[current] + 1 && edge.residualCapacity() > 0) {
                int tr = dinicDFS(network, edge.to, sink, Math.min(pushed, edge.residualCapacity()), level, ptr);
                if (tr == 0) continue;
                edge.flow += tr;
                edge.reverse.flow -= tr;
                return tr;
            }
        }
        return 0;
    }

    // 4. BIPARTITE MATCHING
    public static int bipartiteMatching(boolean[][] bipartiteMatrix) {
        if (bipartiteMatrix == null || bipartiteMatrix.length == 0) return 0;
        int leftCount = bipartiteMatrix.length;
        int rightCount = bipartiteMatrix[0].length;
        int[] matchR = new int[rightCount];
        Arrays.fill(matchR, -1);

        int result = 0;
        for (int u = 0; u < leftCount; u++) {
            boolean[] seen = new boolean[rightCount];
            if (bpmDFS(bipartiteMatrix, u, seen, matchR)) {
                result++;
            }
        }
        return result;
    }

    private static boolean bpmDFS(boolean[][] bpGraph, int u, boolean[] seen, int[] matchR) {
        for (int v = 0; v < bpGraph[0].length; v++) {
            if (bpGraph[u][v] && !seen[v]) {
                seen[v] = true;
                if (matchR[v] < 0 || bpmDFS(bpGraph, matchR[v], seen, matchR)) {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    // 5. MAX-FLOW MIN-CUT CUT EDGES
    public static List<String> getMinCutEdges(FlowNetwork network, int source) {
        boolean[] visited = new boolean[network.vertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (Edge edge : network.graph[current]) {
                if (edge.residualCapacity() > 0 && !visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        List<String> cutEdges = new ArrayList<>();
        for (int u = 0; u < network.vertices; u++) {
            if (visited[u]) {
                for (Edge edge : network.graph[u]) {
                    if (!visited[edge.to] && edge.capacity > 0) {
                        cutEdges.add(getNodeName(u) + " → " + getNodeName(edge.to) + " (Capacity: " + edge.capacity + ")");
                    }
                }
            }
        }
        return cutEdges;
    }
}
