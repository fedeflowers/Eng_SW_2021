package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.control.ChoiceBox;

public class ControllerAggiungiBici {

	@FXML
	private Button backToLogin;

	@FXML
	private ChoiceBox<String> modelloChoice;

	@FXML
	private ChoiceBox<Boolean> dannoChoice;

	@FXML
	private ChoiceBox<Boolean> seggiolinoChoice;

	@FXML
	private Button invia;

	@FXML
	private void initialize() {
		ArrayList<String> modelli = new ArrayList<>();
		modelli.add("elettrica");
		modelli.add("normale");
		ArrayList<Boolean> danno = new ArrayList<>();
		danno.add(true);
		danno.add(false);
		ArrayList<Boolean> seggiolino = new ArrayList<>();
		seggiolino.add(true);
		seggiolino.add(false);

		ObservableList<String> modelliC = FXCollections.observableArrayList(modelli);
		modelloChoice.setItems(modelliC);
		ObservableList<Boolean> dannoC = FXCollections.observableArrayList(danno);
		dannoChoice.setItems(dannoC);

		// seggiolino appare solo se il modello è elettrico
		modelloChoice.getSelectionModel().selectedIndexProperty()
				.addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
					if (new_val.intValue() == 0) {// 0 = elettrica
						ObservableList<Boolean> seggiolinoC = FXCollections.observableArrayList(seggiolino);
						seggiolinoChoice.setItems(seggiolinoC);
					} else {
						// se non è elettrica il seggiolino non deve essere sceglibile
						ObservableList<Boolean> seggiolinoC = FXCollections.observableArrayList();
						seggiolinoChoice.setItems(seggiolinoC);
					}
				});
	}

	@FXML
	void backToLogin(ActionEvent event) {
		Stage oldStage = (Stage) invia.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestioneBici_Rastrelliere.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void invia(ActionEvent event) { //vanno aggiunte anche alla tabella degli impiegati così che l'impiegato che aggiunge la bici la può attaccare al totem
		ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
		if (modelloChoice.getValue() != null) {
			if (dannoChoice.getValue() != null) {
				if (modelloChoice.getValue().equals("elettrica")) {
					if (seggiolinoChoice.getValue() != null) {
						sw.aggiungiBici(modelloChoice.getValue(), dannoChoice.getValue(), seggiolinoChoice.getValue());
						sw.insRitiroBici(sw.getIdUtenteLoggato(), sw.getMaxIdBici());
					}	
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setContentText("Campo seggiolino vuoto");
						alert.showAndWait();
					}
				} else {// se non è elettrica non ha seggiolino
					sw.aggiungiBici(modelloChoice.getValue(), dannoChoice.getValue(), false);
					sw.insRitiroBici(sw.getIdUtenteLoggato(), sw.getMaxIdBici());
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Campo danno vuoto");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Selezionare modello");
			alert.showAndWait();
		}
		System.out.println("bici aggiunta");
	}
}
