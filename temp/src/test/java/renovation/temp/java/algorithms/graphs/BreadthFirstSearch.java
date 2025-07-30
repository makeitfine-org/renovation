/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.graphs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstSearch {

    private Map<String, List<String>> adjList = new HashMap<>();

    public void addEdge(String src, String dest) {
        adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(src); // For undirected graph
    }

    public void bfs(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);
        System.out.println();

        while (!queue.isEmpty()) {
            String node = queue.poll();
//            System.out.print(node + " ");
            System.out.print(node + ": ");

            for (String neighbor : adjList.getOrDefault(node, List.of())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    System.out.print(neighbor + ", ");
                }
            }
            System.out.println();
        }
    }

    @Test
    void test() {
        BreadthFirstSearch graph = new BreadthFirstSearch();

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D1");
        graph.addEdge("D1", "E");
        graph.addEdge("B", "D");
        graph.addEdge("E", "C1");
        graph.addEdge("E", "C2");
        graph.addEdge("D1", "E1");

        System.out.print("BFS traversal: ");
        graph.bfs("A");
    }
}

