package algorithms.utility;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.jetbrains.annotations.NotNull;

/**
 * Preconditions for graphs.
 */
public class GraphPreconditions {

    // ====== EXC-METHODS FOR GRAPHS ======

    public static void mustHaveWeights(@NotNull final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (!edgeHasWeight(edge))
                throw new IllegalArgumentException("Graph has edge without weight attribute");
            if (!isNumeric(edge))
                throw new IllegalArgumentException("Edge weight attribute is not a rational number");
        }
    }

    private static boolean isNumeric(final Edge edge) {
        return edge.getAttribute("weight") instanceof Number;
    }

    public static void mustHaveDirectedEdges(@NotNull final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (!isDirected(edge))
                throw new IllegalArgumentException("Graph has nonDirected edges");
        }
    }

    public static void mustHavePositiveWeights(@NotNull final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (edgeHasNegativeWeight(edge))
                throw new IllegalArgumentException("Graph has edge with negative weight!");
        }
    }

    public static void mustHaveCapacity(@NotNull final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (edgeHasNoCapacity(edge))
                throw new IllegalArgumentException("Graph has edge without capacity attribute");
        }
    }

    public static void mustHavePositiveCapacity(@NotNull final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (edgeHasNegativeCapacity(edge))
                throw new IllegalArgumentException("Graph has edge with negative capacity!");
        }
    }

    public static void isNetwork(final Graph graph) throws IllegalArgumentException {
        for (final Edge edge : graph.getEachEdge()) {
            if (edgeHasNoCapacity(edge))
                throw new IllegalArgumentException("Graph has edge without capacity attribute");
            if (edgeHasNegativeCapacity(edge))
                throw new IllegalArgumentException("Graph has edge with negative capacity!");
            if (!isDirected(edge))
                throw new IllegalArgumentException("Graph has nonDirected edges");
        }
    }

    // TODO Error-Msg auslagern
    // TODO eigene Exc als innerclass

    // ====== BOOLEAN-METHODS FOR EDGES ======

    private static boolean edgeHasNoCapacity(final Edge edge) {
        return !edge.hasAttribute("capacity");
    }

    private static boolean edgeHasNegativeCapacity(final Edge edge) {
        return ((Double.valueOf(edge.getAttribute("capacity").toString())) < 0.0);
    }

    private static boolean edgeHasWeight(final Edge edge) {
        return edge.hasAttribute("weight");
    }

    private static boolean edgeHasNegativeWeight(final Edge edge) {
        return ((Double.valueOf(edge.getAttribute("weight").toString())) < 0.0);
    }

    private static boolean isDirected(final Edge edge) {
        return edge.isDirected();
    }


}
