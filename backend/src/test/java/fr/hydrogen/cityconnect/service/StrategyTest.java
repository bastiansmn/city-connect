package fr.hydrogen.cityconnect.service;

import static org.junit.jupiter.api.Assertions.*;

import fr.hydrogen.cityconnect.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

public class StrategyTest {

    private static Api api;
    private static Strategy timeStrategy;
    private static Strategy distanceStrategy;

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
        
    public void initializeHoraire(VariantLigne vl){
        for (int i=0; i<24; i++){
            for(int j=0; j<60; j++){
                for (int s=0; s<60; s++){
                    vl.addHoraire(LocalTime.of(i,j,s));
                }
            }
        }
    }

    @BeforeAll
    public static void init()
    {
        api = new Api();
        timeStrategy = new TimeStrategy();
        distanceStrategy = new DistanceStrategy();
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
    public void testGet_shorted_path_time(){
        network.addEdge(new Trajet<>(A,coordGPS,B,coordGPS,ligne1,85l,100.));
        network.addEdge(new Trajet<>(A,coordGPS,C,coordGPS,ligne2,217l,100.));
        network.addEdge(new Trajet<>(A,coordGPS,E,coordGPS,ligne3,173l,100.));
        network.addEdge(new Trajet<>(B,coordGPS,F,coordGPS,ligne1,80l,100.));
        network.addEdge(new Trajet<>(C,coordGPS,G,coordGPS,ligne2,186l,100.));
        network.addEdge(new Trajet<>(C,coordGPS,H,coordGPS,ligne4,103l,100.));
        network.addEdge(new Trajet<>(H,coordGPS,D,coordGPS,ligne4,183l,100.));
        network.addEdge(new Trajet<>(E,coordGPS,J,coordGPS,ligne3,502l,100.));
        network.addEdge(new Trajet<>(F,coordGPS,I,coordGPS,ligne1,250l,100.));
        network.addEdge(new Trajet<>(H,coordGPS,J,coordGPS,ligne42,167l,100.));
        network.addEdge(new Trajet<>(I,coordGPS,J,coordGPS,ligne1,84l,100.));

        ligne1.addTrajet(new Trajet<>(A,coordGPS,B,coordGPS,ligne1,85l,100.));
        ligne1.addTrajet(new Trajet<>(B,coordGPS,F,coordGPS,ligne1,80l,100.));
        ligne1.addTrajet(new Trajet<>(F,coordGPS,I,coordGPS,ligne1,250l,100.));
        ligne1.addTrajet(new Trajet<>(I,coordGPS,J,coordGPS,ligne1,84l,100.));

        ligne2.addTrajet(new Trajet<>(A,coordGPS,C,coordGPS,ligne2,217l,100.));
        ligne2.addTrajet(new Trajet<>(C,coordGPS,G,coordGPS,ligne2,186l,100.));

        ligne3.addTrajet(new Trajet<>(A,coordGPS,E,coordGPS,ligne3,173l,100.));
        ligne3.addTrajet(new Trajet<>(E,coordGPS,J,coordGPS,ligne3,502l,100.));

        ligne4.addTrajet(new Trajet<>(C,coordGPS,H,coordGPS,ligne4,103l,100.));
        ligne4.addTrajet(new Trajet<>(H,coordGPS,D,coordGPS,ligne4,183l,100.));

        ligne42.addTrajet(new Trajet<>(H,coordGPS,J,coordGPS,ligne42,167l,100.));
        
        List<Trajet<Station>> res = timeStrategy.path(network, A, J, LocalTime.now());
        assertEquals(A, res.get(0).getSource());
        assertEquals(C, res.get(0).getDestination());
        assertEquals(C, res.get(1).getSource());
        assertEquals(H, res.get(1).getDestination());
        assertEquals(H, res.get(2).getSource());
        assertEquals(J, res.get(2).getDestination());
        assertEquals(3, res.size());
    }

    @Test
    public void testGet_shorted_path_distance(){
        network.addEdge(new Trajet<>(A,coordGPS,B,coordGPS,ligne1,85l,85.));
        network.addEdge(new Trajet<>(A,coordGPS,C,coordGPS,ligne2,217l,217.));
        network.addEdge(new Trajet<>(A,coordGPS,E,coordGPS,ligne3,173l,173.));
        network.addEdge(new Trajet<>(B,coordGPS,F,coordGPS,ligne1,80l,80.));
        network.addEdge(new Trajet<>(C,coordGPS,G,coordGPS,ligne2,186l,186.));
        network.addEdge(new Trajet<>(C,coordGPS,H,coordGPS,ligne4,103l,103.));
        network.addEdge(new Trajet<>(H,coordGPS,D,coordGPS,ligne4,183l,183.));
        network.addEdge(new Trajet<>(E,coordGPS,J,coordGPS,ligne3,502l,0.));
        network.addEdge(new Trajet<>(F,coordGPS,I,coordGPS,ligne1,250l,250.));
        network.addEdge(new Trajet<>(H,coordGPS,J,coordGPS,ligne42,167l,167.));
        network.addEdge(new Trajet<>(I,coordGPS,J,coordGPS,ligne1,84l,84.));

        List<Trajet<Station>> res = distanceStrategy.path(network, A, J, LocalTime.now());
        assertEquals(A, res.get(0).getSource());
        assertEquals(E, res.get(0).getDestination());
        assertEquals(E, res.get(1).getSource());
        assertEquals(J, res.get(1).getDestination());
        assertEquals(2, res.size());
    }

    @Test
    public void testGet_shorted_path_sourceIsDestination(){
        network.addEdge(new Trajet<>(A,coordGPS,B,coordGPS,ligne1,85l,100.));
        network.addEdge(new Trajet<>(A,coordGPS,C,coordGPS,ligne2,217l,100.));
        network.addEdge(new Trajet<>(A,coordGPS,E,coordGPS,ligne3,173l,100.));
        network.addEdge(new Trajet<>(B,coordGPS,F,coordGPS,ligne1,80l,100.));
        network.addEdge(new Trajet<>(C,coordGPS,G,coordGPS,ligne2,186l,100.));
        network.addEdge(new Trajet<>(C,coordGPS,H,coordGPS,ligne4,103l,100.));
        network.addEdge(new Trajet<>(H,coordGPS,D,coordGPS,ligne4,183l,100.));
        network.addEdge(new Trajet<>(E,coordGPS,J,coordGPS,ligne3,502l,100.));
        network.addEdge(new Trajet<>(F,coordGPS,I,coordGPS,ligne1,250l,100.));
        network.addEdge(new Trajet<>(H,coordGPS,J,coordGPS,ligne42,167l,100.));
        network.addEdge(new Trajet<>(I,coordGPS,J,coordGPS,ligne1,84l,100.));

        ligne1.addTrajet(new Trajet<>(A,coordGPS,B,coordGPS,ligne1,85l,100.));
        ligne1.addTrajet(new Trajet<>(B,coordGPS,F,coordGPS,ligne1,80l,100.));
        ligne1.addTrajet(new Trajet<>(F,coordGPS,I,coordGPS,ligne1,250l,100.));
        ligne1.addTrajet(new Trajet<>(I,coordGPS,J,coordGPS,ligne1,84l,100.));

        ligne2.addTrajet(new Trajet<>(A,coordGPS,C,coordGPS,ligne2,217l,100.));
        ligne2.addTrajet(new Trajet<>(C,coordGPS,G,coordGPS,ligne2,186l,100.));

        ligne3.addTrajet(new Trajet<>(A,coordGPS,E,coordGPS,ligne3,173l,100.));
        ligne3.addTrajet(new Trajet<>(E,coordGPS,J,coordGPS,ligne3,502l,100.));

        ligne4.addTrajet(new Trajet<>(C,coordGPS,H,coordGPS,ligne4,103l,100.));
        ligne4.addTrajet(new Trajet<>(H,coordGPS,D,coordGPS,ligne4,183l,100.));

        ligne42.addTrajet(new Trajet<>(H,coordGPS,J,coordGPS,ligne42,167l,100.));

        assertEquals(0, timeStrategy.path(network, C, C, LocalTime.now()).size());
        assertEquals(0, distanceStrategy.path(network, C, C, LocalTime.now()).size());
    }

    // TODO: tester getNearestStation
    @Test
    public void testGetNearestStation() {
        DirectedLigne dl_14 = api.getDirectedLigne("14",api.getStation("Olympiades"));
        assertEquals(
                api.getStation("Bercy"),
                distanceStrategy.getNearestStation(api.getNetwork(),api.getStation("Bercy").getCoordGPS(dl_14))
        );
        assertEquals(
                api.getStation("Bercy"),
                timeStrategy.getNearestStation(api.getNetwork(),api.getStation("Bercy").getCoordGPS(dl_14))
        );

        assertNotEquals(
                api.getStation("Bibliothèque François-Mitterrand"),
                timeStrategy.getNearestStation(api.getNetwork(),api.getStation("Bercy").getCoordGPS(dl_14))
        );

        assertNotEquals(
                api.getStation("Bibliothèque François-Mitterrand"),
                timeStrategy.getNearestStation(api.getNetwork(),api.getStation("Bercy").getCoordGPS(dl_14))
        );
    }
}
