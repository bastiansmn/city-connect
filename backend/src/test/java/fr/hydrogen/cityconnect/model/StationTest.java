package fr.hydrogen.cityconnect.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StationTest {

    private Station GARE_DE_LYON;
    private Network<Station> network;

    @BeforeEach
    public void initialize()
    {
        network = new Network<>();
        GARE_DE_LYON = new Station("Gare de Lyon", network);
    }

    @Test
    public void testGetNom()
    {
        assertEquals("Gare de Lyon", GARE_DE_LYON.name());
    }

    @Test
    public void testGetCoordGPS()
    {
        // TODO: test getCoordGPS
    }
    
    @Test
    public void testGetLignes()
    {
        // TODO: test getLignes()
    }

    @Test
    public void testGetHorairesLigne()
    {
        // TODO: test getHoraires(Ligne) et impl
    }

    @Test
    public void testToString()
    {
        Station station = new Station("Gare de Lyon", network);
        String expected = "Gare de Lyon";
        assertEquals(expected, station.toString());
    }

    @Test
    public void testEquals()
    {
        Station station = new Station("Gare de Lyon", network);
        // CoordGPS coordGPS = new CoordGPS(1, 1);
        Station porteDeVersailles1 = new Station("Porte de Versailles", network);
        // porteDeVersailles1.addCoordGPS(coordGPS);
        Station porteDeVersailles2 = new Station("Porte de Versailles", network);
        // porteDeVersailles2.addCoordGPS(coordGPS);
        assertEquals(GARE_DE_LYON, station);
        assertEquals(porteDeVersailles1, porteDeVersailles2);
    }

    @Test
    public void testEqualsOnlyOnNom()
    {
        Station station = new Station("Gare de Lyon", network);
        // GARE_DE_LYON.addCoordGPS(new CoordGPS(1,1));
        // station.addCoordGPS(new CoordGPS(2,2));
        assertEquals(GARE_DE_LYON, station);
    }

    @Test
    public void testHashCode()
    {
        Station station = new Station("Gare de Lyon", network);
        Station porteDeVersailles1 = new Station("Porte de Versailles", network);
        Station porteDeVersailles2 = new Station("Porte de Versailles", network);
        assertEquals(GARE_DE_LYON.hashCode(), station.hashCode());
        assertEquals(porteDeVersailles1.hashCode(), porteDeVersailles2.hashCode());
        assertNotEquals(GARE_DE_LYON.hashCode(), porteDeVersailles1.hashCode());
    }

}
