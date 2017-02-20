package algorithms.optimal_ways;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Finds shortest ways of an graph with weights. <br />
 * Every edge must have the attribute "weight"!
 *
 * @see Graph
 */
public class ShortestWay {
    /**
     * Holds strategy.
     */
    private ShortestWayStrategy strategy;

    /**
     * Constructor with selectable strategy.
     *
     * @param strategy not null.
     */
    public ShortestWay(@NotNull ShortestWayStrategy strategy) {
        this.strategy = checkNotNull(strategy, "strategy has to be not null!");
    }

    /**
     * Executes chosen strategy.
     *
     * @param graph  not null.
     * @param source not null.
     * @param target not null.
     * @return Shortest Path
     */
    public List<Node> executeStrategy(@NotNull Graph graph,
                                      @NotNull Node source,
                                      @NotNull Node target) {
        checkNotNull(graph, "graph has to be not null!");
        checkNotNull(source, "source has to be not null!");
        checkNotNull(target, "target has to be not null!");

        this.strategy.init(graph, source, target);
        this.strategy.compute();
        return this.strategy.getShortestPath();
    }

    public List<Node> getShortestPath() {
        return this.strategy.getShortestPath();
    }

}
