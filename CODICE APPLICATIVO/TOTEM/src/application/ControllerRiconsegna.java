package application;

import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.text.Text;

public class ControllerRiconsegna {

	@FXML
	private Button riconsegna;

	@FXML
	private ChoiceBox<Integer> postiLiberiChoice;

	@FXML
	private ChoiceBox<String> idUtentiChoice;

	@FXML
	private ChoiceBox<String> biciNoleggiateIdChoice;
	@FXML
	private Text erroreText;

	@FXML
	private void initialize() {
		TotemInterface totem = Totem.getInstance();

		if (totem.postiLiberi().isEmpty()) {
			erroreText.setText("Non ci sono posti liberi");
			return;
		}
		ObservableList<Integer> posti = FXCollections.observableArrayList(totem.postiLiberi());
		postiLiberiChoice.setItems(posti);

		if (totem.idUtentiNoleggio().isEmpty() && totem.IdImpiegatiRiallocazione().isEmpty()) {
			erroreText.setText("riconsegna effettuata\nnessuno ha in noleggio bici al momento!");
			return;
		}
		ObservableList<String> idUtentiNoleggio = FXCollections.observableArrayList(totem.idUtentiNoleggio());
		for (String s : totem.IdImpiegatiRiallocazione()) {
			idUtentiNoleggio.add(s);
		}
		idUtentiChoice.setItems(idUtentiNoleggio);

		// fa in modo che le bici da selezionare corrispondano all'utente selezionato
		idUtentiChoice.getSelectionModel().selectedIndexProperty()
				.addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
					if (new_val.intValue() >= 0) {

						ArrayList<String> idUtenti = totem.idUtentiNoleggio();
						for (String s : totem.IdImpiegatiRiallocazione()) {
							idUtenti.add(s);
						}
						ObservableList<String> modelliBici = FXCollections.observableArrayList();
						for (String id : totem.IdBiciNoleggio(idUtenti.get(new_val.intValue()))) {
							modelliBici.add(id + "-" + totem.getModelloBici(Integer.parseInt(id)));
						}

						biciNoleggiateIdChoice.setItems(modelliBici);
					} else {
						ObservableList<String> vuota = FXCollections.observableArrayList();
						biciNoleggiateIdChoice.setItems(vuota); // svuoto il form
					}
				});
	}

	/*
	 * @FXML void back(ActionEvent event) { Stage oldStage = (Stage)
	 * riconsegna.getScene().getWindow(); try { FXMLLoader fxmlLoader = new
	 * FXMLLoader(getClass().getResource("Sample.fxml")); Parent root1 = (Parent)
	 * fxmlLoader.load(); Stage stage = new Stage(); stage.setScene(new
	 * Scene(root1)); stage.show(); oldStage.close(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	@FXML
	void riconsegnaBici(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		if (idUtentiChoice.getValue() != null && postiLiberiChoice.getValue() != null
				&& biciNoleggiateIdChoice.getValue() != null) {
			if (totem.idUtentiNoleggio().contains(idUtentiChoice.getValue())) {
				if (totem.riconsegna(idUtentiChoice.getValue(), biciNoleggiateIdChoice.getValue().split("-")[1],
						postiLiberiChoice.getValue())) {
					totem.controlloConsegnaTardiva(idUtentiChoice.getValue()); // verifica annullamenti abbonamenti
																				// ritardi..
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("ERRORE, verifica in console l'errore accaduto" );
					alert.showAndWait();
				}

			} else if (totem.IdImpiegatiRiallocazione().contains(idUtentiChoice.getValue())) {
				// riconsegna del root
				boolean res = totem.riconsegnaRoot(idUtentiChoice.getValue(),
						Integer.parseInt(biciNoleggiateIdChoice.getValue().split("-")[0]),
						biciNoleggiateIdChoice.getValue().split("-")[1], postiLiberiChoice.getValue());					
				if(res == false) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("ERRORE, verifica in console l'errore accaduto" );
					alert.showAndWait();
				}
					
			}
			if (totem.idUtentiNoleggio().isEmpty() && totem.IdImpiegatiRiallocazione().isEmpty()) {
				erroreText.setText("riconsegna effettuata\nnessuno ha in noleggio bici al momento!");
			}
			ObservableList<String> idUtentiNoleggio = FXCollections.observableArrayList(totem.idUtentiNoleggio());
			for (String s : totem.IdImpiegatiRiallocazione()) {
				idUtentiNoleggio.add(s);
			}
			idUtentiChoice.setItems(idUtentiNoleggio);
			if (totem.postiLiberi().isEmpty()) {
				erroreText.setText("Non ci sono posti liberi");
				return;
			}
			ObservableList<Integer> posti = FXCollections.observableArrayList(totem.postiLiberi());
			postiLiberiChoice.setItems(posti);

		}

	}

}
