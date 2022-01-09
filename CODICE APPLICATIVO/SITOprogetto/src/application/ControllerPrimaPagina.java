package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.text.Text;


public class ControllerPrimaPagina {

    @FXML
    private Text textSito;

    @FXML
    private Button rinnovaAbbBtn;
    
    @FXML
    private Button creaAbbonamento;

    @FXML
    void openRegistrazione(ActionEvent event) {
    	Stage oldStage = (Stage) textSito.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("abbonamento.fxml"));
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
    void rinnovaAbbonamento(ActionEvent event) {
    	Stage oldStage = (Stage) textSito.getScene().getWindow();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rinnovoAbbonamento.fxml"));
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
