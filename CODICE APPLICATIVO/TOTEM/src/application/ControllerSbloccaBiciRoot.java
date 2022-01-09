package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;

import javafx.event.ActionEvent;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerSbloccaBiciRoot {

	@FXML
	private Text testoModelli;

	@FXML
	private Button button;

	@FXML
	private TextField insModello;

	@FXML
	private Button btnSblocca;

	@FXML
	private Text postoBici;

	@FXML
	private Button back;

	@FXML
	void back(ActionEvent event) {
		Stage oldStage = (Stage) postoBici.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginAlternativo.fxml"));
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
	void Sblocca(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		int res = totem.getPostoBici(insModello.getText());
		
		if (res >= 0) {
			String codBici = totem.getBiciArray()[res].getCodiceUnivoco();
			totem.setTotemNull(Integer.parseInt(codBici));
			postoBici.setText("La bici sbloccata si trova al posto: " + String.valueOf(res));
			//aggiungo nel db dello sbloccaggio della bici da aprte del root per averne traccia per sicurezza.
			totem.rootRitiroBici(totem.getIdUtenteLoggato(), Integer.valueOf(codBici));
			totem.sbloccaBici(insModello.getText());
			
			ArrayList<Bici> bici = totem.getBici();
			String modelliCompatti = "";
			for (Bici a : bici) {
				if (a != null) {
					modelliCompatti += a.getModello();
					System.out.println("Bici: "+ a.getCodiceUnivoco()+ " "+ a.getModello()+ " "+a.getDanno());
					
					if (a.getDanno() == true) {
						modelliCompatti += " (danneggiata)";
					}
				}

				modelliCompatti += "\n";
			}
			testoModelli.setText("modelli bici presenti: \n" + modelliCompatti);
		}
	}

	@FXML
	void show(ActionEvent event) {
		TotemInterface totem = Totem.getInstance();
		ArrayList<Bici> bici = totem.getBici();
		String modelliCompatti = "";
		for (Bici a : bici) {
			if (a != null) {
				modelliCompatti += a.getModello();
				System.out.println("Bici: "+ a.getCodiceUnivoco()+ " "+ a.getModello()+ " "+a.getDanno());
				
				if (a.getDanno() == true) {
					modelliCompatti += " (danneggiata)";
				}
			}

			modelliCompatti += "\n";
		}
		testoModelli.setText("modelli bici presenti: \n" + modelliCompatti);
	}

}
