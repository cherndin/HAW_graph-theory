package algorithms.river_issue;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class EdmondsKarp extends FordFulkerson implements Algorithm {
    private static Logger logger = Logger.getLogger(EdmondsKarp.class);
    static boolean preview = true;

    /**
     * Returns marked but not inspected node
     *
     * @return marked but not inspected node
     */
    @NotNull
    Node getMarkedButNotInspected() {
        for (Node node : nodes) {
            if (isMarked(node) && !inspected[indexOf(node)]) {
                return node;
            }
        }
        throw new IllegalArgumentException("no node found");
    }
}
