package helper;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.jetbrains.annotations.NotNull;

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
            bw.write(edgeToLine(edge,enableName,enableWeight));
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
    @NotNull
    public static Graph fromFileWithFileChooser(@NotNull String name) throws RuntimeException, FileNotFoundException {
        JFileChooser fc = new JFileChooser();
        File fileToRead = fc.getSelectedFile();
        int state = fc.showOpenDialog(null);

        if (state == JFileChooser.APPROVE_OPTION)
            return fromFile(name, fileToRead);
        else
            throw new RuntimeException("Much Error!");
    }

    /**
     * Reads a graph from a .gka file.
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
        Scanner scanner = new Scanner(fileToRead,"ISO-8859-1");
        scanner.useLocale(Locale.GERMANY);
//        while (scanner.hasNextLine()){
//            System.out.println(scanner.nextLine());
//        }
        int currentLineNumber = 1;
        String linePattern = "([\\w[öÖäÄüÜßa-zA-Z]]+)\\p{Blank}(-[->])\\p{Blank}([\\w[öÖäÄüÜßa-zA-Z]]+)(\\p{Blank}([\\w[öÖäÄüÜßa-zA-Z]]*))?(\\p{Blank}:\\p{Blank}+(\\d+))?;";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Pattern pattern = Pattern.compile(linePattern);
            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()){
                throw new RuntimeException("error at line:" + currentLineNumber);
            }else {
                Boolean isDirected = false;
                String edge ="";
                String weight="";
                String node0 = matcher.group(1);
                String node1 = matcher.group(3);
                String arrow = matcher.group(2);

                if (matcher.group(5)!=null)
                    edge = matcher.group(5);
                if (matcher.group(7)!=null)
                    weight = matcher.group(7);
                if (arrow.equals("->"))
                    isDirected = true;
                if (graph.getNode(node0)==null)
                    graph.addNode(node0);
                if (graph.getNode(node1)==null)
                    graph.addNode(node1);
                try {
                    if (edge.equals(""))
                        graph.addEdge(node0+node1,node0,node1,isDirected);
                    else
                        graph.addEdge(arrow,node0,node1,isDirected);
                }catch (EdgeRejectedException e){
                    System.err.println(e);
                }

                if (!weight.equals(""))
                    graph.setAttribute("weight",Integer.valueOf(weight));
            }


//            Matcher matcher = Pattern
//            scanner.findInLine(linePattern);
//            MatchResult result = scanner.match();
//            for (int i=1; i<=result.groupCount(); i++)
//                System.out.println(result.group(i));
            currentLineNumber++;

        }
        scanner.close();
        return graph;
    }

    // ====== PRIVATE =======

    private static void preview(@NotNull Graph graph,
                                @NotNull Boolean enableName,
                                @NotNull Boolean enableWeight) {
        for (Edge edge : graph.getEachEdge()) {
            System.out.println(edgeToLine(edge,enableName,enableWeight));
        }
    }

    private static String edgeToLine(@NotNull Edge edge,
                                     @NotNull Boolean enableName,
                                     @NotNull Boolean enableWeight){
        String name = "", weight = "", arrow = " -- ";
        if (edge.isDirected()) arrow = " -> ";
        if (enableName) name = " (" + edge.getId() + ")";
        if (enableWeight) {
            if (edge.getAttribute("weight") == null)
                throw new RuntimeException("Edge has no attribute with the key <weight>");
            weight = " : " + edge.getAttribute("weight");
        }
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

        Graph graph1 = fromFile("MyGraph",new File("src/main/resources/input/BspGraph/graph03.gka"));
        graph1.display();
//        fromFileWithFileChooser("dfdf");
    }

}
