package com.graphs;

import java.util.*;

public class Dijkstra<T> {

    public static class Result<T> {
        private final Map<T, Double> distances;
        private final Map<T, T> predecessors;

        public Result(Map<T, Double> distances, Map<T, T> predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }

        public Map<T, Double> getDistances() { return distances; }
        public Map<T, T> getPredecessors() { return predecessors; }

        public List<T> getShortestPathTo(T destination) {
            List<T> path = new ArrayList<>();
            if (!distances.containsKey(destination) || distances.get(destination) == Double.POSITIVE_INFINITY) {
                return path;
            }
            T curr = destination;
            while (curr != null) {
                path.add(0, curr);
                curr = predecessors.get(curr);
            }
            return path;
        }
    }

    public Result<T> findShortestPaths(Graph<T> graph, T source) {
        if (!graph.hasVertex(source)) {
            throw new IllegalArgumentException("Source vertex does not exist in graph");
        }

        Map<T, Double> distances = new HashMap<>();
        Map<T, T> predecessors = new HashMap<>();
        PriorityQueue<VertexNode<T>> pq = new PriorityQueue<>(Comparator.comparingDouble(VertexNode::getDistance));
        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }

        distances.put(source, 0.0);
        pq.add(new VertexNode<>(source, 0.0));

        while (!pq.isEmpty()) {
            VertexNode<T> current = pq.poll();
            T u = current.getVertex();

            if (visited.contains(u)) continue;
            visited.add(u);

            for (Graph.Edge<T> edge : graph.getNeighbors(u)) {
                T v = edge.getDestination();
                if (!visited.contains(v)) {
                    double newDist = distances.get(u) + edge.getWeight();
                    if (newDist < distances.get(v)) {
                        distances.put(v, newDist);
                        predecessors.put(v, u);
                        pq.add(new VertexNode<>(v, newDist));
                    }
                }
            }
        }

        return new Result<>(distances, predecessors);
    }

    private static class VertexNode<T> {
        private final T vertex;
        private final double distance;

        public VertexNode(T vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public T getVertex() { return vertex; }
        public double getDistance() { return distance; }
    }
}
