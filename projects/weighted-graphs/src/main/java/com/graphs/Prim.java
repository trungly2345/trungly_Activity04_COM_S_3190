package com.graphs;

import java.util.*;

public class Prim<T> {

    public static class MSTResult<T> {
        private final List<Graph.Edge<T>> mstEdges;
        private final double totalWeight;

        public MSTResult(List<Graph.Edge<T>> mstEdges, double totalWeight) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
        }

        public List<Graph.Edge<T>> getMstEdges() { return mstEdges; }
        public double getTotalWeight() { return totalWeight; }
    }

    public MSTResult<T> findMinimumSpanningTree(Graph<T> graph, T startVertex) {
        if (!graph.hasVertex(startVertex)) {
            throw new IllegalArgumentException("Start vertex does not exist in graph");
        }

        List<Graph.Edge<T>> mstEdges = new ArrayList<>();
        double totalWeight = 0.0;
        Set<T> inMST = new HashSet<>();
        PriorityQueue<Graph.Edge<T>> pq = new PriorityQueue<>(Comparator.comparingDouble(Graph.Edge::getWeight));

        inMST.add(startVertex);
        pq.addAll(graph.getNeighbors(startVertex));

        while (!pq.isEmpty() && inMST.size() < graph.getVertices().size()) {
            Graph.Edge<T> edge = pq.poll();
            T dest = edge.getDestination();

            if (inMST.contains(dest)) continue;

            inMST.add(dest);
            mstEdges.add(edge);
            totalWeight += edge.getWeight();

            for (Graph.Edge<T> neighborEdge : graph.getNeighbors(dest)) {
                if (!inMST.contains(neighborEdge.getDestination())) {
                    pq.add(neighborEdge);
                }
            }
        }

        return new MSTResult<>(mstEdges, totalWeight);
    }
}
