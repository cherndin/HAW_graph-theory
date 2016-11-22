package algorithms;

import helper.BigGraph;
import org.graphstream.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static helper.IOGraph.fromFile;
import static org.junit.Assert.assertEquals;

/**
 * Created by abw286 on 21.11.2016.
 */
public class DijkstraVSFloydTest {
    // TODO Big Graph auslagern und damit algos vergleichen; hits vergleichen
    private Graph graph3;

    @Before
    public void setUp() throws Exception {
        graph3 = fromFile("graph3", new File("src/main/resources/input/BspGraph/graph03.gka"));
    }

    @Test
    public void graph03test() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        Dijkstra dijk = new Dijkstra();

        Dijkstra.preview = false;
        FloydWarshall.preview = false;

        dijk.init(graph3);
        floyd.init(graph3);

        dijk.setSourceAndTarget(graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
        floyd.setSourceAndTarget(graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));

        dijk.compute();
        floyd.compute();

        assertEquals(Double.valueOf(170.0), dijk.distance); //TODO not same as Floyd's
        assertEquals(Double.valueOf(170.0), floyd.distance);
        assertEquals(floyd.distance, dijk.distance);

        System.out.println("Hits: " + dijk.hits);
        System.out.println("Hits: " + floyd.hits);
    }

    @Test
    public void bigGraphTest() throws Exception {
        BigGraph bigGraph = new BigGraph(100, 2500);
        Graph big = bigGraph.createBigGraph();

        Dijkstra dijk = new Dijkstra();
        FloydWarshall.preview = false;
        dijk.init(big);
        dijk.setSourceAndTarget(big.getNode("1"), big.getNode("" + bigGraph.nodes));
        dijk.compute();
        assertEquals(2500, bigGraph.edgeCount);
        assertEquals(Double.valueOf(1), dijk.distance);
        System.out.println("Hits: " + dijk.hits);
    }

}
