/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<String> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	int year;
    	try {
    		year = Integer.parseInt(this.boxAnno.getValue());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Selezionare un anno ");
    		return;
    	}catch(NullPointerException n) {
    		this.txtResult.setText("Selezionare un anno ");
    		return;
    	}
    	
    	this.model.creaGrafo(year);
    	this.txtResult.appendText("GRAFO CREATO: \n");
    	this.txtResult.appendText("#Vertici: " + this.model.getNVertici() +"\n");
    	this.txtResult.appendText("#Archi: " + this.model.getNArchi());
    	this.boxRegista.getItems().addAll(model.getVertici());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	int year;
    	try {
    		year = Integer.parseInt(this.boxAnno.getValue());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("CREARE PRIMA IL GRAFO");
    		return;
    	}catch(NullPointerException n) {
    		this.txtResult.setText("CREARE PRIMA IL GRAFO ");
    		return;
    	}
    	
    	Director d = this.boxRegista.getValue();
    	if(d == null) {
    		this.txtResult.setText("CREARE PRIMA IL GRAFO ");
    		return;
    	}
    	
    	List<Adiacenza> result = model.getRegistiAdiacenti(d);
    	this.txtResult.appendText("Hai selezionato il regista: " + d +" e i suoi registi adiacenti sono: \n\n");
    	for(Adiacenza a: result) {
    		this.txtResult.appendText(a.getD2().toString() + "   #attori condivisi: " + a.getPeso() + "\n");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Director partenza = this.boxRegista.getValue();
    	if(partenza == null) {
    		this.txtResult.setText("Selezionare prima un regista");
    		return;
    	}
    	
    	int max;
    	try{
    		max = Integer.parseInt(this.txtAttoriCondivisi.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Inserisci un valore numerico positivo come n. attori condivisi");
    		return;
    	}
    	
    	List<Director> adiacenti = this.model.cercaCamminoMinimo(partenza, max);
    	
    	for(Director d: adiacenti) {
    		this.txtResult.appendText(d + "\n");
    	}
    	
    	this.txtResult.appendText("\nIl peso totale: " + this.model.getPesoTotale());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().addAll("2004", "2005", "2006");
    }
    
}
