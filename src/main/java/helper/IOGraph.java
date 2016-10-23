package helper;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.util.IllegalFormatException;
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
            if (edge.getAttribute("weight")!=null)
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
        System.err.println("====== "+ fileToRead.getName() +" ======");
        Graph graph = new MultiGraph(name);
        Scanner scanner = new Scanner(fileToRead, "ISO-8859-1");
//        scanner.useLocale(Locale.GERMANY);
        int ln = 1;
        String uml = "[öÖäÄüÜßa-zA-Z0-9]";
        String ws = "\\p{Blank}*";
        String normalEdgePattern = "(" + uml + "+)" + ws + "(-[->])" + ws + "(" + uml + "+)(" + ws + "\\((" + uml + "*)\\))?(" + ws + ":" + ws + "(\\d+))?"+ws+";";
        String singleEdgePatterm = "(" + uml + "+)(" + ws + "\\((" + uml + ")*\\))?(" + ws + ":" + ws + "(\\d+))?"+ws+";";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher normalMatcher = Pattern.compile(normalEdgePattern).matcher(line);
            Matcher singleMatcher = Pattern.compile(singleEdgePatterm).matcher(line);

            if (normalMatcher.matches()){ // a -> b (ab) : 1
                Boolean isDirected = false;
                String edge = "";
                String weight = "";
                String node0 = normalMatcher.group(1);
                String node1 = normalMatcher.group(3);
                String arrow = normalMatcher.group(2);

                if (normalMatcher.group(5) != null) {
                    edge = normalMatcher.group(5);
                    if (graph.getEdge(edge) != null){ // if edge does already exists
                        System.err.println("Edge '" +edge+ "' does already exists. Will be renamed to " + node0 + "_to_" + node1);
                        edge = node0 + "_to_" + node1;
                    }
                }else
                    edge = node0+"_to_"+node1;
                if (normalMatcher.group(7) != null) weight = normalMatcher.group(7);
                if (arrow.equals("->")) isDirected = true;

                createNodeIfDoestExist(graph, node0);
                createNodeIfDoestExist(graph, node1);

                try {
                    addEdge(graph,edge,node0,node1,isDirected,weight,ln);
                } catch (EdgeRejectedException e) {
                    System.err.println(e);
                }

            } else if(singleMatcher.matches()){ // a (a) : 1
                String edge = "";
                String weight = "";
                String node = singleMatcher.group(1);

                createNodeIfDoestExist(graph, node);
                if (singleMatcher.group(5) != null) weight = singleMatcher.group(5);
                if (singleMatcher.group(3) != null) {
                    edge = singleMatcher.group(3);
                    if (graph.getEdge(edge) != null){ // if edge does already exists
                        System.out.println("Edge" +edge+ "does already exists. Will be renamed to " + node + "_to_" + node);
                        edge = node + "_to_" + node;
                    }
                }
                if (!edge.equals("")){
                    try {
                        System.err.println("creating loop...");
                        addEdge(graph,edge,node,node,false,weight,ln);
                    } catch (EdgeRejectedException e) {
                        System.err.println(e);
                    }
                }else
                    System.err.println(ln+". single node "+node+" added");
            } else {
                // Skip empty Lines
                if (!line.equals("")) {
                    System.err.println(ln + ". ERROR: [" + line + "]");
//                    throw new IllegalArgumentException("Wrong line format at line "+ln);
                }
//                else
//                    System.err.println(ln + ". is empty");
            }
            ln++;
        }
        scanner.close();
        return graph;
    }

    // ====== PRIVATE =======


    /**
     * Preview of a graph in the terminal
     * <p>
     * @param graph
     * @param enableName with name from edges?
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
     * @param edge edge we wanna create.
     * @param enableName name should be added?
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
     * @param graph given graph
     * @param edge edge with we wanna create
     * @param node0 first node
     * @param node1 second node; can be the same like node0 for loop
     * @param isDirected boolean
     * @param weight "" for no weight
     * @param ln line number
     */
    private static void addEdge(@NotNull Graph graph,
                                @NotNull String edge,
                                @NotNull String node0,
                                @NotNull String node1,
                                @NotNull Boolean isDirected,
                                @NotNull String weight,
                                @NotNull Integer ln) {
        if (weight.equals(""))
            graph.addEdge(edge, node0, node1, isDirected);
        else
            graph.addEdge(edge, node0, node1, isDirected).setAttribute("weight", Integer.valueOf(weight));
        System.err.println(ln + ". "+edgeToLine(graph.getEdge(edge),true,false)+" added.");
    }

    /**
     * Adds a node to the given graph if doest already exist.
     * <p>
     * @param graph
     * @param node0
     */
    private static void createNodeIfDoestExist(Graph graph, String node0) {
        if (graph.getNode(node0) == null) graph.addNode(node0);
    }

    // ========== MAIN ===========

    public static void main(String[] args) throws Exception {
        Graph graph = new SingleGraph("Tutorial 1");
//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addEdge("AB", "A", "B", true).setAttribute("weight", 4);
//        graph.addEdge("BC", "B", "C", false).setAttribute("weight", 5);
//        graph.addEdge("CA", "C", "A", true).setAttribute("weight", 6);
//
//        preview(graph, false, false);
//        preview(graph, true, false);
//        preview(graph, false, true);
//        preview(graph, true, true);
//        save(graph, "MyFile", true, true);
//        graph.addEdge("AB", "A", "B", true);


        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph01.gka")),
                "graph01");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph02.gka")),
                "graph02");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph03.gka")),
                "graph03");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph04.gka")),
                "graph04");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph05.gka")),
                "graph05");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph06.gka")),
                "graph06");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph07.gka")),
                "graph07");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph08.gka")),
                "graph08");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph09.gka")),
                "graph09");
        save(fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph10.gka")),
                "graph10");
//        preview(graph1,true,false);
//        graph1.display();
//        fromFileWithFileChooser("dfdf");
    }

}
