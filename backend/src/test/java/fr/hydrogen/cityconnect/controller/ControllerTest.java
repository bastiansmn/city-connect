package fr.hydrogen.cityconnect.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.hydrogen.cityconnect.service.Api;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTest {

    private static MetroController controller;
    private static Api api;

    @BeforeAll
    public static void initStatic() {
        api = new Api();
        controller = new MetroController(api);
    }

    @Test
    public void testRequestItineraire() throws JsonProcessingException {
        // TODO: handle error when same start and finish
        // assertEquals("[]", controller.handleRequest("Hoche","Hoche"));

        String response1 = controller.handleRequest("Bercy","Olympiades");
        System.out.println(response1);
        assertTrue(response1.contains("Bercy"));
        assertTrue(response1.contains("Cour Saint-Emilion"));
        assertTrue(response1.contains("Bibliothèque François Mitterrand"));
        assertTrue(response1.contains("Olympiades"));

        // TODO: tester la requete avec des coordonnées
//        String response2 = controller.handleRequest(
//                api.getStation("Bercy").getAllCoordGPS().stream().findAny().get().toString(),
//                api.getStation("Bercy").getAllCoordGPS().stream().findAny().get().toString()
//        );
//        assertTrue(response2.contains("Bercy"));
//        assertTrue(response2.contains("Cour Saint-Emilion"));
//        assertTrue(response2.contains("Bibliothèque François Mitterrand"));
//        assertTrue(response2.contains("Olympiades"));
    }

    @Test
    public void testRequestLignes() throws JsonProcessingException {
        assertTrue(controller.metroLigne("Bercy").contains("14"));

        String response1 = controller.metroLigne("Nation");
        System.out.println(response1);
        assertTrue(response1.contains("1"));
        assertTrue(response1.contains("2"));
        assertTrue(response1.contains("6"));
        assertTrue(response1.contains("9"));
    }

    @Test
    public void testRequestDirections() throws JsonProcessingException {
        String response1 = controller.metroDirection("14");
        assertTrue(response1.contains("Olympiades"));
        assertTrue(response1.contains("Mairie de Saint-Ouen"));

        String response2 = controller.metroDirection("2");
        assertTrue(response2.contains("Nation"));
        assertTrue(response2.contains("Porte Dauphine"));
    }

    @Test
    public void testAutocomplete() {
        List<String> list = controller.autocomplete("Natione");
        assertTrue(list.contains("Nation"));
        assertTrue(list.contains("Nationale"));
    }

}
