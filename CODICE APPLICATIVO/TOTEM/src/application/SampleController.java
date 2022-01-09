package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.*;

public class SampleController {

	@FXML
	private TextField user;

	@FXML
	private PasswordField paswd;

	@FXML
	private Button loginBtn;

	@FXML
	private Button riconsegna;
	
	@FXML
	private Button back;

	@FXML
	private Text TextError;

	/*@FXML
	void riconsegnaBici(ActionEvent event) {
		Stage oldStage = (Stage) TextError.getScene().getWindow();

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
	}*/
	
	@FXML
	void back(ActionEvent event) {
		Stage oldStage = (Stage) TextError.getScene().getWindow();
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
	}

	@FXML
	public void LogIn(ActionEvent event) {
		Stage oldStage = (Stage) TextError.getScene().getWindow();
		Totem totem = Totem.getInstance();

		if (totem.verificaAbbonamento(user.getText(), paswd.getText())) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample2.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				oldStage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			TextError.setText("Error Try Again");
		}
	}
}
