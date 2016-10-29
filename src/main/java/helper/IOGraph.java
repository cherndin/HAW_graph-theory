package helper;

import org.apache.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MattX7 on 18.10.2016.
 * <p>
 * Export and import graphs to a .gka file.
 * <p>
 * <b>Format of directed:</b><br>
 * name node1 [ -> name node2] [(edge name)] [: edgeweight]; <br>
 * <b>Format of undirected</b><br>
 * name node1 [ -- name node2] [(edge name)] [: edgeweight]; <br>
 */
public class IOGraph {
    private static Logger logger = Logger.getLogger(IOGraph.class);

    /* Class should not be an instance
     * because it's a helper class with static methods.
     */
    private IOGraph() {
    }

    // ===== PUBLIC =====

    public static void save(@NotNull Graph graph,
                            @NotNull String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output/" + filename + ".gka"));
        Boolean enableWeight = false;
        for (Edge edge : graph.getEachEdge()) {
            if (edge.getAttribute("weight") != null)
                enableWeight = true;
            bw.write(edgeToLine(edge, true, enableWeight));
            bw.newLine();
            enableWeight = false;
        }
        bw.close();
    }

    /**
     * Saves a graph in a gka-file in the output folder.
     *
     * @param graph        you wanna save in the file
     * @param filename     name of the file
     * @param enableName   true if the name of the edge should be saved
     * @param enableWeight true if you wanna save the weight of the edges
     * @throws IOException if export fails
     */
    public static void save(@NotNull Graph graph,
                            @NotNull String filename,
                            @NotNull Boolean enableName,
                            @NotNull Boolean enableWeight) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output/" + filename + ".gka"));

