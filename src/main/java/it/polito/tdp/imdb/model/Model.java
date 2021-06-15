package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	
	public Model() {
		this.dao = new ImdbDAO();
		
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertici(idMap, anno);
		
		for(Director d: idMap.values()) {
			this.grafo.addVertex(d);
		}
		
		List<Adiacenza> archi = new ArrayList<>(dao.getArchi(anno, idMap));
		for(Adiacenza a: archi) {
			Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Set<Director> getVertici(){
		return grafo.vertexSet();
	}
	
	public List<Adiacenza> getRegistiAdiacenti(Director d){
		
		List<Adiacenza> parziale = new ArrayList<>();
		
		for(DefaultWeightedEdge de: this.grafo.edgesOf(d)) {
			parziale.add(new Adiacenza(d, Graphs.getOppositeVertex(grafo, de, d), grafo.getEdgeWeight(de)));
		}
		
		Collections.sort(parziale);
		
		return parziale;
	}
	
	private List<Adiacenza> camminoMinimo;
	private int contAttori = 0;
	public List<Adiacenza> cercaCamminoMinimo(Director partenza, int nAttoriMax) {
		
		List<Adiacenza> parziale = new ArrayList<>();
		
		parziale.add(new Adiacenza(partenza, null,0));
		
		camminoMinimo = new ArrayList<>();
		
		cerca(partenza, parziale, nAttoriMax);
		
		return camminoMinimo;
	}

	private void cerca(Director partenza, List<Adiacenza> parziale, int nAttoriMax) {
		if(contAttori > nAttoriMax) {
			if(camminoMinimo.size() < parziale.size()) {
				camminoMinimo = new ArrayList<>(parziale);
			}
			
			return;
		}
		
		for(DefaultWeightedEdge de: grafo.edgesOf(parziale.get(parziale.size()-1).getD1())) {
			
			Adiacenza a = new Adiacenza(grafo.getEdgeSource(de), grafo.getEdgeTarget(de), grafo.getEdgeWeight(de));
			if(!parziale.contains(a)) {
				parziale.add(a);
				
				cerca(partenza, parziale, nAttoriMax);
				
				parziale.remove(parziale.get(parziale.size()-1));
			}
		}
	}
}
