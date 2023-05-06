package fr.hydrogen.cityconnect.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.hydrogen.cityconnect.model.graph.Vertex;

public class TrajetTest {

    private static Vertex vertexA;
    private static Vertex vertexB;
    private static CoordGPS gpsA;
    private static CoordGPS gpsB;
    private static VariantLigne variantLigne;
    private static Trajet<Vertex> trajetAB;
    private static Trajet<Vertex> trajetBA;

    @BeforeAll
    public static void initialize()
    {
        vertexA = new VertexTestImpl("A");
        vertexB = new VertexTestImpl("B");
        gpsA = new CoordGPS(1, 1);
        gpsA = new CoordGPS(2, 2);
        variantLigne = new VariantLigne(new Ligne("1"),1);
        trajetAB = new Trajet<>(vertexA, gpsA, vertexB, gpsB, variantLigne, 120, 200);
        trajetBA = new Trajet<>(vertexB, gpsA, vertexA, gpsB, variantLigne, 120, 200);
    }

    @Test
    public void testGetSource(){
        assertEquals(vertexA, trajetAB.getSource());
        assertEquals(vertexB, trajetBA.getSource());
    }

    @Test
    public void testGetDestination(){
        assertEquals(vertexB, trajetAB.getDestination());
        assertEquals(vertexA, trajetBA.getDestination());
    }

    @Test
    public void testGetTime(){
        assertEquals(Duration.ofSeconds(120), trajetAB.getTime());
        assertEquals(Duration.ofSeconds(120), trajetBA.getTime());
    }

    @Test
    public void testGetDistance(){
        assertEquals(200., trajetAB.getDistance());
        assertEquals(200., trajetAB.getDistance());
    }

    @Test
    public void testEquals() {
        Trajet<Vertex> t_ab = new Trajet<>(vertexA, gpsA, vertexB, gpsB, variantLigne, Duration.ofSeconds(120), 200);
        assertEquals(trajetAB, t_ab);
        assertNotEquals(trajetBA, trajetAB);
        assertNotEquals(trajetBA, t_ab);
    }

    @Test
    public void testHashCode() {
        Trajet<Vertex> t_ab = new Trajet<>(vertexA, gpsA, vertexB, gpsB, variantLigne, Duration.ofSeconds(120), 200);
        assertEquals(trajetAB.hashCode(), t_ab.hashCode());
        assertNotEquals(trajetBA.hashCode(), trajetAB.hashCode());
        assertNotEquals(trajetBA.hashCode(), t_ab.hashCode());
    }

}
