package algorithms.river_issue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class FordFulkersonTest {
    private Graph test;
    @Before
    public void setUp() throws Exception {
        //https://www.youtube.com/watch?v=Om4j8C6w_SU
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

    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        FordFulkerson ford = new FordFulkerson();
        FordFulkerson
        ford.init(test);
        ford.setSourceAndTarget(test.getNode("S"), test.getNode("T"));
        ford.compute();
        assertEquals(28, ford.maxFlow(test));
//        System.out.println("Steps: " + floyd.getShortestPath());
    }

}