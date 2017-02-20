package algorithms;

import algorithms.optimal_ways.ShortestWay;
import algorithms.optimal_ways.ShortestWayStrategy;
import algorithms.river_issue.MaxFlow;
import algorithms.river_issue.MaxFlowStrategy;
import algorithms.utility.GraphUtil;
import algorithms.utility.IOGraph;
import com.google.common.base.Preconditions;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Facade implementation for graph algorithms.
 */
public class FacadeImpl implements Facade {

    @Override
    public List<Node> findShortestWay(@NotNull ShortestWayStrategy strategy,
                                      @NotNull Graph graph,
                                      @NotNull Node source,
                                      @NotNull Node target) {
        Preconditions.checkNotNull(strategy, "strategy has to be not null!");
        Preconditions.checkNotNull(graph, "graph has to be not null!");
        Preconditions.checkNotNull(source, "source has to be not null!");
        Preconditions.checkNotNull(target, "target has to be not null!");

        ShortestWay shortestWay = new ShortestWay(strategy);
        shortestWay.executeStrategy(graph, source, target);
        return shortestWay.getShortestPath();
    }

    @Override
    public Graph applyMaxFlow(@NotNull MaxFlowStrategy strategy,
                              @NotNull Graph graph,
                              @NotNull Node source,
                              @NotNull Node sink) {
        Preconditions.checkNotNull(strategy, "strategy has to be not null!");
        Preconditions.checkNotNull(graph, "graph has to be not null!");
        Preconditions.checkNotNull(source, "source has to be not null!");
        Preconditions.checkNotNull(sink, "target has to be not null!");

        MaxFlow maxFlow = new MaxFlow(strategy);
        return maxFlow.executeStrategy(graph, source, sink);
    }

    @Override
    public void saveGraph(@NotNull Graph graph) throws IOException {
        Preconditions.checkNotNull(graph, "graph has to be not null!");

        IOGraph.save(graph);
    }

    @Override
    public Graph graphFromFile(@NotNull String name,
                               @NotNull File fileToRead) throws FileNotFoundException {
        Preconditions.checkNotNull(name, "name has to be not null!");
        Preconditions.checkNotNull(fileToRead, "fileToRead has to be not null!");

        return IOGraph.fromFile(name, fileToRead);
    }

    @Override
    public Graph styleGraphForDisplay(@NotNull Graph graph) {
        Preconditions.checkNotNull(graph, "graph has to be not null!");

        return GraphUtil.buildForDisplay(graph);
    }

}
