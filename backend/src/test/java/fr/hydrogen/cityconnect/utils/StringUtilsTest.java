package fr.hydrogen.cityconnect.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void removeAccentsTest() {
        assertEquals("etude", StringUtils.removeAccents("étude"));
        assertEquals("Etude", StringUtils.removeAccents("Étude"));
        assertEquals("A", StringUtils.removeAccents("À"));
    }

    @Test
    public void levenshteinDistanceTest() {
        assertEquals(5, StringUtils.levenshteinDistance("Chiens","Niche"));
        assertEquals(3, StringUtils.levenshteinDistance("Jeanne Dupont","Jean Dupond"));
        assertEquals(1, StringUtils.levenshteinDistance("Jeanne Dupont","Jeanne Dupond"));
        assertEquals(9, StringUtils.levenshteinDistance("Jeanne Dupont","Marie Blanc"));
    }

}
