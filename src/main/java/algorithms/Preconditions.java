package algorithms;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class Preconditions {

    /**
     * <b>Precondition</b>
     *
     * @return true if all edges are directed
     */
    @NotNull
    public static Boolean hasWeights(@NotNull Graph graph) {
        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.hasAttribute("weight"))
                hasWeight = false;
        }
        return hasWeight;
    }

    /**
     * <b>Precondition</b>
     *
     * @throws IllegalArgumentException if graph has edge without weight attribute
     */
    public static void mustHaveWeights(@NotNull Graph graph) {
        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.hasAttribute("weight"))
                throw new IllegalArgumentException("Graph has edge without weight attribute");
        }
    }

    /**
     * <b>Precondition</b>
     *
     * @return true if all edges are directed
     */
    @NotNull
    public static Boolean isDirected(@NotNull Graph graph) {
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.isDirected()) return false;
        }
        return true;
    }

    /**
     * <b>Precondition</b>
     *
     * @throws IllegalArgumentException if graph has nonDirected edges.
     */
    public static void noNonDirectedEdges(@NotNull Graph graph) {
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.isDirected())
                throw new IllegalArgumentException("Graph has nonDirected edges");
        }
    }

    /**
     * <b>Precondition</b>
     *
     * @return true if all edges are directed
     */
    @NotNull
    public static Boolean hasNegativeWeights(@NotNull Graph graph) {
        for (Edge edge : graph.getEachEdge()) {
            if ((Double.valueOf(edge.getAttribute("weight").toString())) < 0.0)
                return true;
        }
        return false;
    }

    /**
     * <b>Precondition</b>
     *
     * @throws IllegalArgumentException if edge with negative weight gets detected
     */
    public static void noNegativeWeights(@NotNull Graph graph) throws IllegalArgumentException {
        for (Edge edge : graph.getEachEdge()) {
            if ((Double.valueOf(edge.getAttribute("weight").toString())) < 0.0)
                throw new IllegalArgumentException("Graph has edge with negative weight!");
        }
    }
}
