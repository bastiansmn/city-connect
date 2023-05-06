package fr.hydrogen.cityconnect.model;

import org.junit.jupiter.api.Test;

import fr.hydrogen.cityconnect.model.graph.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;

public class EdgeTest
{
    private static class EdgeImplTest extends Edge<Vertex> {
        public EdgeImplTest(Vertex source, Vertex destination){
            super(source, destination);
        }
    }

    private static Edge<Vertex> edge;
    private static Vertex source;
    private static Vertex destination;


    @BeforeAll
    public static void initialize()
    {
        source = new VertexTestImpl("A");
        destination = new VertexTestImpl("B");
        edge = new EdgeImplTest(source, destination);
    }

    @Test
    public void testGetSource(){
        assertEquals(source, edge.getSource());
    }

    @Test
    public void testGetDestination(){
        assertEquals(destination, edge.getDestination());
    }

    @Test
    public void testToString(){
        // Edge<String> edge = new Edge<>("A", "B");
        String expected = "(A -> B)";
        assertEquals(expected, edge.toString());
    }

    @Test
    public void testEquals(){
        assertEquals(new EdgeImplTest(source,destination), edge);
    }

    @Test
    public void testHashCode(){
        assertEquals(new EdgeImplTest(source,destination).hashCode(), edge.hashCode());
    }

}
