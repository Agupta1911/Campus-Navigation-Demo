/**
 * Project 4:
 * Navigator.java
 * Description:
 * A graph processing program that analyzes campus connectivity using different graph algorithms. The program will read a campus map from a file and allow users to explore different paths and connections between campus buildings using DFS, BFS, find the MST using Kruskal’s algorithm, and find the shortest path using Dijkstra’s algorithm.
 * 
 * Name: Aarav Gupta
 * ID: 20760160
 * Github ID: Agupta1911
 * Date: 12/01/2024
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Public class which represents the campus navigation system
 * Manages and records information about nodes(buildings), paths between them, the MST, results of DFS and BFS, and finding the shortest path between buildings.
 */
public class Navigator {
    /*Main function to take the users input and fufill the user's request calling the relevant methods and displaying the end result. */
    public static void main(String[] args) {
        Graph graph = null;
        // Scanner to take in the user's input.
        Scanner scanner = new Scanner(System.in);

        //Checks if an argument has been provided to read in the file from.
        if (args.length > 0) {
            String fileName = args[0];
            graph = new Graph();
            try {
                graph.readInGraph(fileName);
                System.out.println("The campus map was loaded from: " + fileName);
            } catch (IOException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        }
        while (true) {
            System.out.println("\nMenu for Campus Navigation");
            System.out.println("Please input 1 to Load Campus Map");
            System.out.println("Please input 2 for Depth First Search (DFS)");
            System.out.println("Please input 3 for Breadth First Search (BFS)");
            System.out.println("Please input 4 to Find Minimum Spanning Tree using Kruskal's algorithm");
            System.out.println("Please input 5 to Find Shortest Path using Dijkstra's algorithm");
            System.out.println("Please input 6 to Exit");
            System.out.print("Please enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                // Read in the graph and store the information
                case 1:
                    System.out.print("Please enter the input file name:");
                    String fileName = scanner.nextLine();
                    graph = new Graph();
                    try {
                        graph.readInGraph(fileName);
                        System.out.println("The campus map was loaded!");
                    } catch (IOException e) {
                        //Prints error message if there was an error loading in the text file
                        System.out.println("Error loading file: " + e.getMessage());
                    }
                    break;
                // Performs the Depth First Search, taking the input of a starting building
                case 2:
                    //Checks that a valid campus map has been loaded and read in.
                    if (graph == null) {
                        System.out.println("Please load a campus map first.");
                        break;
                    }
                    System.out.print("Please enter the starting building: ");
                    String startNodeDFS = scanner.nextLine();
                    // Checks that the building is in the map
                    if (!graph.hasNode(startNodeDFS)) {
                        System.out.println("Building was not found in the campus map.");
                        break;
                    }
                    List<String> resultDFS = graph.DFS(startNodeDFS);
                    // Prints the traversal in the required format using "->"
                    System.out.println("DFS Traversal from " + startNodeDFS + ": " + String.join(" -> ", resultDFS));
                    break;
                // Performs the Breadth First search, asking for the input of a starting building.
                case 3:
                    if (graph == null) {
                        System.out.println("Please load a campus map first.");
                        break;
                    }
                    System.out.print("Please enter the starting building: ");
                    String startNodeBFS = scanner.nextLine();
                    if (!graph.hasNode(startNodeBFS)) {
                        System.out.println("Building was not found in the campus map.");
                        break;
                    }
                    List<String> resultBFS = graph.BFS(startNodeBFS);
                    System.out.println("BFS Traversal from " + startNodeBFS + ": " + String.join(" -> ", resultBFS));
                    break;
                // Finds the Minimum Spanning Tree using Kruskal's Algorithm
                case 4:
                    if (graph == null) {
                        System.out.println("Please load a campus map first.");
                        break;
                    }
                    List<Graph.Edge> mst = graph.kruskalMST();
                    System.out.println("Minimum Spanning Tree Results:");
                    /** Stores the total time it takes when moving through the minimum spanning tree reaching every node */
                    int minimumtotalWeight = 0;
                    for (Graph.Edge edge : mst) {
                        System.out.println(edge.beginning + " -> " + edge.finish + " (" + edge.weight + " minutes)");
                        minimumtotalWeight += edge.weight;
                    }
                    // Shows the total time it would take to traverse the entire MST.
                    System.out.println("Total time: " + minimumtotalWeight + " minutes");
                    break;
                // Find the shortest path between two buildings using Dijkstra's algorithm.
                case 5:
                    if (graph == null) {
                        System.out.println("Please load a campus map first.");
                        break;
                    }
                    System.out.print("Please enter the starting building: ");
                    String startDijkstra = scanner.nextLine();
                    System.out.print("Please enter the destination building: ");
                    String endDijkstra = scanner.nextLine();
                    if (!graph.hasNode(startDijkstra) || !graph.hasNode(endDijkstra)) {
                        System.out.println("One building or both buildings were not found in the campus map.");
                        break;
                    }
                    /* Stores the shortest path between two buildings given by the method, is assigned null if there is no path */
                    Graph.PathDetails path = graph.dijkstraShortestPath(startDijkstra, endDijkstra);
                    if (path == null) {
                        System.out.println("No path found between the buildings.");
                        break;
                    }
                    // Print out the result in the required format, and reconstruct the shortest path along with the distance.
                    System.out.println("Shortest Path Analysis:");
                    System.out.println("Path: " + String.join(" -> ", path.path));
                    System.out.println("Total time: " + path.distance + " minutes");
                    System.out.println("Detailed directions:");
                    System.out.println("1. Start at " + path.path.get(0));
                    for (int i = 0; i < path.path.size() - 1; i++) {
                        int distanceTime = graph.getDistance(path.path.get(i), path.path.get(i + 1));
                        System.out.println((i + 2) + ". Walk to " + path.path.get(i + 1) + " (" + distanceTime + " minutes)");
                    }
                    break;
                // Exit the program.
                case 6:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                // Checks for an invalid input choice.
                default:
                    System.out.println("Invalid choice inputted. Please try again.");
            }
        }
    }
}

/** Stores methods and data about the campus map in the form of a graph
 * Has methods to perform all of the menu options
 * Has nested classes which store specific information about the graph.
 */
class Graph {
    /** Represents an edge in a graph, recording the starting and ending buildings as well as the time it takes to travel between them (weight)  */
    static class Edge {
        String beginning;
        String finish;
        int weight; // Stores the time it takes to travel between buildings

