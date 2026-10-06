package com.graphs;

import java.util.List;
import java.util.Set;

public interface Graph<T> {

    void addVertex(T vertex);

    void addEdge(T source, T destination, double weight);

    Set<T> getVertices();

    List<Edge<T>> getNeighbors(T vertex);

    double getWeight(T source, T destination);

    boolean hasVertex(T vertex);

    boolean hasEdge(T source, T destination);

    public static class Edge<T> {
        private final T source;
        private final T destination;
        private final double weight;

        public Edge(T source, T destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public T getSource() { return source; }
        public T getDestination() { return destination; }
        public double getWeight() { return weight; }

        @Override
        public String toString() {
            return source + " -> " + destination + " (" + weight + ")";
        }
    }
}
