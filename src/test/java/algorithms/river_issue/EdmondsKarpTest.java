package algorithms.river_issue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class EdmondsKarpTest {
    private Graph graphFromYouTube, graphFromWiki;

    // TODO test mit negativen Kanten

    @Before
    public void setUp() throws Exception {
        // https://www.youtube.com/watch?v=Om4j8C6w_SU
        graphFromYouTube = new SingleGraph("graphFromYouTube");
        graphFromYouTube.addNode("S");
        graphFromYouTube.addNode("A");
        graphFromYouTube.addNode("B");
        graphFromYouTube.addNode("C");
        graphFromYouTube.addNode("D");
        graphFromYouTube.addNode("E");
        graphFromYouTube.addNode("F");
        graphFromYouTube.addNode("T");

        graphFromYouTube.addEdge("SA", "S", "A", true).addAttribute("capacity", 10.0);
        graphFromYouTube.addEdge("SB", "S", "B", true).addAttribute("capacity", 5.0);
        graphFromYouTube.addEdge("SC", "S", "C", true).addAttribute("capacity", 15.0);
        graphFromYouTube.addEdge("AD", "A", "D", true).addAttribute("capacity", 9.0);
        graphFromYouTube.addEdge("AE", "A", "E", true).addAttribute("capacity", 15.0);
        graphFromYouTube.addEdge("AB", "A", "B", true).addAttribute("capacity", 4.0);
        graphFromYouTube.addEdge("BE", "B", "E", true).addAttribute("capacity", 8.0);
        graphFromYouTube.addEdge("BC", "B", "C", true).addAttribute("capacity", 4.0);
        graphFromYouTube.addEdge("CF", "C", "F", true).addAttribute("capacity", 16.0);
        graphFromYouTube.addEdge("DE", "D", "E", true).addAttribute("capacity", 15.0);
        graphFromYouTube.addEdge("DT", "D", "T", true).addAttribute("capacity", 10.0);
        graphFromYouTube.addEdge("ET", "E", "T", true).addAttribute("capacity", 10.0);
        graphFromYouTube.addEdge("EF", "E", "F", true).addAttribute("capacity", 15.0);
        graphFromYouTube.addEdge("FB", "F", "B", true).addAttribute("capacity", 6.0);
        graphFromYouTube.addEdge("FT", "F", "T", true).addAttribute("capacity", 10.0);

        // https://de.wikipedia.org/wiki/Max-Flow-Min-Cut-Theorem
        graphFromWiki = new SingleGraph("graphFromWiki");
        graphFromWiki.addNode("O");
        graphFromWiki.addNode("P");
        graphFromWiki.addNode("Q");
        graphFromWiki.addNode("R");
        graphFromWiki.addNode("S");
        graphFromWiki.addNode("T");

        graphFromWiki.addEdge("SO", "S", "O", true).addAttribute("capacity", 3.0);
        graphFromWiki.addEdge("SP", "S", "P", true).addAttribute("capacity", 3.0);
        graphFromWiki.addEdge("OP", "O", "P", true).addAttribute("capacity", 2.0);
        graphFromWiki.addEdge("OQ", "O", "Q", true).addAttribute("capacity", 3.0);
        graphFromWiki.addEdge("PR", "P", "R", true).addAttribute("capacity", 2.0);
        graphFromWiki.addEdge("QR", "Q", "R", true).addAttribute("capacity", 4.0);
        graphFromWiki.addEdge("QT", "Q", "T", true).addAttribute("capacity", 2.0);
        graphFromWiki.addEdge("RT", "R", "T", true).addAttribute("capacity", 3.0);
//          like graphFromWiki but residual
//        maxFminCGraphResidual = new SingleGraph("maxFminCGraphResidual");
//        maxFminCGraphResidual.addNode("O");
//        maxFminCGraphResidual.addNode("P");
//        maxFminCGraphResidual.addNode("Q");
//        maxFminCGraphResidual.addNode("R");
//        maxFminCGraphResidual.addNode("S");
//        maxFminCGraphResidual.addNode("T");
//
//        maxFminCGraphResidual.addEdge("SP", "S", "P", true).addAttribute("capacity", 1.0);
//        maxFminCGraphResidual.addEdge("OS", "O", "S", true).addAttribute("capacity", 3.0);
//        maxFminCGraphResidual.addEdge("OP", "O", "P", true).addAttribute("capacity", 2.0);
//        maxFminCGraphResidual.addEdge("PS", "P", "S", true).addAttribute("capacity", 2.0);
//        maxFminCGraphResidual.addEdge("QO", "Q", "O", true).addAttribute("capacity", 3.0);
//        maxFminCGraphResidual.addEdge("QR", "Q", "R", true).addAttribute("capacity", 3.0);
//        maxFminCGraphResidual.addEdge("RQ", "R", "Q", true).addAttribute("capacity", 1.0);
//        maxFminCGraphResidual.addEdge("RP", "R", "P", true).addAttribute("capacity", 2.0);
//        maxFminCGraphResidual.addEdge("TQ", "T", "Q", true).addAttribute("capacity", 2.0);
//        maxFminCGraphResidual.addEdge("TR", "T", "R", true).addAttribute("capacity", 3.0);

    }

    @Test
    public void testWithSmallGraph() throws Exception {
        EdmondsKarp edmondsWiki = new EdmondsKarp();
        edmondsWiki.init(graphFromWiki);
        edmondsWiki.setSourceAndTarget(graphFromWiki.getNode("S"), graphFromWiki.getNode("T"));
        edmondsWiki.compute();

        assertTrue(edmondsWiki.maxFlow == 5);
    }


    @Test
    public void testWithMediumGraph() throws Exception {
        EdmondsKarp edmondsYT = new EdmondsKarp();
        edmondsYT.init(graphFromYouTube);
        edmondsYT.setSourceAndTarget(graphFromYouTube.getNode("S"), graphFromYouTube.getNode("T"));
        edmondsYT.compute();

        assertTrue(edmondsYT.maxFlow == 28);
    }

}