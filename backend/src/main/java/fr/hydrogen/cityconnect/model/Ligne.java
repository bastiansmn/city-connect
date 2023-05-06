package fr.hydrogen.cityconnect.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Ligne {

    private final DirectedLigne directedLigne1;
    private final DirectedLigne directedLigne2;

    private final String numero;

    /**
     * Constructor
     * @param numero
     */
    public Ligne(String numero) {
        this.numero = numero;
        this.directedLigne1 = new DirectedLigne(this);
        this.directedLigne2 = new DirectedLigne(this);
    }

    /**
     * This function gets the direction 1 of the ligne
     * @return the direction 1 of the ligne
     */
    public Station getDirection1() {
        return directedLigne1.getArrivee();
    }

    /**
     * This function gets the direction 2 of the ligne
     * @return the direction 2 of the ligne
     */
    public Station getDirection2() {
        return directedLigne2.getArrivee();
    }

    /**
     * This function gets the directed ligne of the ligne with the arrival station "arrivee"
     * @param arrivee
     * @return the directed ligne of the ligne
     */
    public DirectedLigne getDirectedLigne(Station arrivee) {
        if (directedLigne1.getArrivee().equals(arrivee)) {
            return directedLigne1;
        }
        else if (directedLigne2.getArrivee().equals(arrivee)) {
            return directedLigne2;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This function adds the parameter variant ligne to the ligne
     * @param variantLigne
     */
    public void addVariantLigne(VariantLigne variantLigne) {
        if (directedLigne1.memeSens(variantLigne)) {
            directedLigne1.addVariant(variantLigne);
        } else if (directedLigne2.memeSens(variantLigne)) {
            directedLigne2.addVariant(variantLigne);
        }  else {
            throw new RuntimeException();
        }
    }

    /**
     * This function gets the variants of the ligne
     * @return a list of the variants of the ligne
     */
    public List<VariantLigne> getVariants() {
        List<VariantLigne> res = new ArrayList<>();
        res.addAll(directedLigne1.getVariantLignes());
        res.addAll(directedLigne2.getVariantLignes());
        return res;
    }

    /**
     * This function gets the variant ligne of the ligne with the variant "variant"
     * @param variant
     * @return the variant ligne of the ligne
     */
    public VariantLigne getVariant(int variant) {
        for (VariantLigne variantLigne : directedLigne1.getVariantLignes()) {
            if (variantLigne.getVariant() == variant) {
                return variantLigne;
            }
        }
        for (VariantLigne variantLigne : directedLigne2.getVariantLignes()) {
            if (variantLigne.getVariant() == variant) {
                return variantLigne;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * This function returns the string of the number "numero" of the ligne
     * @return the string of the number "numero" of the ligne
     */
    @Override
    public String toString() {
        return this.numero;
    }
    
}
