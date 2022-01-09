package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.*;
import javafx.stage.*;

public class ControllerPrimaPagina {

    @FXML
    private TextField user;

    @FXML
    private PasswordField paswd;

    @FXML
    private Button loginBtn;

    @FXML
    private Text TextError;

    @FXML
    void LogIn(ActionEvent event) {
    	Stage oldStage = (Stage) TextError.getScene().getWindow();
    	ImpiegatoSWInterface swControllo = ImpiegatoSW.getInstance();

		if (swControllo.loginRoot(user.getText(), paswd.getText())) {
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

		} else {
			TextError.setText("Error Try Again");
		}
    }

}
