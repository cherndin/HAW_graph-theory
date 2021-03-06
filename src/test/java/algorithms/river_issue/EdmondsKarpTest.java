package algorithms.river_issue;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Tests for {@link EdmondsKarp}
 */
public class EdmondsKarpTest {
    private Graph graphFromYouTube, graphFromWiki, negGraph, triangleGraph;

    @BeforeClass
    public static void closeLogger() throws Exception {
        final List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (final Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }
        Logger.getLogger(EdmondsKarp.class).setLevel(Level.DEBUG);
    }

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

        negGraph = new SingleGraph("negGraph");
        negGraph.addNode("S");
        negGraph.addNode("v2");
        negGraph.addNode("v3");
        negGraph.addNode("v4");
        negGraph.addNode("T");

        negGraph.addEdge("Sv2", "S", "v2", true).addAttribute("capacity", -3.0);
        negGraph.addEdge("v2v3", "v2", "v3", true).addAttribute("capacity", -11.0);
        negGraph.addEdge("v3v4", "v3", "v4", true).addAttribute("capacity", 4.0);
        negGraph.addEdge("v4v2", "v4", "v2", true).addAttribute("capacity", 3.0);
        negGraph.addEdge("Tv4", "T", "v4", true).addAttribute("capacity", -2.0);

        triangleGraph = new SingleGraph("triangleGraph");
        triangleGraph.addNode("S");
        triangleGraph.addNode("v2");
        triangleGraph.addNode("T");

        triangleGraph.addEdge("Sv2", "S", "v2", true).addAttribute("capacity", -3.0);
        triangleGraph.addEdge("v2T", "v2", "T", true).addAttribute("capacity", -11.0);
        triangleGraph.addEdge("TS", "T", "S", true).addAttribute("capacity", 4.0);



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
        final EdmondsKarp edmondsWiki = new EdmondsKarp();
        edmondsWiki.init(graphFromWiki, graphFromWiki.getNode("S"), graphFromWiki.getNode("T"));
        edmondsWiki.compute();

        assertTrue(edmondsWiki.maxFlow == 5);
    }


    @Test
    public void testWithMediumGraph() throws Exception {
        final EdmondsKarp edmondsYT = new EdmondsKarp();
        edmondsYT.init(graphFromYouTube, graphFromYouTube.getNode("S"), graphFromYouTube.getNode("T"));
        edmondsYT.compute();

        assertTrue(edmondsYT.maxFlow == 28);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegGraph() throws Exception {
        final EdmondsKarp edmonds = new EdmondsKarp();
        edmonds.init(negGraph, negGraph.getNode("S"), negGraph.getNode("T"));
        edmonds.compute();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriangleGraph() throws Exception {
        final EdmondsKarp edmonds = new EdmondsKarp();
        edmonds.init(triangleGraph, triangleGraph.getNode("S"), triangleGraph.getNode("T"));
        edmonds.compute();

    }
}