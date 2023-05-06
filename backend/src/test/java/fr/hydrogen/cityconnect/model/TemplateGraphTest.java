/*
package fr.hydrogen;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class GraphTest
{
    private static Graph<String, Integer> graph;
    @BeforeEach
    public void initialize()
    {
        graph = new Graph<>();
    }

    @Test
    public void testAddVertex()
    {

        assertTrue(graph.addVertex("A"));
        assertTrue(graph.addVertex("B"));
        assertFalse(graph.addVertex("A"));
    }

    @Test
    public void testAddEdge()
    {
        graph.addVertex("A");
        graph.addVertex("B");
        assertTrue(graph.addEdge("A", "B", 23));
        assertFalse(graph.addEdge("A", "B", 4));
        assertTrue(graph.addEdge("C", "D", 6));
    }

    @Test
    public void testGetAdjacents(){
        graph.addEdge("A", "B", 5);
        graph.addEdge("A", "C", 10);
        assertEquals(2, graph.getAdjacents("A").size());
        assertEquals(0, graph.getAdjacents("B").size());
        assertNull(graph.getAdjacents("D"));
    }

    @Test
    public void testGetVertexSet() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        assertEquals(3, graph.getVertexSet().size());
        assertTrue(graph.getVertexSet().contains("A"));
        assertTrue(graph.getVertexSet().contains("B"));
        assertTrue(graph.getVertexSet().contains("C"));
        assertFalse(graph.getVertexSet().contains("D"));
    }

    @Test
    public void testToString() {
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 3);
        graph.addEdge("C", "D", 4);
        String expected = "A: (A -> B, weight = 1), (A -> C, weight = 2), \n" +
                    "B: (B -> C, weight = 3), \n" +
                    "C: (C -> D, weight = 4), \n" +
                    "D: \n";
            assertEquals(expected, graph.toString());
    }

    @Test
    public void testInitializationDist(){
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 3);
        graph.addEdge("C", "D", 4);
        HashMap<String,Double> dist=graph.initializationDist("A");
        HashMap<String,Double> expected=new HashMap<>();
        expected.put("A",0.);
        expected.put("B",-1.);
        expected.put("C",-1.);
        expected.put("D",-1.);
        assertEquals(expected,dist);
    }

    @Test
    public void testInitializationP(){
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 3);
        graph.addEdge("C", "D", 4);
        List<String> P=graph.initializationP();
        List<String> expected=new LinkedList<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");
        assertEquals(expected,P);
    }

    @Test
    public void testInitializationPredecessor(){
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 3);
        graph.addEdge("C", "D", 4);
        HashMap<String,String> dist=graph.initializationPredecessor();
        HashMap<String,String> expected=new HashMap<>();
        expected.put("A",null);
        expected.put("B",null);
        expected.put("C",null);
        expected.put("D",null);
        assertEquals(expected,dist);
    }

    @Test
    public void testFind_min(){
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 3);
        graph.addEdge("C", "D", 4);
        List<String> P=graph.initializationP();
        HashMap<String,String> predecessor=new HashMap<String,String>();
        HashMap<String,Double> dist=graph.initializationDist("A");
        assertEquals("A",graph.find_min(P,dist));
    }


    @Test
    public void testUpdate_distance(){
        graph.addEdge("A","B",85);
        graph.addEdge("A","C",217);
        HashMap<String,String> predecessor=graph.initializationPredecessor();
        HashMap<String,Double> dist=graph.initializationDist("A");
        graph.update_distance(dist,predecessor,"A","B",85);
        HashMap<String,String> expected=new HashMap<>();
        expected.put("A",null);
        expected.put("B","A");
        expected.put("C",null);
        assertEquals(expected,predecessor);


    }

    @Test
    public void testDijkstra(){
        graph.addEdge("A","B",85);
        graph.addEdge("A","C",217);
        graph.addEdge("A","E",173);
        graph.addEdge("B","F",80);
        graph.addEdge("C","G",186);
        graph.addEdge("C","H",103);
        graph.addEdge("H","D",183);
        graph.addEdge("E","J",502);
        graph.addEdge("F","I",250);
        graph.addEdge("H","J",167);
        graph.addEdge("I","J",84);
        HashMap<String,String> res=graph.dijkstra("A");
        HashMap<String,String> expected=new HashMap<>();
        expected.put("A",null);
        expected.put("B","A");
        expected.put("C","A");
        expected.put("D","H");
        expected.put("F","B");
        expected.put("G","C");
        expected.put("H","C");
        expected.put("E","A");
        expected.put("I","F");
        expected.put("J","H");
        assertEquals(expected,res);


    }

    @Test
    public void testGet_shorted_path(){
        graph.addEdge("A","B",85);
        graph.addEdge("A","C",217);
        graph.addEdge("A","E",173);
        graph.addEdge("B","F",80);
        graph.addEdge("C","G",186);
        graph.addEdge("C","H",103);
        graph.addEdge("H","D",183);
        graph.addEdge("E","J",502);
        graph.addEdge("F","I",250);
        graph.addEdge("H","J",167);
        graph.addEdge("I","J",84);
        LinkedList<String> res=graph.get_shorted_path("A","J");
        LinkedList<String> expected=new LinkedList<String>();
        expected.add("A");
        expected.add("C");
        expected.add("H");
        expected.add("J");
        assertEquals(expected,res);

    }







}

*/