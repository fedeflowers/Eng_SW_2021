package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerEliminaBici {

    @FXML
    private Button backToLogin;

    @FXML
    private ChoiceBox<Integer> biciChoice;

    @FXML
    private Button invia;

    @FXML
	private void initialize() {
    	ArrayList<Integer> idBici = ImpiegatoSW.getInstance().getIdBicidaEliminare();
    	ObservableList<Integer> bici = FXCollections.observableArrayList(idBici);
    	biciChoice.setItems(bici);
    	
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
    void invia(ActionEvent event) {
    	ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
    	if(biciChoice.getValue()!= null) {
    		sw.eliminaBici(biciChoice.getValue());
    	}
    	
    	ArrayList<Integer> idBici = sw.getIdBicidaEliminare();
    	ObservableList<Integer> bici = FXCollections.observableArrayList(idBici);
    	biciChoice.setItems(bici);
    }

}
