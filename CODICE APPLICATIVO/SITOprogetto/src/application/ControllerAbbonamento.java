package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;

import javafx.scene.*;

import javafx.stage.*;


import java.math.BigInteger;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class ControllerAbbonamento {

	private AbbonamentoAnnuale AA = new AbbonamentoAnnuale();
	private AbbonamentoGiornaliero AG = new AbbonamentoGiornaliero();
	private AbbonamentoSettimanale AS = new AbbonamentoSettimanale();

	@FXML
	private TextField passwordInput;

	@FXML
	private TextField numeroCartaInput;

	@FXML
	private DatePicker dataScadenzaInput;

	@FXML
	private Button inviaForms;

	@FXML
	private TextField nomeUtenteInput;

	@FXML
	private ChoiceBox<String> tipoAbbChoice;

	@FXML
	private CheckBox studente;

	@FXML
	private void initialize() {
		ArrayList<String> tipi = new ArrayList<>();
		tipi.add("Giornaliero");
		tipi.add("Settimanale");
		tipi.add("Annuale");
		ObservableList<String> tipoAbb = FXCollections.observableArrayList(tipi);
		tipoAbbChoice.setItems(tipoAbb);
	}

	//FARE VERIFICA DI VALIDITA' DELLA CARTA
	@FXML
	void inviaForms(ActionEvent event) {
		String nome = null;
		String password = null;
		String numeroCarta = null;
		Date dataValidit‡ = null;
		boolean studenteRes = false;
		String tipo = null;

		boolean flag = true; // la flag Ë inutile ho gi‡ il return

		if (nomeUtenteInput.getText() != null && !nomeUtenteInput.getText().trim().equals("")) {
			nome = nomeUtenteInput.getText();
			if (!Abbonamento.unicit‡NomeUtente(nome)) { // non unico
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Nome utente non univoco, cambiarlo");
				alert.showAndWait();
				flag = false;
				return;
			}

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Nome non settato");
			alert.showAndWait();
			return;
		}
		if (passwordInput.getText() != null && !passwordInput.getText().trim().equals("")) {
			password = passwordInput.getText();
		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Password non settata");
			alert.showAndWait();
			return;
		}

		if (numeroCartaInput.getText() != null && !numeroCartaInput.getText().trim().equals("")) {
			numeroCarta = numeroCartaInput.getText();
			try {
				new BigInteger(numeroCarta); // controllo che non siano state inserite lettere
			} catch (NumberFormatException e) {
				flag = false;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Inserire numero carta correttamente");
				alert.showAndWait();
				return;
			}

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Numero carta non settato");
			alert.showAndWait();
			return;
		}

		if (dataScadenzaInput.getValue() != null) {
			LocalDate localDate = dataScadenzaInput.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			dataValidit‡ = convertUtilToSql(Date.from(instant));
		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("data non settata"); // manca il controllo se Ë settata male
			alert.showAndWait();
			return;
		}
		studenteRes = studente.isSelected();

		if (tipoAbbChoice.getValue() != null) {
			tipo = tipoAbbChoice.getValue();
		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Tipo abbonamento non settato");
			alert.showAndWait();
			return;
		}

		String r = "";
		//VERIFICARE VALIDIT‡ CARTA DATA SCADENZA
		if (flag == true && tipo.trim().equals("Annuale")) {
			r = AA.creaAbbonamentoFirstTime(nome, password, numeroCarta, dataValidit‡, studenteRes, tipo);
		} else if (flag == true && tipo.trim().equals("Settimanale")) {
			r = AS.creaAbbonamentoFirstTime(nome, password, numeroCarta, dataValidit‡, studenteRes, tipo);
		} else if (flag == true && tipo.trim().equals("Giornaliero")) {
			r = AG.creaAbbonamentoFirstTime(nome, password, numeroCarta, dataValidit‡, studenteRes, tipo);
		}

		if(!r.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("il tuo codice univoco per l'attivazione di noleggi Ë: "+r+"\nSalvalo da qualche parte!");
			alert.showAndWait();
			System.out.println("codice univoco: "+ r);
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("ERRORE, visualizza la console per i dettagli");
			alert.showAndWait();
			studenteRes = false;
		}
		
		
		// se Ë uno studente apro un altro form
		if (studenteRes) {
			//Stage oldStage = (Stage) studente.getScene().getWindow();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("formsStudente.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				//oldStage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}

	}

	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}

}
