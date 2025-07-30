/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.graphs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepthFirstSearch {
    static class Graph {
        private final Map<String, List<String>> adjList = new HashMap<>();

        public void addEdge(String src, String dest) {
            adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(dest);
            adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(src); // для неориентированного графа
        }

        public void dfs(String start) {
            Set<String> visited = new HashSet<>();
            System.out.print("DFS starting from " + start + ": ");
            dfsRecursive(start, visited);
            System.out.println();
        }

        private void dfsRecursive(String node, Set<String> visited) {
            if (visited.contains(node)) return;

            visited.add(node);
            System.out.print(node + " ");

            for (String neighbor : adjList.getOrDefault(node, Collections.emptyList())) {
                dfsRecursive(neighbor, visited);
            }
        }
    }

    @Test
    void test() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D1");
        graph.addEdge("D1", "E");
        graph.addEdge("B", "D");
        graph.addEdge("E", "C1");
        graph.addEdge("E", "C2");
        graph.addEdge("D1", "E1");

        graph.dfs("A");  // Пример вызова: A B D C D1 E C1 C2 E1
    }
}

