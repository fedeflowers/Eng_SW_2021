package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

public class ControllerFormsStudente {

	private String codiceAbbonamentoStudente = Abbonamento.getUltimoCodiceAbbonamentoInserito(); // me lo salvo appena apro il form studenti cosi se nel frattempo qualcun altro inserisce un abbonamento mentre compilo i form studente ce l'ho salvato.
	
	@FXML
	private TextField email;

	@FXML
	private TextField universit‡;

	@FXML
	private TextField matricola;

	@FXML
	private TextField corso;

	@FXML
	private Button Invia;

	@FXML
	void inviaFormsStudente(ActionEvent event) {
		String emailString = null;
		String universit‡String = null;
		String matricolaString = null;
		String corsoString = null;
		boolean flag = true;

		if (email.getText() != null && !email.getText().trim().equals("")) {
			emailString = email.getText();
			if (!emailString.contains("@")) { // non unico
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Inserire email correttamente");
				alert.showAndWait();
				flag = false;
			}

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Email non settata");
			alert.showAndWait();
		}

		if (universit‡.getText() != null && !universit‡.getText().trim().equals("")) {
			universit‡String = universit‡.getText();

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Universit‡ non settata");
			alert.showAndWait();
		}

		if (matricola.getText() != null && !matricola.getText().trim().equals("")) {
			matricolaString = matricola.getText();

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Matricola non settata");
			alert.showAndWait();
		}

		if (corso.getText() != null && !corso.getText().trim().equals("")) {
			corsoString = corso.getText();

		} else {
			flag = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Corso non settato");
			alert.showAndWait();
		}
		
		if(flag == true) {
			Abbonamento.inviaFormStudente(emailString, universit‡String, matricolaString, corsoString, codiceAbbonamentoStudente);
			Stage oldStage = (Stage) email.getScene().getWindow();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primaPagina.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				oldStage.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Grazie, i dati da lei inseriti verrano controllati per verificare il suo status di studente");
				alert.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}

}