package com.graphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    private WeightedGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new WeightedGraph<>(false);
        // Build graph with nodes A, B, C, D, E
        graph.addEdge("A", "B", 4.0);
        graph.addEdge("A", "C", 2.0);
        graph.addEdge("B", "C", 1.0);
        graph.addEdge("B", "D", 5.0);
        graph.addEdge("C", "D", 8.0);
        graph.addEdge("C", "E", 10.0);
        graph.addEdge("D", "E", 2.0);
    }

    @Test
    @DisplayName("Test Dijkstra Shortest Path correctness")
    public void testDijkstraShortestPath() {
        Dijkstra<String> dijkstra = new Dijkstra<>();
        Dijkstra.Result<String> result = dijkstra.findShortestPaths(graph, "A");

        Map<String, Double> distances = result.getDistances();
        assertEquals(0.0, distances.get("A"));
        assertEquals(3.0, distances.get("B")); // A -> C -> B (2 + 1 = 3)
        assertEquals(2.0, distances.get("C")); // A -> C (2)
        assertEquals(8.0, distances.get("D")); // A -> C -> B -> D (2 + 1 + 5 = 8)
        assertEquals(10.0, distances.get("E")); // A -> C -> B -> D -> E (8 + 2 = 10)

        List<String> pathToE = result.getShortestPathTo("E");
        assertEquals(List.of("A", "C", "B", "D", "E"), pathToE);
    }

    @Test
    @DisplayName("Test Prim Minimum Spanning Tree (MST) correctness")
    public void testPrimMST() {
        Prim<String> prim = new Prim<>();
        Prim.MSTResult<String> result = prim.findMinimumSpanningTree(graph, "A");

        assertEquals(4, result.getMstEdges().size()); // 5 vertices -> 4 edges in MST
        assertEquals(10.0, result.getTotalWeight(), 0.001); // 2 + 1 + 5 + 2 = 10
    }

    @Test
    @DisplayName("Test Graph Edge operations and non-existent nodes")
    public void testGraphOperations() {
        assertTrue(graph.hasVertex("A"));
        assertFalse(graph.hasVertex("Z"));
        assertTrue(graph.hasEdge("A", "B"));
        assertFalse(graph.hasEdge("A", "E"));
        assertEquals(4.0, graph.getWeight("A", "B"));
        assertEquals(Double.POSITIVE_INFINITY, graph.getWeight("A", "Z"));
    }
}