        for (Edge edge : graph.getEachEdge()) {
            bw.write(edgeToLine(edge, enableName, enableWeight));
            bw.newLine();
        }
        bw.close();
    }


    /**
     * @param name of the new graph
     * @return the new created graph from file
     * @throws RuntimeException      Problem with FileChooser
     * @throws FileNotFoundException selected file could not be found
     */
    @Nullable
    public static Graph fromFileWithFileChooser(@NotNull String name) throws RuntimeException, FileNotFoundException {
        JFileChooser fc = new JFileChooser();
        File fileToRead = fc.getSelectedFile();
        int state = fc.showOpenDialog(null);

        if (state == JFileChooser.APPROVE_OPTION)
            return fromFile(name, fileToRead);
        else
            return null;
    }

    /**
     * Reads a graph from a .gka file.
     * <p>
     *
     * @param name       name of the new graph
     * @param fileToRead file to read
     * @return the new created graph from file
     * @throws FileNotFoundException fileToRead could not be found
     */
    @NotNull
    public static Graph fromFile(@NotNull String name,
                                 @NotNull File fileToRead) throws FileNotFoundException {
        Graph graph = new MultiGraph(name);
        Scanner scanner = new Scanner(fileToRead, "ISO-8859-1");
//        scanner.useLocale(Locale.GERMANY);
        int ln = 1;
        String uml = "[öÖäÄüÜßa-zA-Z0-9]";
        String ws = "\\p{Blank}*";
        String normalEdgePattern = "(" + uml + "+)(" + ws + "(-[->])" + ws + "(" + uml + "+))?(" + ws + "\\((" + uml + "*)\\))?(" + ws + ":" + ws + "(\\d+))?" + ws + ";";
        String singleEdgePattern = "(" + uml + "+)(" + ws + "\\((" + uml + ")*\\))?(" + ws + ":" + ws + "(\\d+))?" + ws + ";";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher linePattern = Pattern.compile(normalEdgePattern).matcher(line);

            if (linePattern.matches()) {

                Boolean isDirected = false;
                String edgeID = "", edgeWeight = "", arrow = "", node1 = ""; // maybe null
                String node0 = linePattern.group(1);
                createNodeIfDoestExist(graph, node0); // TODO delete if exception got thrown

                // NORMAL EDGE with two different nodes
                if (linePattern.group(3) != null && linePattern.group(4) != null) {
                    arrow = linePattern.group(3);
                    node1 = linePattern.group(4);
                    createNodeIfDoestExist(graph, node1);

                    if (linePattern.group(6) != null) {
                        edgeID = linePattern.group(6);
                        if (graph.getEdge(edgeID) != null) { // if edgeID does already exists
                            logger.info("Edge '" + edgeID + "' does already exists. Will be renamed to " + node0 + "_to_" + node1);
                            edgeID = node0 + "_to_" + node1;
                        }
                    } else
                        edgeID = node0 + "_to_" + node1;
                    if (linePattern.group(8) != null) edgeWeight = linePattern.group(8);
                    if (arrow.equals("->")) isDirected = true;
                } else { // SINGLE NODE OR LOOP
                    if (linePattern.group(6) != null) { // edgeID is given so we create a node with loop
                        node1 = node0;
                    } else {
                        logger.debug(ln + ". single node " + node0 + " added");
                        break; // addEdge should NOT be reached
                    }
                }
                addEdge(graph, edgeID, node0, node1, isDirected, edgeWeight, ln);
            } else {
                if (!line.equals("")) { // big big Error
                    logger.error(ln + ". ERROR: [" + line + "]");
//                    throw new IllegalArgumentException("Wrong line format at line "+ln);
                } else // Skip empty Lines
                    logger.debug(ln + ". is empty");
            }
            ln++;
        }
        scanner.close();
        logger.info(graph.getId() + " created from " + fileToRead.getName());
        return graph;
    }

    // ====== PRIVATE =======


    /**
     * Preview of a graph in the terminal
     * <p>
     *
     * @param graph
     * @param enableName   with name from edges?
     * @param enableWeight with weight from edges?
     */
    private static void preview(@NotNull Graph graph,
                                @NotNull Boolean enableName,
                                @NotNull Boolean enableWeight) {
        for (Edge edge : graph.getEachEdge()) {
            System.out.println(edgeToLine(edge, enableName, enableWeight));
        }
    }

    /**
     * Converts an Edge to String.
     * <p>
     *
     * @param edge         edge we wanna create.
     * @param enableName   name should be added?
     * @param enableWeight weight should be added?
     * @return name node1 [ arrow name node2] [(edge name)] [: edgeweight];
     */
    private static String edgeToLine(@NotNull Edge edge,
                                     @NotNull Boolean enableName,
                                     @NotNull Boolean enableWeight) {
        String name = "";
        String weight = "";
        String arrow = " -- ";

        if (enableWeight) {
            if (edge.getAttribute("weight") == null)
                throw new IllegalArgumentException("Edge has no attribute with the key <weight>");
            weight = " : " + edge.getAttribute("weight");
        }
        if (edge.isDirected()) arrow = " -> ";
        if (enableName) name = " (" + edge.getId() + ")";

        return edge.getNode0() + arrow + edge.getNode1() + name + weight + ";";
    }

    /**
     * Adds an edge to the given graph.
     * <p>
     *
     * @param graph      given graph
     * @param edge       edge with we wanna create
     * @param node0      first node
     * @param node1      second node; can be the same like node0 for loop
     * @param isDirected boolean
     * @param weight     "" for no weight
     * @param ln         line number
     */
    private static void addEdge(@NotNull Graph graph,
                                @NotNull String edge,
                                @NotNull String node0,
                                @NotNull String node1,
                                @NotNull Boolean isDirected,
                                @NotNull String weight,
                                @NotNull Integer ln) {
        String loop = "";
        try {
            if (node0.equals(node1)) // loop
                loop = "creating loop... ";
            if (weight.equals(""))
                graph.addEdge(edge, node0, node1, isDirected);
            else
                graph.addEdge(edge, node0, node1, isDirected).setAttribute("weight", Integer.valueOf(weight));
            logger.debug(ln + ". " + loop + edgeToLine(graph.getEdge(edge), true, false) + " added.");
        } catch (EdgeRejectedException e) {
            System.err.println(e);
        }
    }

    /**
     * Adds a node to the given graph if doest already exist.
     * <p>
     *
     * @param graph
     * @param node0
     */
    private static void createNodeIfDoestExist(Graph graph, String node0) {
        if (graph.getNode(node0) == null) graph.addNode(node0);
    }

    // ========== MAIN ===========

    public static void main(String[] args) throws Exception {
//        Graph graph = new SingleGraph("Tutorial 1");
//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addEdge("AB", "A", "B", true).setAttribute("weight", 4);
//        graph.addEdge("BC", "B", "C", false).setAttribute("weight", 5);
//        graph.addEdge("CA", "C", "A", true).setAttribute("weight", 6);
//        Graph graph1 = fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph01.gka"));
//
//
//        preview(graph, false, false);
//        preview(graph, true, false);
//        preview(graph, false, true);
//        preview(graph, true, true);
//        save(graph, "MyFile", true, true);
//        graph.addEdge("AB", "A", "B", true);


        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph01.gka")), "graph01");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph02.gka")), "graph02");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph03.gka")), "graph03");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph04.gka")), "graph04");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph05.gka")), "graph05");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph06.gka")), "graph06");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph07.gka")), "graph07");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph08.gka")), "graph08");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph09.gka")), "graph09");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph10.gka")), "graph10");
//        preview(graph1,true,false);
//        fromFileWithFileChooser("dfdf");
    }

}
