package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;
import javafx.scene.control.ChoiceBox;

public class ControllerRinnovoAbbonamento {

	private AbbonamentoAnnuale AA = new AbbonamentoAnnuale();
	private AbbonamentoGiornaliero AG = new AbbonamentoGiornaliero();
	private AbbonamentoSettimanale AS = new AbbonamentoSettimanale();
	
    @FXML
    private TextField nomeUtenteInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private ChoiceBox<String> tipoAbbonamento;

    @FXML
    private Button rinnova;
    
    @FXML
    private void initialize() {
		ArrayList<String> tipi = new ArrayList<>();
		tipi.add("Giornaliero");
		tipi.add("Settimanale");
		tipi.add("Annuale");
		ObservableList<String> tipoAbb = FXCollections.observableArrayList(tipi);
		tipoAbbonamento.setItems(tipoAbb);
	}
    
    @FXML
    void rinnova(ActionEvent event) {
    	
    	if(passwordInput.getText()!= null && !passwordInput.getText().trim().equals("")&& nomeUtenteInput.getText()!= null && !nomeUtenteInput.getText().trim().equals("")&& tipoAbbonamento.getValue()!= null) {
    		if(Abbonamento.login(nomeUtenteInput.getText(), passwordInput.getText())) { // loggato correttamente quindi è già presente nel db
    			
    			boolean b = false;
    			String tipo = tipoAbbonamento.getValue().trim();
    			if(tipo.equals("Giornaliero")) {
    				b= AG.rinnovaAbb(nomeUtenteInput.getText());
    			}else if(tipo.equals("Settimanale")) {
    				 b= AS.rinnovaAbb(nomeUtenteInput.getText());
    			}else if(tipo.equals("Annuale")) {
    				b= AA.rinnovaAbb(nomeUtenteInput.getText());
    			}
    			
    			
    			
    			Alert alert = new Alert(AlertType.INFORMATION);
    			if(b) {// se è stato rinnovato corretamente
    				alert.setContentText("Grazie del tuo rinnovo");
        			alert.showAndWait();
        			Stage oldStage = (Stage) rinnova.getScene().getWindow();
        			try {
        				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primaPagina.fxml"));
        				Parent root1 = (Parent) fxmlLoader.load();
        				Stage stage = new Stage();
        				stage.setScene(new Scene(root1));
        				stage.show();
        				oldStage.close();
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
    			}else {
    				alert.setContentText("ERRORE!, visualizza i dettagli nella console");
        			alert.showAndWait();
    			}
    			
    		}else {
    			Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("nome utente o password errati");
				alert.showAndWait();
    		}
    	}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("inserire nome utente e password");
			alert.showAndWait();
		}
    	
    }

}