package fr.hydrogen.cityconnect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.hydrogen.cityconnect.model.CoordGPS;
import fr.hydrogen.cityconnect.model.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ApiTest 
{

    private static Api api;

    @BeforeAll
    public static void initialize() {
        api = new Api();
    }

    @Test
    public void testGetStation()
    {
        assertEquals(
            "Nation",
            api.getStation("Nation").toString()
        );

        assertEquals(
            5,
            api.getStation("Olympiades").getVariantsLigne().size()
        );
    }

    @Test
    public void testGetStations()
    {
        assertEquals(
                api.getStations(api.getDirectedLigne("1", api.getDirection1("1"))),
                api.getStations("1", 1)
        );
        assertEquals(
                api.getStations(api.getDirectedLigne("1", api.getDirection2("1"))),
                api.getStations("1", 2)
        );
    }

    @Test
    public void testGetLignes()
    {
        assertEquals(16, api.getLignes().size());
    }

    @Test
    public void testGetDirection()
    {
        List<Station> directions = new ArrayList<>();
        directions.add(api.getStation("Nation"));
        directions.add(api.getStation("Porte Dauphine"));
        assertTrue(directions.contains(api.getDirection1("2")));
        assertTrue(directions.contains(api.getDirection2("2")));
        assertTrue(directions.contains(api.getDirection1(api.getLigne("2"))));
        assertTrue(directions.contains(api.getDirection2(api.getLigne("2"))));
    }

    @Test
    public void testGetDirectedLigne()
    {
        assertEquals(
                api.getDirection1("1"),
                api.getDirectedLigne("1",api.getDirection1("1")).getArrivee()
        );
        assertEquals(
                api.getDirection1("1"),
                api.getDirectedLigne(api.getLigne("1"),api.getDirection1("1")).getArrivee()
        );
    }

    @Test
    public void testGetShortestPathStations()
    {
        assertEquals(
            3,
            api.getShortestPath("Nation", "Daumesnil").size()
        );
        System.out.println(api.getShortestPath("Nation", "Daumesnil"));

        assertEquals(
            3,
            api.getShortestPath(api.getStation("Olympiades"), api.getStation("Bercy")).size()
        );
        System.out.println(api.getShortestPath("Olympiades", "Bercy"));
    }

    @Test
    public void testGetShortestPathCoordGPS()
    {
        CoordGPS c1 = api.getStation("Olympiades").getAllCoordGPS().stream().findAny().get();
        CoordGPS c2 = api.getStation("Bercy").getAllCoordGPS().stream().findAny().get();
        assertEquals(
                3,
                api.getShortestPath(c1, c2).size()
        );
    }
    
    @Test
    public void testGetHorairesDepart()
    {
        assertEquals(
            api.getLigne("14").getVariant(2).getHoraires(),
            api.getHoraires(
                api.getStation("Olympiades"), 
                api.getLigne("14").getDirectedLigne(api.getLigne("14").getDirection1()))
        );

        assertEquals(
            api.getLigne("14").getVariant(1).getHoraires().size()+
            api.getLigne("14").getVariant(2).getHoraires().size()+
            api.getLigne("14").getVariant(4).getHoraires().size()+
            api.getLigne("14").getVariant(5).getHoraires().size()
            ,
            api.getHoraires(
                api.getStation("Gare de Lyon"), 
                api.getLigne("14").getDirectedLigne(api.getLigne("14").getDirection1())).size()
        );

        assertEquals(
            api.getLigne("14").getVariant(1).getHoraires().size()+
            api.getLigne("14").getVariant(2).getHoraires().size()+
            api.getLigne("14").getVariant(5).getHoraires().size()
            ,
            api.getHoraires(
                api.getStation("Bercy"), 
                api.getLigne("14").getDirectedLigne(api.getLigne("14").getDirection1())).size()
        );
    }

    @Test
    public void testAutocomplete()
    {
        List<String> list = api.autocomplete("Natione");
        assertTrue(list.contains(api.getStation("Nation").name()));
        assertTrue(list.contains(api.getStation("Nationale").name()));
    }

    @Test
    public void testGetNetwork()
    {
        assertEquals(16,api.getNetwork().getLignes().size());
    }

}
