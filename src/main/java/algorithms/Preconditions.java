package algorithms;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class Preconditions {
    /**
     * <b>Precondition</b> Helper Method for init()
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
     * <b>Precondition</b> Helper Method for init()
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
     * <b>Precondition</b> Helper Method for init()
     *
     * @return true if all edges are directed
     */
    @NotNull
    public static Boolean hasNegativeWeights(Graph graph) {
        for (Edge edge : graph.getEachEdge()) {
            if ((Double.valueOf(edge.getAttribute("weight").toString())) < 0.0)
                return true;
        }
        return false;
    }
}
