package application;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.text.Text;

public class ControllerDanno {

	@FXML
	private ChoiceBox<Integer> biciNoleggiateIdChoice;

	@FXML
	private Button back;

	@FXML
	private Button segnala;

	@FXML
	private Text grazieText;

	@FXML
	void SegnalaBici(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		if (biciNoleggiateIdChoice.getValue() != null) {
			totem.segnalaDanno(biciNoleggiateIdChoice.getValue());
			grazieText.setText("Grazie della segnalazione!");
		}
			
	}

	@FXML
	private void initialize() {
		TotemInterface totem = Totem.getInstance();

		if (totem.idUtentiNoleggio().isEmpty()) {
			grazieText.setText("Non hai noleggi in corso, non puoi segnalare una bici danneggiata non noleggiata");
			return;
		}
		ObservableList<Integer> idBici = FXCollections
				.observableArrayList(totem.getBiciNoleggiataId(totem.getIdUtenteLoggato()));
		biciNoleggiateIdChoice.setItems(idBici);

	}

	@FXML
	void back(ActionEvent event) {
		Stage oldStage = (Stage) grazieText.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample2.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
