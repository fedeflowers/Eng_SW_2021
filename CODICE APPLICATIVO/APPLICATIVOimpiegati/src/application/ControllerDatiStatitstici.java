package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.text.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerDatiStatitstici {

    @FXML
    private Button backToLogin;

    @FXML
    private Button NBMUG;

    @FXML
    private Button TPU;

    @FXML
    private Button FOMU;

    @FXML
    private Text NBMUGtxt;

    @FXML
    private Text TPUtxt;

    @FXML
    private Text FOMUtxt;

    @FXML
    void FOMU(ActionEvent event) {
    	ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
    	FOMUtxt.setText("Ci sono 3 fasce orarie: 1: 00.00 - 8.00 \n2: 8.00 - 16.00 3: 16.00 - 00.00\nRisultato: "+String.valueOf(sw.determinaFasciaOrariaPiùRichiesta()));
    }

    @FXML
    void NBMUG(ActionEvent event) throws ParseException {
    	ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
    	NBMUGtxt.setText(String.valueOf(sw.numeroBiciMedioUtilizzatoAlGiorno()));
    }

    @FXML
    void TPU(ActionEvent event) {
    	ImpiegatoSWInterface sw = ImpiegatoSW.getInstance();
    	TPUtxt.setText("Id Totem: "+String.valueOf(sw.getTotempiùUsato()));
    }

    @FXML
    void backToLogin(ActionEvent event) {
    	Stage oldStage = (Stage) TPU.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PrimaPagina.fxml"));
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
