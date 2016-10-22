package helper;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.MatchResult;
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
        int currentLineNumber = 1;
        String uml = "[öÖäÄüÜßa-zA-Z0-9]";
        String ws = "\\p{Blank}*";
        String linePattern = "(" + uml + "+)" + ws + "(-[->])" + ws + "(" + uml + "+)(" + ws + "\\((" + uml + ")*\\))?(" + ws + ":" + ws + "(\\d+))?"+ws+"?;";
        //TODO Edges ohne kanten
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Pattern pattern = Pattern.compile(linePattern);
            Matcher matcher = pattern.matcher(line);

            if (!matcher.find()) {
                // Skip empty Lines
                if (!line.equals(""))
                    System.err.println(currentLineNumber + ". ERROR: [" +line+"]");
                else
                    System.err.println(currentLineNumber + ". is empty");
            } else {
                Boolean isDirected = false;
                String edge = "";
                String weight = "";
                String node0 = matcher.group(1);
                String node1 = matcher.group(3);
                String arrow = matcher.group(2);

                if (matcher.group(5) != null) edge = matcher.group(5);
                else edge = node0+node1;
                if (matcher.group(7) != null) weight = matcher.group(7);
                if (arrow.equals("->")) isDirected = true;
                if (graph.getNode(node0) == null) graph.addNode(node0);
                if (graph.getNode(node1) == null) graph.addNode(node1);

                try {
                    graph.addEdge(edge, node0, node1, isDirected);
                    System.err.println(currentLineNumber + ". "+edgeToLine(graph.getEdge(edge),true,false)+" added.");
                } catch (EdgeRejectedException e) {
                    System.err.println(e);
                } catch (IdAlreadyInUseException e){
                    System.err.println(e + "\nEdge will be created with the ID "+node0+node1);
                    graph.addEdge(node0+node1, node0, node1, isDirected);
                    System.err.println(currentLineNumber + ". "+edgeToLine(graph.getEdge(node0+node1),true,false)+" added.");
                }

                if (!weight.equals(""))
                    graph.setAttribute("weight", Integer.valueOf(weight));
            }
            currentLineNumber++;

        }
        scanner.close();
        return graph;
    }

    // ====== PRIVATE =======
    /**
     * Preview of a graph in the terminal
     * <p>
     *
     * @param graph
     * @param enableName
     * @param enableWeight
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
     * @param edge
     * @param enableName
     * @param enableWeight
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

        Graph graph1 = fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph10.gka"));
//        preview(graph1,true,false);
//        graph1.display();
//        fromFileWithFileChooser("dfdf");
    }

}
