package fr.hydrogen.cityconnect.model;

import fr.hydrogen.cityconnect.model.graph.Graph;
import fr.hydrogen.cityconnect.model.graph.Vertex;

import java.time.LocalTime;
import java.util.*;

public class Network<T extends Vertex> implements Graph<T, Trajet<T>> {

    /* it represents a map that store the vertex as key
     and their lists of trajets as value */
    private final Map<T, List<Trajet<T>>> outgoingEdgesMap;
    private final Map<T, List<Trajet<T>>> incomingEdgesMap;
    private final List<Ligne> lignes;

    /**
     * Constructor
     */
    public Network(){
        this.outgoingEdgesMap = new HashMap<>();
        this.incomingEdgesMap = new HashMap<>();
        this.lignes = new ArrayList<>();
    }

    /**
     * The function get the Vertex with the name "name"
     * @param name
     * @return a Vertex with the name "name"
     */
    @Override
    public T getVertex(String name) {
        for (T t : outgoingEdgesMap.keySet()) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * This function adds ligne to the list that contains Ligne
     * @param ligne
     * @return true if ligne is added successfully, false otherwise
     */
    public boolean addLigne(Ligne ligne) {
        if (this.lignes.stream().noneMatch(l->l.getNumero().equals(ligne.getNumero()))) {
            return this.lignes.add(ligne);
        }
        return false;
    }

    /**
     * This function get the list of Ligne
     * @return a list which contains ligne
     */
    public List<Ligne> getLignes() {
        return new ArrayList<>(lignes);
    }

    /**
     * This function get ligne whose number is "numero"
     * @param numero
     * @return the ligne whose number is numero
     * @throws NoSuchElementException
     */
    public Ligne getLigne(String numero) throws NoSuchElementException {
        for (Ligne ligne : lignes) {
            if (ligne.getNumero().equals(numero)) {
                return ligne;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * This function get VariantLigne with number "numero" and variant "variant"
     * @param numero of ligne
     * @param variant
     * @return VariantLigne
     */
    public VariantLigne getVariantLigne(String numero, int variant) {
        return getLigne(numero).getVariant(variant);
    }

    /**
     * This function adds horaire to VariantLigne with number "numero" and variant "variant"
     * @param numero
     * @param variant
     * @param horaire
     * @return true if Horaire is added successfully to the VariantLigne, false otherwise
     */
    public boolean addHoraire(String numero, int variant, LocalTime horaire){
        VariantLigne variantLigne = this.getVariantLigne(numero, variant);
        return variantLigne.addHoraire(horaire);
    }

    /**
     * This function add horaire to VariantLigne "ligne"
     * @param ligne
     * @param horaire
     * @return true if Horaire is added successfully to the VariantLigne, false otherwise
     */
    public boolean addHoraire(VariantLigne ligne, LocalTime horaire){
        return this.addHoraire(ligne.getNumero(), ligne.getVariant(), horaire);
    }

    /**
     * This function get a list of the departuretime with number "numero" and variant "variant"
     * @param numero
     * @param variant
     * @return a list of DepartureTime
     */
    public List<LocalTime> getHorairesDepart(String numero, int variant){
        VariantLigne variantLigne = this.getVariantLigne(numero, variant);
        return variantLigne.getHoraires();
    }

    /**
     * This function get a list of Departure Time with variantLigne "ligne"
     * @param ligne
     * @return a list of DepartureTime
     */
    public List<LocalTime> getHorairesDepart(VariantLigne ligne){
        return this.getHorairesDepart(ligne.getNumero(), ligne.getVariant());
    }

    /**
     * This function add a Vertex of type T
     * @param vertex
     * @return true if the vertex is added successfully, false otherwise
     */
    @Override
    public boolean addVertex(T vertex) {
        boolean result = false;
        if (outgoingEdgesMap.keySet().stream().noneMatch(k -> k.name().equals(vertex.name()))){
            outgoingEdgesMap.put(vertex , new LinkedList<>());
            incomingEdgesMap.put(vertex, new LinkedList<>());
            result = true;
        }
        return result;
    }

    /**
     * This function add Edge of type Trajet
     * @param edge
     * @return true if the edge is added successfully, false otherwise
     */
    @Override
    public boolean addEdge(Trajet<T> edge) {
        addVertex(edge.getSource());
        addVertex(edge.getDestination());
        List<Trajet<T>> outgoingEdges = getOutgoingEdges(edge.getSource());
        for (Trajet<T> outgoing: outgoingEdges){
            if (edge.equals(outgoing)){
                return false;
            }
        }
        List<Trajet<T>> incomingEdges = getIncomingEdges(edge.getDestination());
        for (Trajet<T> incoming: incomingEdges){
            if (edge.equals(incoming)){
                return false;
            }
        }
        outgoingEdges.add(edge);
        incomingEdges.add(edge);
        return true;
    }

    /**
     * This function get a list of the vertex adjacents to the Vertex "vertex"
     * @param vertex
     * @return a list of the vertex of type T
     */
    @Override
    public List<T> getAdjacents(T vertex){
        List<Trajet<T>> trajets = getAdjacentEdges(vertex);
        if (trajets == null) {return null;}
        List<T> adjacents = new ArrayList<>();
        for (Trajet<T> t : trajets) {
            adjacents.add(t.getDestination());
        }
        return adjacents;
    }

    /**
     * This function get a list of the edges adjacents to vertex
     * @param vertex
     * @return a list of the adges of type Trajet
     */
    @Override
    public List<Trajet<T>> getAdjacentEdges(T vertex) {
        return getOutgoingEdges(vertex);
    }

    /**
     * This function get the list of the outgoing edges of the vertex
     * @param vertex
     * @return the list of edges of Trajet
     */
    public List<Trajet<T>> getOutgoingEdges(T vertex) {
        return outgoingEdgesMap.get(vertex);
    }
    
    /**
     * This function get the list of the incoming edges of the vertex
     * @param vertex
     * @return the list of edges of type Trajet T
     */
    public List<Trajet<T>> getIncomingEdges(T vertex) {
        return incomingEdgesMap.get(vertex);
    }

    /**
     * This function return a set of vertex
     * @return a set of vertex
     */
    @Override
    public Set<T> getVertexSet(){
        return outgoingEdgesMap.keySet();
    }

}
