package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static helper.IOGraph.fromFile;
import static org.junit.Assert.assertEquals;

/**
 * Created by Neak on 18.11.2016.
 */
public class FloydWarshallTest {
    private Graph graph;
    private Graph graph3;


    // TODO Testen Sie für graph3 in graph3.gka dabei Floyd-Warshall gegen Dijkstra und geben Sie den kürzesten Weg, sowie die Anzahl der Zugriffe auf den Graphen an

    @Before
    public void setUp() throws Exception {
        // Graph aus den Folien
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
        graph = new SingleGraph("graph");
        graph.addNode("v1");
        graph.addNode("v2");
        graph.addNode("v3");
        graph.addNode("v4");
        graph.addNode("v5");
        graph.addNode("v6");
        graph.addEdge("v1v2", "v1", "v2").addAttribute("weight", 1.0);
        graph.addEdge("v1v6", "v1", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v2v3", "v2", "v3").addAttribute("weight", 5.0);
        graph.addEdge("v2v5", "v2", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v2v6", "v2", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v3v6", "v3", "v6").addAttribute("weight", 2.0);
        graph.addEdge("v3v5", "v3", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v3v4", "v3", "v4").addAttribute("weight", 1.0);
        graph.addEdge("v5v4", "v5", "v4").addAttribute("weight", 3.0);
        graph.addEdge("v5v6", "v5", "v6").addAttribute("weight", 1.0);

        graph3 = fromFile("graph3", new File("src/main/resources/output/graph03.gka"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        Graph graph9 = fromFile("graph09", new File("src/main/resources/output/graph09.gka"));
        FloydWarshall floyd = new FloydWarshall();
        floyd.setSourceAndTarget(graph9.getNode("a"), graph9.getNode("d"));
        floyd.init(graph9);
        floyd.compute();
    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.init(graph);
        floyd.compute();
        // TODO anderer Test
        assertEquals(new Double(6), floyd.distance);
    }

    @Test
    public void graph03test() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        floyd.setSourceAndTarget(graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
        floyd.init(graph3);
        floyd.compute();
        assertEquals(new Double(170.0), floyd.distance);
        assertEquals(new Integer(484), floyd.steps);
    }

    @Test
    public void bigGraphTest() throws Exception {
        int edges = 100;   // TODO 100 Knoten und etwa 2500 Kanten.
        // TODO Lassen Sie bitte beide Algorithmen auf dem Graphen BIG, die kürzestenWege berechnen und vergleichen diese.
        Graph bigGraph = new SingleGraph("bigGraph");
        bigGraph.addNode("0");
        for (int i = 1; i <= edges; i++) {
            bigGraph.addNode("" + i);
            bigGraph.addEdge("" + (i - 1) + i, "" + (i - 1), "" + i).addAttribute("weight", 1.0);
        }
        FloydWarshall floyd = new FloydWarshall();
        floyd.setSourceAndTarget(bigGraph.getNode("0"), bigGraph.getNode("" + edges));
        floyd.init(bigGraph);
        floyd.compute();
        assertEquals(new Double(edges), floyd.distance);
        assertEquals(new Integer(150), floyd.steps);
    }
}