package algorithms.river_issue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fills a graph with max flow <br />
 * Every edge must have the attribute "capacity"!
 *
 * @see Graph
 */
public class MaxFlow {
    /**
     * Holds strategy.
     */
    private MaxFlowStrategy strategy;

    /**
     * Constructor with selectable strategy.
     *
     * @param strategy not null.
     */
    public MaxFlow(@NotNull final MaxFlowStrategy strategy) {
        this.strategy = checkNotNull(strategy, "strategy has to be not null!");
    }

    /**
     * Executes chosen strategy.
     *
     * @param graph  not null.
     * @param source not null.
     * @param target not null.
     * @return graph for inline use;
     */
    public Graph executeStrategy(@NotNull final Graph graph,
                                 @NotNull final Node source,
                                 @NotNull final Node target) {
        checkNotNull(graph, "graph has to be not null!");
        checkNotNull(source, "source has to be not null!");
        checkNotNull(target, "target has to be not null!");

        this.strategy.init(graph, source, target);
        this.strategy.compute();
        return graph;
    }
}
