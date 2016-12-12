package algorithms.river_issue;

import helper.GraphGenerator;
import helper.IOGraph;
import helper.StopWatch;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.graphstream.graph.Graph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static helper.IOGraph.fromFile;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by MattX7 on 25.11.2016.
 * 1.) Testen Sie f¨ur Graph4 dabei Ford und Fulkerson sowie Edmonds und Karp und geben Sie den maximalen Fluss, sowie die Laufzeit der beiden Algorithmen an.
 * 2.) Implementierung eines Netzwerks (gerichteter, gewichteter Graph mit Quelle und Senke) BigNet Gruppe TeamNr mit 50 Knoten und etwa 800 Kanten. Beschreiben Sie die Konstruktion von BigNet Gruppe TeamNr
 * und erl¨autern sie, inwiefern die Konstruktion ein Netzwerk liefert.
 * 3.) Lassen Sie bitte beide Algorithmen auf dem Netzwerk BigNet Gruppe TeamNr, den maximalen Fluss berechnen und vergleichen diese.
 * 4.) Benutzen Sie bitte unterschiedlich große Big-Net-Graphen mit 800 Knoten und 300.000 Kanten und mit 2.500 Knoten und 2.000.000 Kanten. Und lassen Sie bitte Ihre die Algorithmen jeweils 100 durchlaufen.
 */
public class EdmondsKarpVSFordFulkersonTest {
    private Graph graph4;
    private final Logger LOG = Logger.getLogger(EdmondsKarpVSFordFulkersonTest.class);

    @BeforeClass
    public static void closeLogger() throws Exception {
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
                logger.setLevel(Level.OFF);
        }
        Logger.getLogger(EdmondsKarpVSFordFulkersonTest.class).setLevel(Level.DEBUG);
    }

    @Before
    public void setUp() throws Exception {
        IOGraph.attributeKeyValue = "capacity";
        graph4 = fromFile("graph4", new File("src/main/resources/input/BspGraph/graph04.gka"));
        FordFulkerson.preview = false;
        EdmondsKarp.preview = true;

    }

    @Test(expected = IllegalArgumentException.class)
    public void graph4Test() throws Exception {
        StopWatch stopWatch = new StopWatch();
        FordFulkerson ford = new FordFulkerson();
        ford.init(graph4);
        ford.setSourceAndTarget(graph4.getNode("q"), graph4.getNode("s"));
        ford.compute();
        stopWatch.stop();
        System.out.println(stopWatch.getActualTimeString());
        System.out.println(stopWatch.getEndTimeString());

        StopWatch stopWatch2 = new StopWatch();
        EdmondsKarp edmond = new EdmondsKarp();
        edmond.init(graph4);
        edmond.setSourceAndTarget(graph4.getNode("q"), graph4.getNode("s"));
        edmond.compute();
        stopWatch2.stop();
        System.out.println(stopWatch2.getActualTimeString());
        System.out.println(stopWatch2.getEndTimeString());

    }

    @Test
    public void smallNetwork() throws Exception {
        // Netzwerk 50 Knoten und 800 Kanten
        Graph big = GraphGenerator.createGritNetworkGraph(50);
        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();

        edmond.init(big);
        ford.init(big);

        edmond.compute();
        ford.compute();

        assertTrue(edmond.maxFlow == 2);
        assertTrue(ford.maxFlow == 2);
//        long endTime = edmond.stopWatch.getEndTime();
//        long actualTime = ford.stopWatch.getEndTime();
//        assertTrue(endTime < actualTime);
    }


    @Test
    public void bigNetwork() throws Exception {
        // MegafuckingnetworkGraph 100x run
        List<Long> fordRuntimes = new ArrayList<>();
        List<Long> edmondRuntimes = new ArrayList<>();
        Graph big = GraphGenerator.createGritNetworkGraph(800);
        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();

        int rounds = 10;
        for (int i = 0; i <= rounds; i++) {
            ford.init(big);
            edmond.init(big);
            ford.compute();
            fordRuntimes.add(ford.stopWatch.getEndTime());
            edmond.compute();
            edmondRuntimes.add(edmond.stopWatch.getEndTime());
        }

        LOG.debug(String.format("Min: %s/%s (FordFulkerson/EdmondsKarp)", Collections.min(fordRuntimes), Collections.min(edmondRuntimes)));
        LOG.debug(String.format("AVG: %s/%s (FordFulkerson/EdmondsKarp)", (fordRuntimes.stream().reduce(0l, (x, y) -> x + y)) / rounds, (edmondRuntimes.stream().reduce(0l, (x, y) -> x + y)) / rounds));
        LOG.debug(String.format("Max: %s/%s (FordFulkerson/EdmondsKarp)", Collections.max(fordRuntimes), Collections.max(edmondRuntimes)));
    }

    @Ignore
    @Test
    public void superBigNetwork() throws Exception {
        // SupermegafuckingnetworkGraph 100x run
        List<Long> fordRuntimes = new ArrayList<>();
        List<Long> edmondRuntimes = new ArrayList<>();
        Graph big = GraphGenerator.createGritNetworkGraph(2500);
        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();

        ford.init(big);
        edmond.init(big);

        int rounds = 10;
        for (int i = 0; i <= rounds; i++) {
            ford.init(big);
            edmond.init(big);
            ford.compute();
            fordRuntimes.add(ford.stopWatch.getEndTime());
            edmond.compute();
            edmondRuntimes.add(edmond.stopWatch.getEndTime());
        }

        LOG.debug(String.format("Min: %s/%s (FordFulkerson/EdmondsKarp)", Collections.min(fordRuntimes), Collections.min(edmondRuntimes)));
        LOG.debug(String.format("AVG: %s/%s (FordFulkerson/EdmondsKarp)", (fordRuntimes.stream().reduce(0l, (x, y) -> x + y)) / rounds, (edmondRuntimes.stream().reduce(0l, (x, y) -> x + y)) / rounds));
        LOG.debug(String.format("Max: %s/%s (FordFulkerson/EdmondsKarp)", Collections.max(fordRuntimes), Collections.max(edmondRuntimes)));

    }

}
