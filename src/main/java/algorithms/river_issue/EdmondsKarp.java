package algorithms.river_issue;

import algorithms.BreadthFirstSearch;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class EdmondsKarp extends FordFulkerson implements Algorithm {
    private static final Logger LOG = Logger.getLogger(EdmondsKarp.class);
    private LinkedList<Node> path = new LinkedList<>();
    /**
     * Returns marked but not inspected node
     *
     * @return marked but not inspected node
     */

    @NotNull
    Node getMarkedButNotInspected() {
        if (path.isEmpty()) { // neuen pfad suchen
            BreadthFirstSearch bfs = new BreadthFirstSearch();
            Graph residual = residualNetwork();
            bfs.init(residual);
            bfs.setSourceAndTarget(residual.getNode(source.getId()), residual.getNode(sink.getId()));
            bfs.compute();
            path.addAll(bfs.getShortestPath());
            LOG.debug("New path created: " + path.toString());
        }
        Node n = path.getFirst();
        path.removeFirst();
        LOG.debug("Next node is " + n + ". rest is: " + path.toString());
        return graph.getNode(n.getId());
    }

    // === MAIN ===
    public static void main(String[] args) throws Exception {
        // Von Folien
//        Graph test = new SingleGraph("test");
//        test.addNode("q");
//        test.addNode("v1");
//        test.addNode("v2");
//        test.addNode("v3");
//        test.addNode("v5");
//        test.addNode("s");
//        test.addEdge("qv5", "q", "v5", true).addAttribute("capacity", 1.0);
//        test.addEdge("qv1", "q", "v1", true).addAttribute("capacity", 5.0);
//        test.addEdge("qv2", "q", "v2", true).addAttribute("capacity", 4.0);
//        test.addEdge("v2v3", "v2", "v3", true).addAttribute("capacity", 2.0);
//        test.addEdge("v1v3", "v1", "v3", true).addAttribute("capacity", 1.0);
//        test.addEdge("v3s", "v3", "s", true).addAttribute("capacity", 3.0);
//        test.addEdge("v1s", "v1", "s", true).addAttribute("capacity", 3.0);
//        test.addEdge("v1v5", "v1", "v5", true).addAttribute("capacity", 1.0);
//        test.addEdge("v5s", "v5", "s", true).addAttribute("capacity", 3.0);
//
//        FordFulkerson ford = new FordFulkerson();
//        ford.init(test);
//        ford.setSourceAndTarget(test.getNode("q"), test.getNode("s"));
//        ford.compute();

        // https://de.wikipedia.org/wiki/Max-Flow-Min-Cut-Theorem
        Graph maxFminCGraph = new SingleGraph("maxFminCGraph");
        maxFminCGraph.addNode("O");
        maxFminCGraph.addNode("P");
        maxFminCGraph.addNode("Q");
        maxFminCGraph.addNode("R");
        maxFminCGraph.addNode("S");
        maxFminCGraph.addNode("T");

        maxFminCGraph.addEdge("SO", "S", "O", true).addAttribute("capacity", 3.0);
        maxFminCGraph.addEdge("SP", "S", "P", true).addAttribute("capacity", 3.0);
        maxFminCGraph.addEdge("OP", "O", "P", true).addAttribute("capacity", 2.0);
        maxFminCGraph.addEdge("OQ", "O", "Q", true).addAttribute("capacity", 3.0);
        maxFminCGraph.addEdge("PR", "P", "R", true).addAttribute("capacity", 2.0);
        maxFminCGraph.addEdge("QR", "Q", "R", true).addAttribute("capacity", 4.0);
        maxFminCGraph.addEdge("QT", "Q", "T", true).addAttribute("capacity", 2.0);
        maxFminCGraph.addEdge("RT", "R", "T", true).addAttribute("capacity", 3.0);

        EdmondsKarp edmonds = new EdmondsKarp();
        edmonds.init(maxFminCGraph);
        edmonds.setSourceAndTarget(maxFminCGraph.getNode("S"), maxFminCGraph.getNode("T"));
        edmonds.compute();
    }

}
