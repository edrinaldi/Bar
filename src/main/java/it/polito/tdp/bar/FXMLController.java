package it.polito.tdp.bar;

import java.net.URL;
import java.util.ResourceBundle;
import it.polito.tdp.bar.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class FXMLController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

	@FXML
	void handleSimula(ActionEvent event) {

		this.txtResult.clear();
		this.model.simula();
		this.txtResult.appendText("Clienti totali: " + this.model.getClienti() + "\n");
		this.txtResult.appendText("Clienti soddisfatti: " + this.model.getSoddisfatti() + "\n");
		this.txtResult.appendText("Clienti insoddisfatti: " + this.model.getInsoddisfatti() + "\n");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;

	}
}