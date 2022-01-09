package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerBici_Rastrelliere {

    @FXML
    private Button backToLogin;

    @FXML
    private Button EliminaBicicletta;

    @FXML
    private Button AddBici;

    @FXML
    private Button addRastrelliera;

    @FXML
    private Button eliminaRastrelliera;

    @FXML
    private Button statistica;
    
    @FXML
    private Button disponibilit‡Bici;

    @FXML
    void disponibilit‡Bici(ActionEvent event) {
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControllaDisponibilita.fxml"));
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
    void AddBici(ActionEvent event) {
    	
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AggiungiBici.fxml"));
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
    void EliminaBicicletta(ActionEvent event) {
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EliminaBici.fxml"));
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
    void addRastrelliera(ActionEvent event) {
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AggiungiRastrelliera.fxml"));
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
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
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

    @FXML
    void eliminaRastrelliera(ActionEvent event) {
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EliminaRastrelliera.fxml"));
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
    void statistica(ActionEvent event) {
    	Stage oldStage = (Stage) statistica.getScene().getWindow();
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatiStatistici.fxml"));
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
