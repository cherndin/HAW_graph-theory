package helper;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static helper.IOGraph.fromFile;
import static org.junit.Assert.assertTrue;

/**
 * Created by MattX7 on 23.10.2016.
 * Edited by Neak on 23.10.2016.
 */
public class IOGraphTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void IOFromGraphTest() throws Exception {
        // Initialize graph
        Graph testGraph = new SingleGraph("testSave");
        testGraph.addNode("1");
        testGraph.addNode("2");
        testGraph.addNode("3");
        testGraph.addNode("4");

        testGraph.addEdge("12", "1", "2");
        testGraph.addEdge("23", "2", "3");
        testGraph.addEdge("34", "3", "4");

        // Save initialized Graph
        IOGraph.save(testGraph, "testSave");
        File file = new File("src/main/resources/output/testSave.gka");
        assertTrue(file.exists());

        // Get saved File
        Graph chooser = fromFile("chooser", file);

        // Compare intitialized File with saved File
        for (Edge testEdge : testGraph.getEachEdge()) {
            Edge chooserEdge = chooser.getEdge(testEdge.getId());
            assertTrue(chooserEdge != null);
            assertTrue(testEdge.getNode0().getId().equals(chooserEdge.getNode0().getId()));
            assertTrue(testEdge.getNode1().getId().equals(chooserEdge.getNode1().getId()));
        }
    }

    // TODO equality of graphs
    @Test
    public void IOFromFileTest() throws Exception {
        Graph graph01 = fromFile("graph01", new File("src/main/resources/input/BspGraph/graph01.gka"));
        Graph graph02 = fromFile("graph02", new File("src/main/resources/input/BspGraph/graph02.gka"));
        Graph graph03 = fromFile("graph03", new File("src/main/resources/input/BspGraph/graph03.gka"));
        Graph graph04 = fromFile("graph04", new File("src/main/resources/input/BspGraph/graph04.gka"));
        Graph graph05 = fromFile("graph05", new File("src/main/resources/input/BspGraph/graph05.gka"));
        Graph graph06 = fromFile("graph06", new File("src/main/resources/input/BspGraph/graph06.gka"));
        Graph graph07 = fromFile("graph07", new File("src/main/resources/input/BspGraph/graph07.gka"));
        Graph graph08 = fromFile("graph08", new File("src/main/resources/input/BspGraph/graph08.gka"));
        Graph graph09 = fromFile("graph09", new File("src/main/resources/input/BspGraph/graph09.gka"));
        Graph graph10 = fromFile("graph10", new File("src/main/resources/input/BspGraph/graph10.gka"));

        IOGraph.save(graph01);
        IOGraph.save(graph02);
        IOGraph.save(graph03);
        IOGraph.save(graph04);
        IOGraph.save(graph05);
        IOGraph.save(graph06);
        IOGraph.save(graph07);
        IOGraph.save(graph08);
        IOGraph.save(graph09);
        IOGraph.save(graph10);

        Graph graph01_2 = fromFile("graph01", new File("src/main/resources/output/graph01.gka"));
        Graph graph02_2 = fromFile("graph02", new File("src/main/resources/output/graph02.gka"));
        Graph graph03_2 = fromFile("graph03", new File("src/main/resources/output/graph03.gka"));
        Graph graph04_2 = fromFile("graph04", new File("src/main/resources/output/graph04.gka"));
        Graph graph05_2 = fromFile("graph05", new File("src/main/resources/output/graph05.gka"));
        Graph graph06_2 = fromFile("graph06", new File("src/main/resources/output/graph06.gka"));
        Graph graph07_2 = fromFile("graph07", new File("src/main/resources/output/graph07.gka"));
        Graph graph08_2 = fromFile("graph08", new File("src/main/resources/output/graph08.gka"));
        Graph graph09_2 = fromFile("graph09", new File("src/main/resources/output/graph09.gka"));
        Graph graph10_2 = fromFile("graph10", new File("src/main/resources/output/graph10.gka"));

//        assertEquals(graph01, graph01_2);
//        assertEquals(graph02, graph02_2);
//        assertEquals(graph03, graph03_2);
//        assertEquals(graph04, graph04_2);
//        assertEquals(graph05, graph05_2);
//        assertEquals(graph06, graph06_2);
//        assertEquals(graph07, graph07_2);
//        assertEquals(graph08, graph08_2);
//        assertEquals(graph09, graph09_2);
//        assertEquals(graph10, graph10_2);

    }

}