package algorithms;

import algorithms.optimal_ways.ShortestWayStrategy;
import algorithms.river_issue.MaxFlowStrategy;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Facade for graph algorithms and utility.
 */
public interface Facade {

// TODO extra Klasse für präsentation die einen graph mit markierungen konsumiert.

    /**
     * Returns the shortest path in a graph with a selectable algorithm.
     *
     * @param strategy Algorithms for shortest way in a graph with weights. Not null.
     * @param graph    Not null.
     * @param source   Not null.
     * @param target   Not null.
     * @return shortest path from source to target.
     */
    List<Node> findShortestWay(@NotNull final ShortestWayStrategy strategy,
                               @NotNull final Graph graph,
                               @NotNull final Node source,
                               @NotNull final Node target);


    /**
     * Marks every edge with the max flow.
     *
     * @param strategy Algorithms for max flow in a graph with capacities. Not null.
     * @param graph    Not null.
     * @param source   Not null.
     * @param sink     Not null.
     * @return graph for inline use.
     */
    Graph applyMaxFlow(@NotNull final MaxFlowStrategy strategy,
                       @NotNull final Graph graph,
                       @NotNull final Node source,
                       @NotNull final Node sink);


    /**
     * Saves a graph into a gka-file.
     *
     * @param graph Not null.
     * @see algorithms.utility.IOGraph#save(Graph)
     */
    void saveGraph(@NotNull final Graph graph) throws IOException;

    /**
     * Reads a graph from a gka-file.
     *
     * @param name       Name of the new graph
     * @param fileToRead File to read
     * @return the new created graph from file
     * @throws FileNotFoundException file could not be found
     */
    Graph graphFromFile(@NotNull final String name,
                        @NotNull final File fileToRead) throws FileNotFoundException;

    /**
     * Adds stylesheets for presentation.
     *
     * @param graph Not null.
     * @return graph for inline use.
     */
    Graph styleGraphForDisplay(@NotNull final Graph graph);
}
