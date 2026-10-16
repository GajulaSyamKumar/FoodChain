package com.foodchain;

import com.foodchain.dto.AlgorithmRequests.*;
import com.foodchain.dto.AlgorithmResponses.*;
import com.foodchain.service.AlgorithmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AlgorithmTests {

    @Autowired
    private AlgorithmService algorithmService;

    @Test
    void testCO1KMP() {
        CO1Request req = new CO1Request();
        req.setAlgorithm("KMP");
        req.setText("organic tomatoes are fresh tomatoes");
        req.setPattern("tomatoes");

        CO1Response res = algorithmService.executeCO1(req);
        assertNotNull(res);
        assertTrue(res.isFound());
        assertEquals(8, res.getFirstIndex());
        assertEquals(2, res.getMatchIndices().size());
    }

    @Test
    void testCO1ZFunction() {
        CO1Request req = new CO1Request();
        req.setAlgorithm("Z-Function");
        req.setText("tomatoes");

        CO1Response res = algorithmService.executeCO1(req);
        assertNotNull(res);
        assertNotNull(res.getzArray());
    }

    @Test
    void testCO1RabinKarpWithEnDash() {
        CO1Request req = new CO1Request();
        req.setAlgorithm("Rabin–Karp"); // en-dash
        req.setText("fresh potatoes and organic tomatoes");
        req.setPattern("potatoes");

        CO1Response res = algorithmService.executeCO1(req);
        assertNotNull(res);
        assertTrue(res.isFound());
        assertEquals(6, res.getFirstIndex());
    }

    @Test
    void testCO1AhoCorasick() {
        CO1Request req = new CO1Request();
        req.setAlgorithm("Aho-Corasick");
        req.setText("organic tomatoes are fresh tomatoes");
        req.setPattern("tomatoes");

        CO1Response res = algorithmService.executeCO1(req);
        assertNotNull(res);
        assertTrue(res.isFound());
        assertEquals(8, res.getFirstIndex());
    }

    @Test
    void testCO2SuffixArrayAndKasai() {
        CO2Request req = new CO2Request();
        req.setAlgorithm("Suffix Array");
        req.setText("banana");

        CO2Response res = algorithmService.executeCO2(req);
        assertNotNull(res);
        assertEquals(6, res.getTotalSuffixes());
        assertNotNull(res.getSuffixArray());
        assertNotNull(res.getLcpArray());
    }

    @Test
    void testCO2SAIS() {
        CO2Request req = new CO2Request();
        req.setAlgorithm("SA-IS");
        req.setText("tomatoes");

        CO2Response res = algorithmService.executeCO2(req);
        assertNotNull(res);
        assertEquals(8, res.getTotalSuffixes());
        assertNotNull(res.getSuffixArray());
    }

    @Test
    void testCO2SuffixTree() {
        CO2Request req = new CO2Request();
        req.setAlgorithm("Suffix Tree");
        req.setText("organic tomatoes");
        req.setPattern("tomatoes");

        CO2Response res = algorithmService.executeCO2(req);
        assertNotNull(res);
        assertTrue(res.isFound());
    }

    @Test
    void testCO2SuffixAutomaton() {
        CO2Request req = new CO2Request();
        req.setAlgorithm("Suffix Automaton");
        req.setText("organic tomatoes");
        req.setPattern("tomatoes");

        CO2Response res = algorithmService.executeCO2(req);
        assertNotNull(res);
        assertTrue(res.isFound());
    }

    @Test
    void testCO3Levenshtein() {
        CO3Request req = new CO3Request();
        req.setAlgorithm("Levenshtein");
        req.setText("Organic Tomatoes");
        req.setPattern("Organic Tomato");

        CO3Response res = algorithmService.executeCO3(req);
        assertNotNull(res);
        assertEquals(2, res.getDistance());
        assertNotNull(res.getDpMatrix());
    }

    @Test
    void testCO3DamerauLevenshteinWithEnDash() {
        CO3Request req = new CO3Request();
        req.setAlgorithm("Damerau–Levenshtein"); // en-dash
        req.setText("ca");
        req.setPattern("ac");

        CO3Response res = algorithmService.executeCO3(req);
        assertNotNull(res);
        assertEquals(1, res.getDistance());
    }

    @Test
    void testCO3BitmaskDP() {
        CO3Request req = new CO3Request();
        req.setAlgorithm("Bitmask DP");

        CO3Response res = algorithmService.executeCO3(req);
        assertNotNull(res);
        assertTrue(res.getResult() > 0);
    }

    @Test
    void testCO3MatrixChain() {
        CO3Request req = new CO3Request();
        req.setAlgorithm("Matrix Chain");

        CO3Response res = algorithmService.executeCO3(req);
        assertNotNull(res);
        assertTrue(res.getResult() > 0);
    }

    @Test
    void testCO3OptimalBST() {
        CO3Request req = new CO3Request();
        req.setAlgorithm("Optimal BST");

        CO3Response res = algorithmService.executeCO3(req);
        assertNotNull(res);
        assertTrue(res.getResult() > 0);
    }

    @Test
    void testCO4FordFulkersonWithEnDash() {
        CO4Request req = new CO4Request();
        req.setAlgorithm("Ford–Fulkerson"); // en-dash

        CO4Response res = algorithmService.executeCO4(req);
        assertNotNull(res);
        assertTrue(res.getMaxFlow() > 0);
        assertNotNull(res.getEdges());
    }

    @Test
    void testCO4EdmondsKarp() {
        CO4Request req = new CO4Request();
        req.setAlgorithm("Edmonds-Karp");

        CO4Response res = algorithmService.executeCO4(req);
        assertNotNull(res);
        assertTrue(res.getMaxFlow() > 0);
    }

    @Test
    void testCO4Dinic() {
        CO4Request req = new CO4Request();
        req.setAlgorithm("Dinic");

        CO4Response res = algorithmService.executeCO4(req);
        assertNotNull(res);
        assertTrue(res.getMaxFlow() > 0);
    }

    @Test
    void testCO4BipartiteMatching() {
        CO4Request req = new CO4Request();
        req.setAlgorithm("Bipartite Matching");

        CO4Response res = algorithmService.executeCO4(req);
        assertNotNull(res);
        assertTrue(res.getMaxFlow() > 0);
    }

    @Test
    void testCO4MaxFlowMinCut() {
        CO4Request req = new CO4Request();
        req.setAlgorithm("Max-Flow Min-Cut");

        CO4Response res = algorithmService.executeCO4(req);
        assertNotNull(res);
        assertTrue(res.getMaxFlow() > 0);
        assertNotNull(res.getMinCutEdges());
        assertFalse(res.getMinCutEdges().isEmpty());
    }
}
