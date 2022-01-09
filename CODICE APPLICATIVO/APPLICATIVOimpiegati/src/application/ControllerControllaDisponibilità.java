package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.stage.*;

public class ControllerControllaDisponibilit� {

    @FXML
    private Button backToLogin;

    @FXML
    private Button ControllaDispBtn;

    @FXML
    private Text textDisponibilit�;

    @FXML
    void backToLogin(ActionEvent event) {
    	Stage oldStage = (Stage) textDisponibilit�.getScene().getWindow();
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
    void controllaDisp(ActionEvent event) {
    	String text ="";
    	ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
    	for(String s : sw.ListaTotemDisponibilit�()) {
    		text+=s + "\n";
    	}
    	textDisponibilit�.setText(text);
    }


}
