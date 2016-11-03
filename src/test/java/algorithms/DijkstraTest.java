package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Neak on 03.11.2016.
 */
public class DijkstraTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void compute() throws Exception {

        // TODO INIT UND COMPUTE AUSFÜHREN STEPS UND SHORTESTPATH ANGUCKEN

        //Ein Pentagram Graph
        Graph penta = new SingleGraph("penta");
        penta.addNode("a");
        penta.addNode("b");
        penta.addNode("c");
        penta.addNode("d");
        penta.addNode("e");
        penta.addEdge("ab", "a", "b");
        penta.addEdge("ac", "a", "c");
        penta.addEdge("de", "d", "e");
        penta.addEdge("ec", "e", "c");
        penta.addEdge("bd", "b", "d");

        Dijkstra.preview = false;

        Dijkstra dick = new Dijkstra();
        dick.init(penta);
        dick.setSourceAndTarget(penta.getNode("a"),penta.getNode("c"));
        dick.compute();

        assertEquals(2, dick.steps);
        assertEquals("[c, b, a]", dick.getShortestPath().toString());

    }

}