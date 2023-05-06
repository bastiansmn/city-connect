package fr.hydrogen.cityconnect.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NetworkTest {

    private Station A;
    private Station B;
    private Station C;
    private Station D;
    private Station E;
    private Station F;
    private Station G;
    private Station H;
    private Station I;
    private Station J;
    private CoordGPS coordGPS;
    private Ligne LIGNE_8;
    private VariantLigne LIGNE_8_2;
    private Network<Station> network;

    private Ligne l1;
    private Ligne l2;
    private Ligne l3;
    private Ligne l4;
    private VariantLigne ligne1;
    private VariantLigne ligne2;
    private VariantLigne ligne3;
    private VariantLigne ligne4;
    private VariantLigne ligne42;
    
    private void initializeHoraire(VariantLigne vl){
        for (int i=0; i<24; i++){
            for(int j=0; j<60; j++){
                for (int s=0; s<60; s++){
                    vl.addHoraire(LocalTime.of(i,j,s));
                }
            }
        }
    }

    @BeforeEach
    public void initialize()
    {
        network = new Network<>();
        A = new Station("A", network);
        B = new Station("B", network);
        C = new Station("C", network);
        D = new Station("D", network);
        E = new Station("E", network);
        F = new Station("F", network);
        G = new Station("G", network);
        H = new Station("H", network);
        I = new Station("I", network);
        J = new Station("J", network);
        coordGPS = new CoordGPS(0, 0);
        LIGNE_8 = new Ligne("8");
        LIGNE_8_2 = new VariantLigne(LIGNE_8, 2);
        LIGNE_8.addVariantLigne(LIGNE_8_2);

        l1 = new Ligne("1");
        l2 = new Ligne("2");
        l3 = new Ligne("3");
        l4 = new Ligne("4");
        ligne1 = new VariantLigne(l1, 1);
        initializeHoraire(ligne1);
        ligne2 = new VariantLigne(l2, 1);
        initializeHoraire(ligne2);
        ligne3 = new VariantLigne(l3, 1);
        initializeHoraire(ligne3);
        ligne4 = new VariantLigne(l4, 1);
        initializeHoraire(ligne4);
        ligne42 = new VariantLigne(l4, 2);
        initializeHoraire(ligne42);
        l1.addVariantLigne(ligne1);
        l2.addVariantLigne(ligne2);
        l3.addVariantLigne(ligne3);
        l4.addVariantLigne(ligne4);
        l4.addVariantLigne(ligne42);
    }

    @Test
    public void testAddLigne()
    {
        assertTrue(network.addLigne(new Ligne("1")));
        assertTrue(network.addLigne(new Ligne("1B")));
        assertFalse(network.addLigne(new Ligne("1")));
    }

    @Test
    public void testGetLignes()
    {
        network.addLigne(new Ligne("1"));
        network.addLigne(new Ligne("2"));
        assertEquals(2, network.getLignes().size());
    }

    @Test
    public void testGetLigne()
    {
        Ligne ligne1 = new Ligne("1");
        Ligne ligne2 = new Ligne("2");
        network.addLigne(ligne1);
        network.addLigne(ligne2);
        assertEquals(ligne1, network.getLigne("1"));
        assertEquals(ligne2, network.getLigne("2"));
        assertNotEquals(ligne2, network.getLigne("1"));
    }

    @Test
    public void testGetVariantLigne()
    {
        network.addLigne(LIGNE_8);
        assertEquals(LIGNE_8_2, network.getVariantLigne("8", 2));
    }

    @Test
    public void testAddHoraire()
    {
        network.addLigne(LIGNE_8);
        assertTrue(network.addHoraire("8", 2, LocalTime.of(8, 33)));
        assertTrue(network.addHoraire(LIGNE_8_2, LocalTime.of(8, 43)));
    }

    @Test
    public void testGetHorairesDepart()
    {
        network.addLigne(LIGNE_8);
        network.addHoraire(LIGNE_8_2, LocalTime.of(8, 33));
        List<LocalTime> expected = new ArrayList<>();
        expected.add(LocalTime.parse("08:33"));
        assertEquals(1, network.getHorairesDepart(LIGNE_8_2).size());
        assertEquals(1, network.getHorairesDepart("8", 2).size());
        assertEquals(expected, network.getHorairesDepart("8", 2));
        assertEquals(network.getHorairesDepart("8", 2), network.getHorairesDepart(LIGNE_8_2));
    }

    @Test
    public void testAddVertex()
    {
        assertTrue(network.addVertex(A));
        assertTrue(network.addVertex(B));
        assertFalse(network.addVertex(A));
    }

    @Test
    public void testAddEdge()
    {
        network.addVertex(A);
        network.addVertex(B);
        assertTrue(network.addEdge(new Trajet<>(A, coordGPS, B, coordGPS, LIGNE_8_2, 23l, 100.)));
        assertFalse(network.addEdge(new Trajet<>(A, coordGPS, B, coordGPS, LIGNE_8_2, 4l, 100.)));
        assertTrue(network.addEdge(new Trajet<>(C, coordGPS, D, coordGPS, LIGNE_8_2, 6l, 100.)));
    }

    @Test
    public void testGetAdjacents(){
        network.addEdge(new Trajet<>(A, coordGPS, B, coordGPS, LIGNE_8_2, 5l, 100.));
        network.addEdge(new Trajet<>(A, coordGPS, C, coordGPS, LIGNE_8_2, 10l, 100.));
        assertEquals(2, network.getAdjacents(A).size());
        assertEquals(0, network.getAdjacents(B).size());
        // assertNull(graph.getAdjacents(B));
    }
    @Test
    public void testGetAdjacentEdges(){
        network.addEdge(new Trajet<>(A, coordGPS, B, coordGPS, LIGNE_8_2, 5l, 100.));
        network.addEdge(new Trajet<>(A, coordGPS, C, coordGPS, LIGNE_8_2, 10l, 100.));
        assertEquals(2, network.getAdjacentEdges(A).size());
        assertEquals(0, network.getAdjacentEdges(B).size());
        // assertNull(graph.getAdjacents(B));
    }

    @Test
    public void testGetVertexSet() {
        network.addVertex(A);
        network.addVertex(B);
        network.addVertex(C);
        assertEquals(3, network.getVertexSet().size());
        assertTrue(network.getVertexSet().contains(A));
        assertTrue(network.getVertexSet().contains(B));
        assertTrue(network.getVertexSet().contains(C));
        assertFalse(network.getVertexSet().contains(D));
    }

    // @Test
    // public void testInitializationDist(){
    //     graph.addEdge(A, B, 1l, 100.);
    //     graph.addEdge(A, C, 2l, 100.);
    //     graph.addEdge(B, C, 3l, 100.);
    //     graph.addEdge(C, D, 4l, 100.);
    //     HashMap<Station,Double> dist=graph.initializationDist(A);
    //     HashMap<Station,Double> expected=new HashMap<>();
    //     expected.put(A,0.);
    //     expected.put(B,-1.);
    //     expected.put(C,-1.);
    //     expected.put(D,-1.);
    //     assertEquals(expected,dist);
    // }

    // @Test
    // public void testInitializationP(){
    //     graph.addEdge(A,B, 1l,100.);
    //     graph.addEdge(A,C, 2l,200.);
    //     graph.addEdge(B,C, 3l,300.);
    //     graph.addEdge(C,D, 4l,400.);
    //     List<Station> P=graph.initializationP();
    //     List<Station> expected=new LinkedList<>();
    //     expected.add(A);
    //     expected.add(B);
    //     expected.add(C);
    //     expected.add(D);
    //     assertEquals(expected.size(),P.size());
    // }

    // @Test
    // public void testInitializationPredecessor(){
    //     graph.addEdge(A, B, 1l, 100.);
    //     graph.addEdge(A, C, 2l, 100.);
    //     graph.addEdge(B, C, 3l, 100.);
    //     graph.addEdge(C, D, 4l, 100.);
    //     HashMap<Station,Station> dist=graph.initializationPredecessor();
    //     HashMap<Station,Station> expected=new HashMap<>();
    //     expected.put(A,null);
    //     expected.put(B,null);
    //     expected.put(C,null);
    //     expected.put(D,null);
    //     assertEquals(expected,dist);
    // }

    // @Test
    // public void testFind_min(){
    //     graph.addEdge(A, B, 1l, 100.);
    //     graph.addEdge(A, C, 2l, 100.);
    //     graph.addEdge(B, C, 3l, 100.);
    //     graph.addEdge(C, D, 4l, 100.);
    //     List<Station> P=graph.initializationP();
    //     // HashMap<Station,Station> predecessor=new HashMap<>();
    //     HashMap<Station,Double> dist=graph.initializationDist(A);
    //     assertEquals(A,graph.find_min(P,dist));
    // }


    // @Test
    // public void testUpdate_distance(){
    //     graph.addEdge(A,B,85l, 100.);
    //     graph.addEdge(A,C,217l, 100.);
    //     HashMap<Station,Station> predecessor=graph.initializationPredecessor();
    //     HashMap<Station,Double> dist=graph.initializationDist(A);
    //     graph.update_distance(dist,predecessor,A,B,85);
    //     HashMap<Station,Station> expected=new HashMap<>();
    //     expected.put(A,null);
    //     expected.put(B,A);
    //     expected.put(C,null);
    //     assertEquals(expected,predecessor);
    // }


    // @Test
    // public void testDijkstra(){
    //     graph.addEdge(A,B,85l,100.);
    //     graph.addEdge(A,C,217l,100.);
    //     graph.addEdge(A,E,173l,100.);
    //     graph.addEdge(B,F,80l,100.);
    //     graph.addEdge(C,G,186l,100.);
    //     graph.addEdge(C,H,103l,100.);
    //     graph.addEdge(H,D,183l,100.);
    //     graph.addEdge(E,J,502l,100.);
    //     graph.addEdge(F,I,250l,100.);
    //     graph.addEdge(H,J,167l,100.);
    //     graph.addEdge(I,J,84l,100.);
    //     HashMap<Station,Station> res=graph.dijkstra(A, Graph.WEIGHT_TIME);
    //     HashMap<Station,Station> expected=new HashMap<>();
    //     expected.put(A,null);
    //     expected.put(B,A);
    //     expected.put(C,A);
    //     expected.put(D,H);
    //     expected.put(F,B);
    //     expected.put(G,C);
    //     expected.put(H,C);
    //     expected.put(E,A);
    //     expected.put(I,F);
    //     expected.put(J,H);
    //     assertEquals(expected,res);
    // }

}
