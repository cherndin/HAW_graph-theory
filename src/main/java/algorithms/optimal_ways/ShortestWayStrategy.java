package algorithms.optimal_ways;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Algorithms for shortest way in a graph with weights.
 */
public interface ShortestWayStrategy {

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph  The graph this algorithm is using. not null.
     * @param source not null.
     * @param target not null.
     */
    void init(@NotNull final Graph graph,
              @NotNull final Node source,
              @NotNull final Node target) throws IllegalArgumentException;

    /**
     * Run the algorithm. The {@link #init(Graph, Node, Node)} method has to be called
     * before computing.
     *
     * @see #init(Graph, Node, Node)
     */
    void compute();

    /**
     * Returns the shortest path from source target.
     *
     * @return the shortest path from source target.
     * @see #init(Graph, Node, Node)
     */
    List<Node> getShortestPath();

}
