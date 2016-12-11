package algorithms;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class Preconditions {

    // ====== EXC-METHODS FOR GRAPHS ======

    public static void mustHaveWeights(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (edgeHasNoWeight(edge))
                throw new IllegalArgumentException("Graph has edge without weight attribute");
        }
    }

    public static void mustHaveDirectedEdges(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (isUndirected(edge))
                throw new IllegalArgumentException("Graph has nonDirected edges");
        }
    }

    public static void mustHavePositiveWeights(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (edgeHasNegativeWeight(edge))
                throw new IllegalArgumentException("Graph has edge with negative weight!");
        }
    }

    public static void mustHaveCapacity(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (edgeHasNoCapacity(edge))
                throw new IllegalArgumentException("Graph has edge without capacity attribute");
        }
    }

    public static void mustHavePositiveCapacity(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (edgeHasNegativeCapacity(edge))
                throw new IllegalArgumentException("Graph has edge with negative capacity!");
        }
    }

    public static void isNetwork(Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if (edgeHasNoCapacity(edge))
                throw new IllegalArgumentException("Graph has edge without capacity attribute");
            if (edgeHasNegativeCapacity(edge))
                throw new IllegalArgumentException("Graph has edge with negative capacity!");
            if (isUndirected(edge))
                throw new IllegalArgumentException("Graph has nonDirected edges");
            // TODO keine schleifen, da schlicht
            // TODO ? schwach zusammenh¨angenden ?
        }
    }

    // TODO Error-Msg auslagern
    // TODO eigene Exc als innerclass

    // ====== BOOLEAN-METHODS FOR EDGES ======

    private static boolean edgeHasNoCapacity(Edge edge) {
        return !edge.hasAttribute("capacity");
    }

    private static boolean edgeHasNegativeCapacity(Edge edge) {
        return ((Double.valueOf(edge.getAttribute("capacity").toString())) < 0.0);
    }

    private static boolean edgeHasNoWeight(Edge edge) {
        return !edge.hasAttribute("weight");
    }

    private static boolean edgeHasNegativeWeight(Edge edge) {
        return ((Double.valueOf(edge.getAttribute("weight").toString())) < 0.0);
    }

    private static boolean isUndirected(Edge edge) {
        return !edge.isDirected();
    }


}
