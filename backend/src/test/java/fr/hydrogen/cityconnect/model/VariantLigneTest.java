package fr.hydrogen.cityconnect.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.hydrogen.cityconnect.model.graph.Vertex;

public class VariantLigneTest {

    private VariantLigne LIGNE_8_2;
    private VariantLigne LIGNE_8_3;
    private Ligne LIGNE_8;

    private static Station balard;
    private static Station lourmel;
    private static Station boucicaut;
    private static Station felix_faure;
    private static Station commerce;
    private static CoordGPS coordGPS;

    @BeforeAll
    public static void initStatic()
    {
        balard = new Station("Balard", null);
        lourmel = new Station("Lourmel", null);
        boucicaut = new Station("Boucicaut", null);
        felix_faure = new Station("Félix Faure", null);
        commerce = new Station("Commerce", null);
        coordGPS = new CoordGPS(0,0);
    }
    
    @BeforeEach
    public void initialize()
    {
        LIGNE_8 = new Ligne("8");

        LIGNE_8_2 = new VariantLigne(LIGNE_8, 2);
        LIGNE_8_2.addTrajet(new Trajet<>(balard, coordGPS, lourmel, coordGPS, LIGNE_8_2, 0, 0));
        LIGNE_8_2.addTrajet(new Trajet<>(lourmel, coordGPS, boucicaut, coordGPS, LIGNE_8_2, 0, 0));
        LIGNE_8_2.addTrajet(new Trajet<>(boucicaut, coordGPS, felix_faure, coordGPS, LIGNE_8_2, 0, 0));
        LIGNE_8_2.addTrajet(new Trajet<>(felix_faure, coordGPS, commerce, coordGPS, LIGNE_8_2, 0, 0));
        LIGNE_8.addVariantLigne(LIGNE_8_2);
        
        
        LIGNE_8_3 = new VariantLigne(LIGNE_8, 3);
        LIGNE_8_3.addTrajet(new Trajet<>(commerce, coordGPS, felix_faure, coordGPS, LIGNE_8_3, 0, 0));
        LIGNE_8_3.addTrajet(new Trajet<>(felix_faure, coordGPS, boucicaut, coordGPS, LIGNE_8_3, 0, 0));
        LIGNE_8_3.addTrajet(new Trajet<>(boucicaut, coordGPS, lourmel, coordGPS, LIGNE_8_3, 0, 0));
        LIGNE_8_3.addTrajet(new Trajet<>(lourmel, coordGPS, balard, coordGPS, LIGNE_8_3, 0, 0));
        LIGNE_8.addVariantLigne(LIGNE_8_3);
    }
    
    @Test
    public void testAddHoraire(){
        assertTrue(LIGNE_8_2.addHoraire(LocalTime.of(8, 33)));
        assertTrue(LIGNE_8_2.addHoraire(LocalTime.of(8, 43)));
        assertEquals(2, LIGNE_8_2.getHorairesDepart().size());
    }

    @Test
    public void testGetNumero(){
        assertEquals("8", LIGNE_8_2.getNumero());
        assertEquals("8", LIGNE_8_3.getNumero());
    }

    @Test
    public void testGetVariant(){
        assertEquals(2, LIGNE_8_2.getVariant());
        assertEquals(3, LIGNE_8_3.getVariant());
    }
    
    @Test
    public void testGetStations(){
        List<Vertex> stations = new ArrayList<>();
        stations.add(balard);
        stations.add(lourmel);
        stations.add(boucicaut);
        stations.add(felix_faure);
        stations.add(commerce);
        assertEquals(stations, LIGNE_8_2.getStations());
        Collections.reverse(stations);
        assertEquals(stations, LIGNE_8_3.getStations());
        assertNotEquals(stations, LIGNE_8_2.getStations());
    }

    @Test
    public void testGetTrajets(){
        assertEquals(4, LIGNE_8_2.getTrajets().size());
        assertEquals(4, LIGNE_8_3.getTrajets().size());
        assertNotEquals(LIGNE_8_3.getTrajets(), LIGNE_8_2.getTrajets());
        List<Trajet<Station>> trajets = LIGNE_8_3.getTrajets();
        Collections.reverse(trajets);
        assertNotEquals(trajets, LIGNE_8_2.getTrajets());
    }
    
    @Test
    public void testGetDepart(){
        assertEquals(balard, LIGNE_8_2.getDepart());
        assertEquals(commerce, LIGNE_8_3.getDepart());
    }

    @Test
    public void testGetArrivee(){
        assertEquals(commerce, LIGNE_8_2.getArrivee());
        assertEquals(balard, LIGNE_8_3.getArrivee());
    }

    @Test
    public void testGetHoraires(){
        assertEquals(0, LIGNE_8_2.getHoraires().size());
        LIGNE_8_2.addHoraire(LocalTime.parse("08:33"));
        assertEquals(1, LIGNE_8_2.getHoraires().size());
        assertEquals(LocalTime.of(8, 33), LIGNE_8_2.getHoraires().get(0));
        LIGNE_8_2.addHoraire(LocalTime.parse("08:43"));
        assertEquals(2, LIGNE_8_2.getHoraires().size());
    }

    @Test
    public void testGetHorairesIsSorted(){
        assertEquals(0, LIGNE_8_2.getHoraires().size());
        LIGNE_8_2.addHoraire(LocalTime.parse("08:33"));
        assertEquals(LocalTime.of(8, 33), LIGNE_8_2.getHoraires().get(0));
        LIGNE_8_2.addHoraire(LocalTime.parse("08:23"));
        assertEquals(LocalTime.of(8, 23), LIGNE_8_2.getHoraires().get(0));
        assertEquals(LocalTime.of(8, 33), LIGNE_8_2.getHoraires().get(1));
    }

}