        Edge(String beginning, String finish, int weight) {
            this.beginning = beginning;
            this.finish = finish;
            this.weight = weight;
        }
    }
    /** Stores information about a path formed between multiple buildings and records the distance (minutes it takes to go through the path) */
    static class PathDetails {
        List<String> path;
        int distance;

        PathDetails(List<String> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }
    /** Represents an adjacency map for the graph between the nodes */
    private final Map<String, List<Edge>> adjMap = new HashMap<>();
    private final Set<String> nodes = new HashSet<>();

    /* Reads in the campus map from a file
     * Differenciates between the NODES and EDGES sections to record the specific data
     * @param fileName is the filename of the file to be read in for the campus map given by the user.
     * @throws, throws the IOException if there is a problem reading the file.
     */ 
    void readInGraph(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            /** Checks if the section is NODES and not EDGES */
            boolean isNodeSection = true; 

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("NODES")) {
                    isNodeSection = true;
                } else if (line.equalsIgnoreCase("EDGES")) {
                    isNodeSection = false;
                } else if (!line.isEmpty()) {
                    if (isNodeSection) {
                        nodes.add(line);
                        adjMap.put(line, new ArrayList<>());
                    } else {
                        //Split using commas
                        String[] details = line.split(",");
                        if (details.length == 3) {
                            String beginning = details[0].trim();
                            String finish = details[1].trim();
                            int weight = Integer.parseInt(details[2].trim());
                            // Checks that the data is valid, checking that edge weight is valid between 0 and less than 30, and that all the nodes in the edges exist.
                            if (weight <= 0 || !nodes.contains(beginning) || !nodes.contains(finish) || weight>30) {
                                throw new IllegalArgumentException("Invalid edge data: " + line);
                            }
                            adjMap.get(beginning).add(new Edge(beginning, finish, weight));
                            adjMap.get(finish).add(new Edge(finish, beginning, weight));
                        }
                    }
                }
            }
        }
    }
    /* Checks to see if a building (node) exists in the campus map (graph)
     * @param node, provides the node to be checked if it is in the nodes set.
     * @return a boolean value showing whether or not the node is contained in nodes.
     */
    boolean hasNode(String node) {
        return nodes.contains(node);
    }

    /** Method to get the distance between nodes connected by an edge 
     * @param start, it is the starting building from where we must get the distance.
     * @param end, it is the destination.
     * @return, it returns the distance between the starting building and the destination building returning -1 if there is no path between buildings.
    */
    public int getDistance(String start, String end) {
        for (Edge edge : adjMap.getOrDefault(start, Collections.emptyList())) {
            if (edge.finish.equals(end)) {
                return edge.weight;
            }
        }
        // Return -1 if no direct path exists
        return -1;
    }

    /* Function that initializes the process for DFS, initializing variables, and returns the result after calling another function to complete the DFS. 
     * @param start, it is the source building to start the DFS from
     * @return returns the result of the DFS, as a List<String>, all the nodes it as gone through in order.
    */
    List<String> DFS(String start) {
        Set<String> visited = new HashSet<>();
        List<String> result = new ArrayList<>();
        // Call function to continue and actually perform the DFS
        performDFS(start, visited, result); 
        return result;
    }

    /* Function that performs the DFS, adding all nodes in the order they were visted using DFS in result.
     * @param current, it is the current node in the recursive process that we are performing the DFS from.
     * @param visted, stores all the nodes we have visted throughout the DFS
     * @param result, stores the result of the DFS as we construct it.
     */
    private void performDFS(String current, Set<String> visited, List<String> result) {
        visited.add(current);
        result.add(current);
        for (Edge edge : adjMap.getOrDefault(current, Collections.emptyList())) { 
            if (!visited.contains(edge.finish)) {
                performDFS(edge.finish, visited, result);
            }
        }
    }

    /* Function that performs BFS and returns the result
     * @param start, it is the source building from where to start the BFS from
     * @return result, it is the result of the BFS, all the nodes it as gone through in order.
     */
    List<String> BFS(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> result = new ArrayList<>();
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);
            for (Edge edge : adjMap.getOrDefault(vertex, Collections.emptyList())) {
                if (!visited.contains(edge.finish)) {
                    visited.add(edge.finish);
                    queue.offer(edge.finish);
                }
            }
        }
        return result;
    }

    /** Returns a MST using Kruskal's algorithm.
     * Sorts every edge of the graph in ascending order of distance.
     * Checks every edge to see if both of the nodes its connecting have the same root.
     * If they have different roots then the edge is added to the MST.
     * @return returns the MST resulted by using the algorithm. 
     */
    List<Edge> kruskalMST() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> edgesList : adjMap.values()) {
            edges.addAll(edgesList);
        }
        /*Sorts the edges in ascending order */
        Collections.sort(edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return Integer.compare(e1.weight, e2.weight);
            }
        });
    
        Map<String, String> parent = new HashMap<>();
        for (String node : nodes) {
            parent.put(node, node);
        }
    
        List<Edge> MST = new ArrayList<>();
        for (Edge edge : edges) {
            String root1 = getRoot(parent, edge.beginning);
            String root2 = getRoot(parent, edge.finish);
            if (!root1.equals(root2)) {
                MST.add(edge);
                parent.put(root1, root2);
            }
        }
        return MST;
    }
    
    /** Returns the root of a node
     * @param parent, it is the map storing the parent of the current node
     * @param node, it is the node that we want to find the root of
     * @return node, returns the root node
     */
    private String getRoot(Map<String, String> parent, String node) {
        while (!parent.get(node).equals(node)) {
            node = parent.get(node);
        }
        return node;
    }

    /** Finds the shortes path between two buildings using dijkstra's algorithm
     * By checking the total distances of various different paths it finds the shortest one to the end building (destination).
     * @param start, it is the starting building to find the shortest path between.
     * @param end, it is the ending building (destination).
     * @return current, returns the shortest path, or returns null if there is none.
     */
    PathDetails dijkstraShortestPath(String start, String end) {
        // Makes a priority queue for the pathDetails, sorting in ascending order by distance
        PriorityQueue<PathDetails> priorityQueue = new PriorityQueue<>(new Comparator<PathDetails>() {
            @Override
            public int compare(PathDetails details1, PathDetails details2) {
                return Integer.compare(details1.distance, details2.distance);
            }
        });
        Map<String, Integer> distances = new HashMap<>();
        // Assigns every distance to max at first
        for (String node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        priorityQueue.add(new PathDetails(new ArrayList<>(List.of(start)), 0));
        //Continue while priorityQueue isn't empty
        while (!priorityQueue.isEmpty()) {
            PathDetails current = priorityQueue.poll();
            String currentNode = current.path.get(current.path.size() - 1);

            if (currentNode.equals(end)) {
                return current;
            }
            // Checks every path for every edge to find the shortest path to the destination
            for (Edge edge : adjMap.getOrDefault(currentNode, Collections.emptyList())) {
                int newDistance = distances.get(currentNode) + edge.weight;
                if (newDistance < distances.get(edge.finish)) {
                    distances.put(edge.finish, newDistance);
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(edge.finish);
                    //adds the new path in the priorityQueue which reorders itself accordingly in ascending order
                    priorityQueue.add(new PathDetails(newPath, newDistance));
                }
            }
        }
        return null;
    }
}
