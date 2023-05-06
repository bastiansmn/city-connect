package fr.hydrogen.cityconnect.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CoordGPSTest {

    private static CoordGPS coordGPS;

    @BeforeAll
    public static void initialize(){
        coordGPS = new CoordGPS(1, 1);
    }

    @Test
    public void testGet(){
        assertEquals(1., coordGPS.x());
        assertEquals(1., coordGPS.y());
    }

    @Test
    public void testEquals(){
        assertEquals(coordGPS, new CoordGPS(coordGPS));
        assertEquals(coordGPS, new CoordGPS(1,1));
        assertEquals(coordGPS, new CoordGPS(1.,1.));
        assertEquals(coordGPS, new CoordGPS(1.0,1.0));
        assertEquals(coordGPS, new CoordGPS(1l,1l));
        assertNotEquals(coordGPS, new CoordGPS(1,1.1));
        assertNotEquals(coordGPS, new CoordGPS(1,2));
    }

    @Test
    public void testHashCode(){
        assertEquals(coordGPS.hashCode(), new CoordGPS(coordGPS).hashCode());
        assertEquals(coordGPS.hashCode(), new CoordGPS(1,1).hashCode());
        assertEquals(coordGPS.hashCode(), new CoordGPS(1.,1.).hashCode());
        assertEquals(coordGPS.hashCode(), new CoordGPS(1.0,1.0).hashCode());
        assertEquals(coordGPS.hashCode(), new CoordGPS(1l,1l).hashCode());
        assertNotEquals(coordGPS.hashCode(), new CoordGPS(1,2).hashCode());
    }

    @Test
    public void testToString(){
        assertEquals("(1.0,1.0)", coordGPS.toString());
    }
    
}
