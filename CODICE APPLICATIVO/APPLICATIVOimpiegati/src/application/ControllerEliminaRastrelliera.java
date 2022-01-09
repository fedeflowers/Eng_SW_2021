package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.stage.*;

public class ControllerEliminaRastrelliera {

    @FXML
    private Button backToLogin;

    @FXML
    private ChoiceBox<Integer> TotemChoice;

    @FXML
    private Button invia;

    @FXML
	private void initialize() {
    	ArrayList<Integer> totemVuoti= ImpiegatoSW.getInstance().getListaTotemVuoti();

		ObservableList<Integer> totemVuotiC = FXCollections.observableArrayList(totemVuoti);
		TotemChoice.setItems(totemVuotiC);
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
    	if(TotemChoice.getValue()!=null) {
    		sw.deleteTotem(TotemChoice.getValue());
    		System.out.println("Totem eliminato");
    	}
    	
    	ArrayList<Integer> totemVuoti= ImpiegatoSW.getInstance().getListaTotemVuoti();
		ObservableList<Integer> totemVuotiC = FXCollections.observableArrayList(totemVuoti);
		TotemChoice.setItems(totemVuotiC);
    }

}
