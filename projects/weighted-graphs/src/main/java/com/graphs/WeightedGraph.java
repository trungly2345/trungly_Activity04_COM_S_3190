package com.graphs;

import java.util.*;

public class WeightedGraph<T> implements Graph<T> {

    private final Map<T, List<Edge<T>>> adjacencyList;
    private final boolean isDirected;

    public WeightedGraph() {
        this(false);
    }

    public WeightedGraph(boolean isDirected) {
        this.adjacencyList = new HashMap<>();
        this.isDirected = isDirected;
    }

    @Override
    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void addEdge(T source, T destination, double weight) {
        addVertex(source);
        addVertex(destination);

        adjacencyList.get(source).add(new Edge<>(source, destination, weight));
        if (!isDirected) {
            adjacencyList.get(destination).add(new Edge<>(destination, source, weight));
        }
    }

    @Override
    public Set<T> getVertices() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    @Override
    public List<Edge<T>> getNeighbors(T vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    @Override
    public double getWeight(T source, T destination) {
        List<Edge<T>> edges = adjacencyList.get(source);
        if (edges != null) {
            for (Edge<T> edge : edges) {
                if (edge.getDestination().equals(destination)) {
                    return edge.getWeight();
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean hasVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    @Override
    public boolean hasEdge(T source, T destination) {
        return getWeight(source, destination) != Double.POSITIVE_INFINITY;
    }
}
