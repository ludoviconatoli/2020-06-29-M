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
	
	public double getPesoTotale() {
		return this.pesoMigliore;
	}
	
	public List<Adiacenza> getRegistiAdiacenti(Director d){
		
		List<Adiacenza> parziale = new ArrayList<>();
		
		for(DefaultWeightedEdge de: this.grafo.edgesOf(d)) {
			parziale.add(new Adiacenza(d, Graphs.getOppositeVertex(grafo, de, d), grafo.getEdgeWeight(de)));
		}
		
		Collections.sort(parziale);
		
		return parziale;
	}
	
	private List<Director> camminoMinimo;
	private int nRegisti = 0;
	private double pesoTotale = 0.0;
	private double pesoMigliore = 0.0;
	
	public List<Director> cercaCamminoMinimo(Director partenza, int nAttoriMax) {
		
		List<Director> parziale = new ArrayList<>();
		
		parziale.add(partenza);
		
		camminoMinimo = new ArrayList<>();
		
		cerca(partenza, parziale, nAttoriMax);
		
		return camminoMinimo;
	}

	private void cerca(Director partenza, List<Director> parziale, int nAttoriMax) {
		if(pesoTotale <= nAttoriMax) {
			if(parziale.size() > nRegisti && pesoTotale >= pesoMigliore) {
				camminoMinimo = new ArrayList<>(parziale);
				this.nRegisti = parziale.size();
				this.pesoMigliore = pesoTotale;
			}
			
			return;
		}
		
		for(Director vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			int peso = (int)grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), vicino));
			
			if(!parziale.contains(vicino)) {
				pesoTotale += peso;
				parziale.add(vicino);
				
				cerca(partenza, parziale, nAttoriMax);
				
				pesoTotale -= peso;
				parziale.remove(parziale.size()-1);
			}
		}
	}
}
