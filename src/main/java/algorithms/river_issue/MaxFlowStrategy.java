package algorithms.river_issue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Algorithms for max flow in a graph with capacities.
 */
public interface MaxFlowStrategy {

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph  The graph this algorithm is using. not null.
     * @param source not null.
     * @param sink   not null.
     */
    void init(@NotNull Graph graph,
              @NotNull Node source,
              @NotNull Node sink) throws IllegalArgumentException;

    /**
     * Run the algorithm. The {@link #init(Graph, Node, Node)} method has to be called
     * before computing.
     *
     * @see #init(Graph, Node, Node)
     */
    void compute();
}
