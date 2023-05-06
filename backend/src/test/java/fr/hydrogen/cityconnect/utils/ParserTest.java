package fr.hydrogen.cityconnect.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.hydrogen.cityconnect.model.*;
import fr.hydrogen.cityconnect.service.DistanceStrategy;
import fr.hydrogen.cityconnect.service.Strategy;
import fr.hydrogen.cityconnect.service.TimeStrategy;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class ParserTest {
    
    private static Network<Station> network;
    private static Strategy strategy;

    @BeforeAll
    public static void initialize() throws IOException {
        network = new Network<>();
        strategy = new TimeStrategy();
        Parser.parseGraph("static/map_data.csv", network);
        Parser.parseTimetables("static/timetables.csv", network);
    }

    private static void printShortestPath(Station source, Station destination) {
        List<Trajet<Station>> path = strategy.path(network, source, destination, LocalTime.now());
        System.out.print("Le plus court chemin entre "+source+" et "+destination+ " par rapport ");
        if (strategy instanceof DistanceStrategy) {System.out.println("à la distance:");}
        else {System.out.println("au temps:");}
        System.out.println(path);
    }

    @Test
    public void testParseGraph()
    {
        assertEquals(308, network.getVertexSet().size());

        // TODO: tester correctement

        printShortestPath(
            network.getVariantLigne("6", 1).getStations().get(0),
            network.getVariantLigne("6", 1).getStations().get(3)
        );

        strategy = new DistanceStrategy();

        printShortestPath(
            network.getVariantLigne("6", 1).getStations().get(0),
            network.getVariantLigne("6", 1).getStations().get(3)
        );

        printShortestPath(
            network.getVariantLigne("6", 1).getStations().get(0),
            network.getVariantLigne("6", 1).getStations().get(4)
        );

        printShortestPath(
            network.getVertex("Nation"),
            network.getVertex("Bibliothèque François Mitterrand")
        );
    }

    @Test
    public void testParseLignes()
    {
        assertEquals(16, network.getLignes().size());
        
        assertEquals(5, network.getLigne("8").getVariants().size());
        assertEquals(9, network.getLigne("14").getVariants().size());
        assertEquals(4, network.getLigne("3").getVariants().size());
        assertEquals(4, network.getLigne("10").getVariants().size());
        assertEquals(6, network.getLigne("5").getVariants().size());
        assertEquals(5, network.getLigne("11").getVariants().size());
        assertEquals(4, network.getLigne("12").getVariants().size());
        assertEquals(2, network.getLigne("3B").getVariants().size());
        assertEquals(6, network.getLigne("1").getVariants().size());
        assertEquals(9, network.getLigne("9").getVariants().size());
        assertEquals(11, network.getLigne("7").getVariants().size());
        assertEquals(7, network.getLigne("13").getVariants().size());
        assertEquals(8, network.getLigne("2").getVariants().size());
        assertEquals(6, network.getLigne("6").getVariants().size());
        assertEquals(5, network.getLigne("4").getVariants().size());
        assertEquals(2, network.getLigne("7B").getVariants().size());

        assertEquals(7, network.getVariantLigne("7B", 1).getStations().size());
        assertEquals(7, network.getVariantLigne("7B", 2).getStations().size());

        assertEquals(28, network.getVariantLigne("6", 1).getStations().size());
    }

    @Test
    public void testParseTimetables()
    {
        assertEquals(73 , network.getVariantLigne("1", 3).getHoraires().size());
        System.out.println("Horaires ligne 1 variant 3: ");
        System.out.println(network.getVariantLigne("1", 3).getHoraires());
    }

    @Test
    public void testParseCoordGPS()
    {
        Station bfm = network.getVertex("Bibliothèque François Mitterrand");
        assertEquals(
            new CoordGPS(2.376487371168301,48.829925765928905),
            bfm.getCoordGPS(network.getVariantLigne("14", 2)));
        assertEquals(
            new CoordGPS(2.3764237443204546,48.8300063775369),
            bfm.getCoordGPS(network.getVariantLigne("14", 3)));

        assertEquals(
            bfm.getCoordGPS(network.getVariantLigne("14", 2)),
            bfm.getCoordGPS(network.getVariantLigne("14", 5)));

        assertEquals(
            bfm.getCoordGPS(network.getVariantLigne("14", 6)),
            bfm.getCoordGPS(network.getVariantLigne("14", 3)));

        assertEquals(
            bfm.getCoordGPS(network.getVariantLigne("14", 7)),
            bfm.getCoordGPS(network.getVariantLigne("14", 3)));

        assertEquals(
            bfm.getCoordGPS(network.getVariantLigne("14", 9)),
            bfm.getCoordGPS(network.getVariantLigne("14", 3)));
    }

    @Test
    public void testParseLigneStation()
    {
        Station bfm = network.getVertex("Bibliothèque François Mitterrand");
        assertEquals(6, bfm.getVariantsLigne().size());
        System.out.println("Lignes passant par "+bfm+": ");
        System.out.println(bfm.getVariantsLigne());
        for (VariantLigne ligne : bfm.getVariantsLigne()) {
            assertEquals("14", ligne.getNumero());
        }
    }

}