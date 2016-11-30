package algorithms.river_issue;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class FordFulkersonTest {
    private Graph testGraph, maxFminCGraph, maxFminCGraphResidual;
    private Set<Edge> cutFromTestGraph, cutFromMaxFminCGraph;

    @Before
    public void setUp() throws Exception {
        cutFromTestGraph = new HashSet<Edge>();
        cutFromMaxFminCGraph = new HashSet<Edge>();
        // https://www.youtube.com/watch?v=Om4j8C6w_SU
        testGraph = new SingleGraph("testGraph");
        testGraph.addNode("S");
        testGraph.addNode("A");
        testGraph.addNode("B");
        testGraph.addNode("C");
        testGraph.addNode("D");
        testGraph.addNode("E");
        testGraph.addNode("F");
        testGraph.addNode("T");

        testGraph.addEdge("SA", "S", "A", true).addAttribute("capacity", 10.0);
        testGraph.addEdge("SB", "S", "B", true).addAttribute("capacity", 5.0);
        testGraph.addEdge("SC", "S", "C", true).addAttribute("capacity", 15.0);
        testGraph.addEdge("AD", "A", "D", true).addAttribute("capacity", 9.0);
        testGraph.addEdge("AE", "A", "E", true).addAttribute("capacity", 15.0);
        testGraph.addEdge("AB", "A", "B", true).addAttribute("capacity", 4.0);
        testGraph.addEdge("BE", "B", "E", true).addAttribute("capacity", 8.0);
        testGraph.addEdge("BC", "B", "C", true).addAttribute("capacity", 4.0);
        testGraph.addEdge("CF", "C", "F", true).addAttribute("capacity", 16.0);
        testGraph.addEdge("DE", "D", "E", true).addAttribute("capacity", 15.0);
        testGraph.addEdge("DT", "D", "T", true).addAttribute("capacity", 10.0);
        testGraph.addEdge("ET", "E", "T", true).addAttribute("capacity", 10.0);
        testGraph.addEdge("EF", "E", "F", true).addAttribute("capacity", 15.0);
        testGraph.addEdge("FB", "F", "B", true).addAttribute("capacity", 6.0);
        testGraph.addEdge("FT", "F", "T", true).addAttribute("capacity", 10.0);

        cutFromTestGraph.add(testGraph.getEdge(0));
        cutFromTestGraph.add(testGraph.getEdge(5));
        cutFromTestGraph.add(testGraph.getEdge(6));
        cutFromTestGraph.add(testGraph.getEdge(12));
        cutFromTestGraph.add(testGraph.getEdge(14));

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

        cutFromMaxFminCGraph.add(maxFminCGraph.getEdge("QT"));
        cutFromMaxFminCGraph.add(maxFminCGraph.getEdge("RT"));

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
        FordFulkerson fordTestGraph = new FordFulkerson();
        fordTestGraph.init(testGraph);
        fordTestGraph.setSourceAndTarget(testGraph.getNode("S"), testGraph.getNode("T"));
        fordTestGraph.compute();

        assertTrue(cutFromTestGraph.equals(fordTestGraph.maxFlowMinCut));
    }


    @Test
    public void compare_CutTest() throws Exception {
        FordFulkerson fordMaxFminC = new FordFulkerson();
        fordMaxFminC.init(maxFminCGraph);
        fordMaxFminC.setSourceAndTarget(testGraph.getNode("S"), testGraph.getNode("T"));
        fordMaxFminC.compute();

        assertTrue(cutFromMaxFminCGraph.equals(fordMaxFminC.maxFlowMinCut));
    }



}