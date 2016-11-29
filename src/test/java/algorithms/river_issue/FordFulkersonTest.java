package algorithms.river_issue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class FordFulkersonTest {
    private Graph test, maxFminCGraph, maxFminCGraphResidual;

    @Before
    public void setUp() throws Exception {
        // https://www.youtube.com/watch?v=Om4j8C6w_SU
        test = new SingleGraph("test");
        test.addNode("S");
        test.addNode("A");
        test.addNode("B");
        test.addNode("C");
        test.addNode("D");
        test.addNode("E");
        test.addNode("F");
        test.addNode("T");

        test.addEdge("SA", "S", "A", true).addAttribute("capacity", 10.0);
        test.addEdge("SB", "S", "B", true).addAttribute("capacity", 5.0);
        test.addEdge("SC", "S", "C", true).addAttribute("capacity", 15.0);
        test.addEdge("AD", "A", "D", true).addAttribute("capacity", 9.0);
        test.addEdge("AE", "A", "E", true).addAttribute("capacity", 15.0);
        test.addEdge("AB", "A", "B", true).addAttribute("capacity", 4.0);
        test.addEdge("BE", "B", "E", true).addAttribute("capacity", 8.0);
        test.addEdge("BC", "B", "C", true).addAttribute("capacity", 4.0);
        test.addEdge("CF", "C", "F", true).addAttribute("capacity", 16.0);
        test.addEdge("DE", "D", "E", true).addAttribute("capacity", 15.0);
        test.addEdge("DT", "D", "T", true).addAttribute("capacity", 10.0);
        test.addEdge("ET", "E", "T", true).addAttribute("capacity", 10.0);
        test.addEdge("EF", "E", "F", true).addAttribute("capacity", 15.0);
        test.addEdge("FB", "F", "B", true).addAttribute("capacity", 6.0);
        test.addEdge("FT", "F", "T", true).addAttribute("capacity", 10.0);

        // https://de.wikipedia.org/wiki/Max-Flow-Min-Cut-Theorem
        maxFminCGraph = new SingleGraph("maxFminCGraph");
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

        maxFminCGraphResidual = new SingleGraph("maxFminCGraphResidual");
        maxFminCGraphResidual.addNode("O");
        maxFminCGraphResidual.addNode("P");
        maxFminCGraphResidual.addNode("Q");
        maxFminCGraphResidual.addNode("R");
        maxFminCGraphResidual.addNode("S");
        maxFminCGraphResidual.addNode("T");

        maxFminCGraphResidual.addEdge("SP", "S", "P", true).addAttribute("capacity", 1.0);
        maxFminCGraphResidual.addEdge("OS", "O", "S", true).addAttribute("capacity", 3.0);
        maxFminCGraphResidual.addEdge("OP", "O", "P", true).addAttribute("capacity", 2.0);
        maxFminCGraphResidual.addEdge("PS", "P", "S", true).addAttribute("capacity", 2.0);
        maxFminCGraphResidual.addEdge("QO", "Q", "O", true).addAttribute("capacity", 3.0);
        maxFminCGraphResidual.addEdge("QR", "Q", "R", true).addAttribute("capacity", 3.0);
        maxFminCGraphResidual.addEdge("RQ", "R", "Q", true).addAttribute("capacity", 1.0);
        maxFminCGraphResidual.addEdge("RP", "R", "P", true).addAttribute("capacity", 2.0);
        maxFminCGraphResidual.addEdge("TQ", "T", "Q", true).addAttribute("capacity", 2.0);
        maxFminCGraphResidual.addEdge("TR", "T", "R", true).addAttribute("capacity", 3.0);


    }
    @Test
    public void computeSimpleGraphTest() throws Exception {
        FordFulkerson ford = new FordFulkerson();
        ford.init(test);
        ford.setSourceAndTarget(test.getNode("S"), test.getNode("T"));
        ford.compute();
//        assertEquals(28, ford.getMaxFlow(test));
//        System.out.println("Steps: " + floyd.getShortestPath());
    }

    @Ignore
    @Test
    public void compare_CutTest() throws Exception {
        FordFulkerson ford = new FordFulkerson();
        ford.init(maxFminCGraph);
        ford.setSourceAndTarget(test.getNode("S"), test.getNode("T"));
        ford.compute();
        // assertTrue(com.google.common.graph.Graphs.equivalent(ford.residualNetwork(maxFminCGraph),maxFminCGraphResidual));
        // TODO Graphsteam auf Google Graph mappen ODER eigene equivalent funktion schreiben
    }



}