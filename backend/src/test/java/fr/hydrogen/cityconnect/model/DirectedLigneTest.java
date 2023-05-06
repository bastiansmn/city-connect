package fr.hydrogen.cityconnect.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectedLigneTest {

    private DirectedLigne DL_8_1;
    private DirectedLigne DL_8_2;

    private static VariantLigne LIGNE_8_2;
    private static VariantLigne LIGNE_8_3;
    private static Ligne LIGNE_8;

    private static Station balard;
    private static Station lourmel;
    private static Station boucicaut;
    private static Station felix_faure;
    private static Station commerce;
    private static CoordGPS coordGPS;

    @BeforeAll
    public static void initStatic() {
        balard = new Station("Balard", null);
        lourmel = new Station("Lourmel", null);
        boucicaut = new Station("Boucicaut", null);
        felix_faure = new Station("Félix Faure", null);
        commerce = new Station("Commerce", null);
        coordGPS = new CoordGPS(0,0);
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

    @BeforeEach
    public void initialize() {
        this.DL_8_1 = new DirectedLigne(LIGNE_8);
        this.DL_8_1.addVariant(LIGNE_8_2);
        this.DL_8_2 = new DirectedLigne(LIGNE_8);
        this.DL_8_2.addVariant(LIGNE_8_3);

    }

    @Test
    public void testGetLigne() {
        assertEquals(LIGNE_8, DL_8_1.getLigne());
        assertEquals(LIGNE_8, DL_8_2.getLigne());
    }

    @Test
    public void testGetDepart() {
        assertEquals(balard, DL_8_1.getDepart());
        assertEquals(commerce, DL_8_2.getDepart());
    }

    @Test
    public void testGetArrivee() {
        assertEquals(commerce, DL_8_1.getArrivee());
        assertEquals(balard, DL_8_2.getArrivee());
    }

    @Test
    public void testContainsVariant() {
        assertTrue(DL_8_1.containsVariant(2));
        assertTrue(DL_8_2.containsVariant(3));
        assertFalse(DL_8_1.containsVariant(3));
        assertFalse(DL_8_2.containsVariant(2));
    }

    @Test
    public void testMemeSens() {
        assertTrue(DL_8_1.memeSens(LIGNE_8_2));
        assertTrue(DL_8_2.memeSens(LIGNE_8_3));
        assertFalse(DL_8_1.memeSens(LIGNE_8_3));
        assertFalse(DL_8_2.memeSens(LIGNE_8_2));
    }

    @Test
    public void testGetVariantLignes() {
        assertTrue(DL_8_1.getVariantLignes().contains(LIGNE_8_2));
        assertEquals(1, DL_8_1.getVariantLignes().size());
        assertTrue(DL_8_2.getVariantLignes().contains(LIGNE_8_3));
        assertEquals(1, DL_8_2.getVariantLignes().size());
    }

    // TODO: Test getHoraires(Station)

}
