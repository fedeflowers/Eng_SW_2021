package application;

import java.sql.Date;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.*;

//controlla il noleggio, dovrò farne altri 2 per le altre due funzionalità
public class ControllerNoleggio {

	@FXML
	private Button Noleggia;

	@FXML
	private Text testoModelli;

	@FXML
	private Button button;

	@FXML
	private Button biciDanneggiataBtn;

	@FXML
	private TextField insModello;

	@FXML
	private Button controllaRiconsegnaBtn;

	@FXML
	private Button btnModello;

	@FXML
	private Text postoBici;

	@FXML
	private Button back;

	@FXML
	private Button backToLogin;

	@FXML
	void back(ActionEvent event) {
		Stage oldStage = (Stage) btnModello.getScene().getWindow();
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

	@FXML
	void backToLogin(ActionEvent event) {
		Stage oldStage = (Stage) Noleggia.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample.fxml"));
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
	void noleggiaModello(ActionEvent event) {
		Date dataOdierna = new Date(System.currentTimeMillis()); // data odierna

		TotemInterface totem = Totem.getInstance();
		if (totem.controlloNoleggio(totem.getIdUtenteLoggato()) == null) { // controlla non abbia altro noleggio in
																			// corso
			// controllo per settare inizio abbonamento occasionale, con il secondo metodo
			// controllo che la carta abbia validità
			if (totem.getValiditàCarta(totem.getIdUtenteLoggato()).after(dataOdierna)) { // significa che la carta è
																							// ancora valida
				totem.setInizioAbbonamento(totem.getIdUtenteLoggato()); // se è già iniziato o è annuale non fa nulla
				int res = totem.getPostoBici(insModello.getText());
				
				if (res >= 0) {
					String codBici = totem.getBiciArray()[res].getCodiceUnivoco();
					totem.setTotemNull(Integer.parseInt(codBici));
					totem.iniziaNoleggio(totem.getIdUtenteLoggato(), codBici);
					totem.sbloccaBici(insModello.getText());
					postoBici.setText("La bici da te noleggiata si trova al posto: " + String.valueOf(res)
							+ " con id Bicicletta: " + codBici);
					ArrayList<Bici> bici = totem.getBici();
					String modelliCompatti = "";
					for (Bici a : bici) {
						if (a != null) {
							modelliCompatti += a.getCodiceUnivoco()+"-";
							modelliCompatti += a.getModello();
							
							if (a.getDanno() == true) {
								modelliCompatti += " (danneggiata)";
							}
						}

						modelliCompatti += "\n";
					}
					testoModelli.setText("modelli bici disponibili: \n" + modelliCompatti);
				} else {
					postoBici.setText("Non sono presenti bici di questo modello");
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Carta scaduta, impossibile noleggiare");
				alert.showAndWait();
			}

		} else {
			postoBici.setText("L'utente ha già noleggiato");
		}

	}

	@FXML
	void show(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		ArrayList<Bici> bici = totem.getBici();
		String modelliCompatti = "";
		for (Bici a : bici) {
			if (a != null) {
				modelliCompatti += a.getCodiceUnivoco()+"-";
				modelliCompatti += a.getModello();
				System.out.println("Bici: "+ a.getCodiceUnivoco()+ " "+ a.getModello()+ " "+a.getDanno());
				
				if (a.getDanno() == true) {
					modelliCompatti += " (danneggiata)";
				}
			}

			modelliCompatti += "\n";
		}
		testoModelli.setText("modelli bici disponibili: \n" + modelliCompatti);
	}

	@FXML
	void modelliBici(ActionEvent event) {

		Stage oldStage = (Stage) Noleggia.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample3.fxml"));
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
	void controllaRiconsegna(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		String idUtente = totem.getIdUtenteLoggato();
		if (totem.controllaRiconsegna(idUtente) != null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Non hai riconsegnato correttamente");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Riconsegna effettuata");
			alert.showAndWait();
		}
	}

	@FXML
	void openBiciDanneggiata(ActionEvent event) {
		Stage oldStage = (Stage) Noleggia.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample5.fxml"));
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
