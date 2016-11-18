package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Neak on 18.11.2016.
 */
public class FloydWarshallTest {
    private Graph graph;

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
        graph.addEdge("v1v2", "v1", "v2");
        graph.addEdge("v1v6", "v1", "v6");
        graph.addEdge("v2v3", "v2", "v3");
        graph.addEdge("v2v5", "v2", "v5");
        graph.addEdge("v2v6", "v2", "v6");
        graph.addEdge("v3v6", "v3", "v6");
        graph.addEdge("v3v5", "v3", "v5");
        graph.addEdge("v3v4", "v3", "v4");
        graph.addEdge("v5v4", "v5", "v4");
        graph.addEdge("v5v6", "v5", "v6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        // TODO INIT UND COMPUTE AUSFÜHREN STEPS UND SHORTESTPATH ANGUCKEN
        FloydWarshall floyd = new FloydWarshall();
        floyd.init(graph);
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.compute();
    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        graph.getEdge("v1v2").addAttribute("weight", 1.0);
        graph.getEdge("v1v6").addAttribute("weight", 3.0);
        graph.getEdge("v2v3").addAttribute("weight", 5.0);
        graph.getEdge("v2v6").addAttribute("weight", 2.0);
        graph.getEdge("v2v5").addAttribute("weight", 3.0);

        graph.getEdge("v3v6").addAttribute("weight", 2.0);
        graph.getEdge("v3v5").addAttribute("weight", 2.0);
        graph.getEdge("v3v4").addAttribute("weight", 1.0);
        graph.getEdge("v5v4").addAttribute("weight", 3.0);
        graph.getEdge("v5v6").addAttribute("weight", 1.0);
        floyd.init(graph);
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.compute();

        assertEquals(new Double(6), floyd.distance);
    }

    @Test
    public void bigGraphTest() throws Exception {
        int edges = 1000;   // TODO 100 Knoten und etwa 2500 Kanten.
                            // TODO Lassen Sie bitte beide Algorithmen auf dem Graphen BIG, die k¨urzestenWege berechnen und vergleichen diese.
        Graph bigGraph = new SingleGraph("bigGraph");
        bigGraph.addNode("0");
        for (int i = 1; i <= edges; i++) {
            bigGraph.addNode("" + i);
            bigGraph.addEdge("" + (i - 1) + i, "" + (i - 1), "" + i).addAttribute("weight", 1.0);
        }
        FloydWarshall floyd = new FloydWarshall();
        floyd.init(bigGraph);
        floyd.setSourceAndTarget(bigGraph.getNode("0"), bigGraph.getNode("" + edges));
        floyd.compute();
        assertEquals(new Double(edges), floyd.distance);
    }
}