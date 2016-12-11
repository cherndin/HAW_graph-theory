package algorithms.river_issue;

import algorithms.BreadthFirstSearch;
import helper.StopWatch;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by MattX7 on 25.11.2016.
 *
 */
public class EdmondsKarp extends FordFulkerson implements Algorithm {
    private static final Logger LOG = Logger.getLogger(EdmondsKarp.class);


    /**
     * Run the algorithm. The {@link #init(Graph)} method has to be called
     * before computing.
     *
     * @see #init(Graph)
     */
    public void compute() {
        stopWatch = new StopWatch();
        LOG.debug("==== (2) compute ====");
        if (!computable)
            throw new IllegalStateException("Do init(Graph) before compute()");
        /* (2) Inspektion und Markierung */
        List<String> bfs = bfs(); // aktueller Pfad angegeben durch ids der Nodes

        LOG.debug("Starting with path:" + bfs);
        LOG.debug(">>> Starting inspection >>>");


        for (int k = 0; k < bfs.size() - 1; k++) {
            Node v_i = graph.getNode(bfs.get(k));
            Node v_j = graph.getNode(bfs.get(k + 1));
            int i = indexOf(v_i);
            int j = indexOf(v_j);
            Edge e_ij = v_i.getEdgeBetween(v_j);


            if (e_ij.getTargetNode() == v_j) { // OUTPUT
                if (!isMarked(v_j) && flow[i][j] < capacity[i][j]) { // nur unmarkierte Knoten markieren V_j mit f(E_ij) < c(E_ij)
                    LOG.debug(String.format("Found unmarked node %s (v_j) from %s with f(E_ij)=%f < c(E_ij))=%f", v_j.getId(), v_i.getId(), flow[i][j], capacity[i][j]));
                    // markiere VERTEX_j
                    mark(j, v_i, true, Math.min(delta[i], capacity[i][j] - flow[i][j]));

                }
            } else if (e_ij.getTargetNode() == v_i) { // INPUT e_ij is actually e_ji
                if (!isMarked(v_j) && flow[i][j] > 0) { // nur unmarkierter Knoten V_j mit f(E_ji) > 0
                    LOG.debug(String.format("Found unmarked node %s (v_j) from %s with f(E_ji)=%f > 0", v_j.getId(), v_i.getId(), flow[i][j]));
                    // markiere V_j
                    mark(j, v_j, false, Math.min(delta[i], flow[i][j]));

                }
            }


            inspected[indexOf(v_j)] = true;
        }

        LOG.debug("<<< Inspection done <<<");

        if (bfs().isEmpty()) {
            compute_Cut(); // (4) Es gibt keinen vergrößernden Weg
        } else if (isMarked(sink)) {
            compute_AugmentedPath(); // (3) Vergrößerung der Flussstärke
            compute();
        } else {
            compute(); // (2) Inspektion und Markierung
        }
    }


    /**
     * Returns marked but not inspected node
     *
     * @return marked but not inspected node
     */
    @NotNull
    private List<String> bfs() {
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        Graph residual = residualNetwork();
        bfs.init(residual);
        bfs.setSourceAndTarget(residual.getNode(source.getId()), residual.getNode(sink.getId()));
        bfs.compute();
        List<Node> shortestPath = bfs.getShortestPath();
        return shortestPath.stream().map(Element::getId).collect(Collectors.toList());
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
