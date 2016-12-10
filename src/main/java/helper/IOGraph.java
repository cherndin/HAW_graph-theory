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
    private static final Logger LOG = Logger.getLogger(IOGraph.class);

    public static boolean throwExc = false;
    public static String attributeKeyValue = "weight";

    /* Class should not be an instance
     * because it's a helper class with static methods.
     */
    private IOGraph() {
    }

    // ===== PUBLIC =====

    /**
     * Saves graph in a .gka file.
     * GraphID will be the name of the file.
     *
     * @param graph
     * @throws IOException
     */
    public static void save(@NotNull Graph graph) throws IOException {
        save(graph, graph.getId());
    }

    /**
     * Saves graph in a .gka file
     *
     * @param graph
     * @param filename
     * @throws IOException
     */
    public static void save(@NotNull Graph graph,
                            @NotNull String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output/" + filename + ".gka"));
        Boolean enableWeight = false;
        for (Edge edge : graph.getEachEdge()) {
            if (edge.getAttribute("weight") != null)
                enableWeight = true;
            bw.write(GraphUtil.edgeToLine(edge, true, enableWeight));
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
            bw.write(GraphUtil.edgeToLine(edge, enableName, enableWeight));
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
        LOG.info("=== Creating graph from " + fileToRead.getName() + " ===");
        Graph graph = new MultiGraph(name);
        Scanner scanner = new Scanner(fileToRead, "utf-8");
        int ln = 1;
        String uml = "[_öÖäÄüÜßa-zA-Z0-9]";
        String ws = "\\p{Blank}*";
        String edgePattern = "(" + uml + "+)(" + ws + "(-[->])" + ws + "(" + uml + "+))?(" + ws + "\\((" + uml + "*)\\))?(" + ws + ":" + ws + "(\\d+))?" + ws + ";";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher lineMatcher = Pattern.compile(edgePattern).matcher(line);

            if (lineMatcher.matches()) {
                Boolean isDirected = false;
                String edgeID = "", edgeWeight = "", arrow = "", node1 = ""; // Nullable
                String node0 = lineMatcher.group(1); // NotNull
                createNodeIfDoesntExist(graph, node0);

                // NORMAL EDGE with two different edges
                if (lineMatcher.group(3) != null && lineMatcher.group(4) != null) {
                    arrow = lineMatcher.group(3);
                    node1 = lineMatcher.group(4);
                    createNodeIfDoesntExist(graph, node1);

                    if (lineMatcher.group(6) != null) { // edgeID set?
                        edgeID = lineMatcher.group(6);
                        if (graph.getEdge(edgeID) != null) { // if edgeID does already exists
                            LOG.info("Edge '" + edgeID + "' does already exists. Will be renamed to " + node0 + "_to_" + node1);
                            edgeID = node0 + "_to_" + node1;
                        }
                    } else
                        edgeID = node0 + "_to_" + node1;
                    if (lineMatcher.group(8) != null) edgeWeight = lineMatcher.group(8);
                    if (arrow.equals("->")) isDirected = true;
                } else { // SINGLE NODE OR LOOP
                    if (lineMatcher.group(6) != null) { // edgeID is given so we create a node with loop
                        node1 = node0;
                    } else {
                        LOG.debug(ln + ". single node " + node0 + " added");
                        break; // addEdge should NOT be reached
                    }
                }
                addEdge(graph, edgeID, node0, node1, isDirected, edgeWeight, ln);
            } else {
                if (!line.equals("")) { // big big Error
                    LOG.error(ln + ". ERROR: [" + line + "]");
                    if (throwExc)
                        throw new IllegalArgumentException("Wrong line format at line " + ln);
                } else // Skip empty Lines
                    LOG.debug(ln + ". is empty");
            }
            ln++;
        }
        scanner.close();
        LOG.info(graph.getId() + " created from " + fileToRead.getName() + "\n");
        return graph;
    }

    /**
     * extended display
     *
     * @param graph
     */
    public static void display(Graph graph) {
        GraphUtil.buildForDisplay(graph);
        graph.display();
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
            System.out.println(GraphUtil.edgeToLine(edge, enableName, enableWeight));
        }
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
            if (weight.equals("")) // without edgeWeight
                graph.addEdge(edge, node0, node1, isDirected);
            else  // normal case
                graph.addEdge(edge, node0, node1, isDirected).setAttribute(attributeKeyValue, Integer.valueOf(weight));
            LOG.debug(ln + ". " + GraphUtil.edgeToLine(graph.getEdge(edge), true, false) + " added.");
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
    private static void createNodeIfDoesntExist(Graph graph, String node0) {
        if (graph.getNode(node0) == null) graph.addNode(node0);
    }

    // ========== MAIN ===========

    public static void main(String[] args) throws Exception {
//        throwExc = false;
        Graph graph3 = fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph02.gka"));
        display(graph3);

//        Graph pentaCircle = new MultiGraph("pentaCircle");
//        pentaCircle.addNode("a");
//        pentaCircle.addNode("b");
//        pentaCircle.addNode("c");
//        pentaCircle.addNode("d");
//        pentaCircle.addNode("e");
//
//        pentaCircle.addEdge("ab", "a", "b");
//        pentaCircle.addEdge("ac", "a", "c");
//        pentaCircle.addEdge("ae", "a", "e");
//        pentaCircle.addEdge("ad", "a", "d");
//        pentaCircle.addEdge("bd", "b", "d");
//        pentaCircle.addEdge("bc", "b", "c");
//        pentaCircle.addEdge("be", "b", "e");
//        pentaCircle.addEdge("ed", "e", "d");
//        pentaCircle.addEdge("ec", "e", "c");
//        pentaCircle.addEdge("cd", "c", "d");
//        save(pentaCircle);
//        Graph pentaCircle2 = fromFile("pentaCircle2", new File("src/main/resources/output/pentaCircle.gka"));
//        display(pentaCircle2);

//        Graph graph2 = fromFileWithFileChooser("MyGraph2");
//        display(graph2);
    }

}
