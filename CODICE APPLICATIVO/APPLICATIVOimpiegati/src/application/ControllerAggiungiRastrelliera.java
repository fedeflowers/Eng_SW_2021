package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.control.ChoiceBox;

public class ControllerAggiungiRastrelliera {

	@FXML
	private Button backToLogin;

	@FXML
	private TextField InsBiciTxt;

	@FXML
	private Button invia;

	  @FXML
	    void invia(ActionEvent event) {
		  ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
		  if (InsBiciTxt.getText() != null ) {
			  if(sw.trovaVieNonUnivocheTotem(InsBiciTxt.getText())==false) { //se è fals enon è presente nel db
				  sw.insertTotem(InsBiciTxt.getText());
					System.out.println("Totem aggiunto");
			  }else {
				  Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Totem già presente a tale via");
					alert.showAndWait();
			  }
				
		  }
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

}
