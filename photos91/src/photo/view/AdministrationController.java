package photo.view;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.User;

/**
 * This class defines an Administration Controller
 * @author mtr103
 * @author kkb62
 */
public class AdministrationController {

	/** Specify the add button */
	@FXML Button btn_add;
	
	/** Specify the remove button */
	@FXML Button btn_remove;
	
	/** Specify the exit button */
	@FXML Button btn_exit;
	
	/** Specify the list of users listview */
	@FXML ListView<User> listV_users;

	/** Specify the previous scene which launched */
	private Scene previousScene;	
	
	/**
	 * Set the previous scene
	 * @param scene Scene element
	 */
	public void setPreviousScene(Scene scene) {
		if (scene != null) {
			previousScene = scene;
		}
	}
	
	/**
	 * Link the list of users in a choice box to the listview
	 * @param choiceB_users ChoiceBox of users
	 */
	public void load(ChoiceBox<User> choiceB_users)
	{
		// Note: This links login's Observable Array to the listView observable array reference
		listV_users.setItems(choiceB_users.getItems());
	}
	
	/**
	 * Save information of a user to local storage
	 * @param user User to be stored
	 */
	public void saveUser(User user) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream("data/" + user.getName() + ".ser");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(user);
		objectOutputStream.close();
		fileOutputStream.close();
	}

	/**
	 * Handle for the Add action event
	 * @param event ActionEvent
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void handleAddButtonAction(ActionEvent event) {
		String name = "";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add a User");
		dialog.setHeaderText(null);
		dialog.setContentText("Username");
		Optional<String> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			name = dialog.getEditor().getText();
		}
		
		if(listV_users.getItems().contains(name)) {
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("Error");
			errAlert.setHeaderText(null);
			errAlert.setContentText("Username already Exists!");
			errAlert.show();
		} else {
			if(name.trim().compareTo("") != 0) {
				User newUser = new User(name, 0);
				listV_users.getItems().add(newUser);
				try {
					saveUser(newUser);
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * Handle for the Remove action event
	 * @param event ActionEvent
	 */
	public void handleRemoveButtonAction(ActionEvent event) {
		int selectedIndex = listV_users.getSelectionModel().getSelectedIndex();
		User selectedUser = listV_users.getItems().get(selectedIndex);
		
		if (selectedUser.getName().equals("Admin") || selectedUser.getName().equals("Stock")) {
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("Error");
			errAlert.setHeaderText(null);
			errAlert.setContentText("Cannot Delete " + selectedUser.getName() + " user!");
			errAlert.show();
		} else {
			if (selectedIndex >= 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle("Delete");
				alert.setHeaderText("Delete user?");
				alert.setGraphic(null);
				
				Optional<ButtonType> response = alert.showAndWait();

				if (response.get() == ButtonType.OK) {
					// Remove the saved data from file
					File file = new File("data/" + selectedUser.getName() + ".ser");
					file.delete();
					
					// Remove the user from the list as well
					listV_users.getItems().remove(selectedIndex);
					listV_users.getSelectionModel().select(selectedIndex);
				}
			}
		}
	}
	
	/**
	 * Handle for the Log Off action event
	 * @param event ActionEvent
	 */
	public void handleLogOffButtonAction(ActionEvent event) {
		if (previousScene != null) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(previousScene);
			stage.show();
		}
	}
}
