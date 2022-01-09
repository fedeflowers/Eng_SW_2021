package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.*;

public class ControllerLoginRoot {

	@FXML
	private TextField user;

	@FXML
	private PasswordField paswd;

	@FXML
	private Button loginBtn;

	@FXML
	private Text TextError;

	@FXML
	private Button back;

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
	void LogIn(ActionEvent event) {
		Stage oldStage = (Stage) TextError.getScene().getWindow();
		TotemInterface totem = Totem.getInstance();

		if (totem.controlloRoot(user.getText(), paswd.getText())) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sbloccaBiciRoot.fxml"));
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