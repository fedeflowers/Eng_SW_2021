package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerPrimaPagina {


    @FXML
    private Button logNoleggio;

    @FXML
    private Button logRoot;
    
    @FXML
    private Button Riconsegna;

    @FXML
    void Riconsegna(ActionEvent event) {
    	Stage oldStage = (Stage) logNoleggio.getScene().getWindow();

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample4.fxml"));
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
    void logNoleggio(ActionEvent event) {
    	Stage oldStage = (Stage) logNoleggio.getScene().getWindow();
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
    void logRoot(ActionEvent event) {
    	Stage oldStage = (Stage) logNoleggio.getScene().getWindow();
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

}