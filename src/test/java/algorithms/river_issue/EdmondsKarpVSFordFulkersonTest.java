package algorithms.river_issue;

import helper.GraphGenerator;
import helper.IOGraph;
import helper.StopWatch;
import org.graphstream.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static helper.IOGraph.fromFile;

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

    @Before
    public void setUp() throws Exception {
        IOGraph.attributeKeyValue = "capacity";
        graph4 = fromFile("graph4", new File("src/main/resources/input/BspGraph/graph04.gka"));


    }
    // TODO Graph4

    @Test(expected = IllegalArgumentException.class)
    public void graph4Test() throws Exception {
        StopWatch stopWatch = new StopWatch();
        FordFulkerson ford = new FordFulkerson();
        ford.init(graph4);
        ford.setSourceAndTarget(graph4.getNode("q"), graph4.getNode("s"));
        ford.compute();
        stopWatch.stop();
        System.out.println(stopWatch.getActualTime());
        System.out.println(stopWatch.getEndTime());

        StopWatch stopWatch2 = new StopWatch();
        EdmondsKarp edmond = new EdmondsKarp();
        edmond.init(graph4);
        edmond.setSourceAndTarget(graph4.getNode("q"), graph4.getNode("s"));
        edmond.compute();
        stopWatch2.stop();
        System.out.println(stopWatch2.getActualTime());
        System.out.println(stopWatch2.getEndTime());

    }
    // TODO Vergleich des Netzwerkes

    @Test
    public void smallNetwork() throws Exception {
        // TODO Netzwerk 50 Knoten und 800 Kanten

        Graph big = GraphGenerator.createNetworkGraph(50, 800);

        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();


        edmond.init(big);
        ford.init(big);

        edmond.setSourceAndTarget(big.getNode("1"), big.getNode("" + 50));
        ford.setSourceAndTarget(big.getNode("1"), big.getNode("" + 800));

        ford.compute();
        edmond.compute();
    }


    @Test
    public void bigNetwork() throws Exception {
        // TODO MegafuckingnetworkGraph 100x run
        Graph big = GraphGenerator.createNetworkGraph(800, 30000);

        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();


        edmond.init(big);
        ford.init(big);

        edmond.setSourceAndTarget(big.getNode("1"), big.getNode("" + 800));
        ford.setSourceAndTarget(big.getNode("1"), big.getNode("" + 30000));

        for (int i = 0; i <= 100; i++) {
            edmond.compute();
            ford.compute();
        }
    }

    @Test
    public void superBigNetwork() throws Exception {
        // TODO SupermegafuckingnetworkGraph 100x run
        Graph big = GraphGenerator.createNetworkGraph(2500, 2000000);

        FordFulkerson ford = new FordFulkerson();
        EdmondsKarp edmond = new EdmondsKarp();


        edmond.init(big);
        ford.init(big);

        edmond.setSourceAndTarget(big.getNode("1"), big.getNode("" + 2500));
        ford.setSourceAndTarget(big.getNode("1"), big.getNode("" + 2000000));

        for (int i = 0; i <= 100; i++) {
            edmond.compute();
            ford.compute();
        }

    }

}
