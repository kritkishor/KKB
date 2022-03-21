package photo.app;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.User;
import photo.view.LoginSceneController;


/**
 * This class defines a user
 * @author mtr103
 */
public class Photos extends Application {
	/** Store the list of users */
	@FXML ChoiceBox<User> choiceB_users;
	
	/** Store the stage of the program */
	private Stage window;
	
	/** Store the login controller */
	private LoginSceneController loginSceneController;
	
	/**
	 * Starting method for entry
	 * @param primaryStage Primary stage passed by launch
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photo/view/ui_login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 600, 400);

		loginSceneController = loader.getController();
		loginSceneController.load();

		primaryStage.setOnCloseRequest(event -> {
			event.consume();
			closeProgram();
		});

		primaryStage.setTitle("Photos");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Main method of the Photo program. Kick starts program via launch
	 * @param args Arguments passed to main
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Program closure clean up
	 * Confirms closure and saves objects to file
	 */
	public void closeProgram() {
		ButtonType btn_yes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType btn_no = new ButtonType("No", ButtonData.CANCEL_CLOSE);

		Alert alert = new Alert(AlertType.CONFIRMATION, "", btn_yes, btn_no);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText("Do you wish to exit?");

		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == btn_yes) {
			loginSceneController.saveAll();
			Platform.exit();
		}
	}	
}
