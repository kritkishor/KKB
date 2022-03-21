package photo.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.Album;
import photo.components.User;

/**
 * This class defines an AlbumSceneController
 * @author mtr103
 */
public class AlbumSceneController {
	
	/** Specify the create button */
	@FXML Button btn_create;
	
	/** Specify the rename button */
	@FXML Button btn_rename;
	
	/** Specify the delete button */
	@FXML Button btn_delete;
	
	/** Specify the open button */
	@FXML Button btn_open;
	
	/** Specify the logoff button */
	@FXML Button btn_logoff;
	
	/** Specify the list of albums for the listview */
	@FXML ListView<Album> listv_albums;
	
	/** Specify the previous scene */
	private Scene previousScene;
	
	/** Specify the current user */
	private User currentUser;
	
	/**
	 * Handle for the Create action event
	 * @param event ActionEvent
	 */
	public void handleCreateButtonAction(ActionEvent event) {
		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Create Album");
		textInputDialog.setContentText("Album Name:");
		textInputDialog.initStyle(StageStyle.UTILITY);
		textInputDialog.setHeaderText(null);
		textInputDialog.setGraphic(null);
		
		Optional<String> result = textInputDialog.showAndWait();
		if (result.isPresent()) {
			String userInput = textInputDialog.getResult();
			Album album = new Album(userInput.trim(), currentUser);
			if (!currentUser.getAlbums().contains(album)) {
				currentUser.getAlbums().add(album);
				updateAlbumList();
			} else {
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Error");
				errAlert.setHeaderText(null);
				errAlert.setContentText("Album with the same name exists.");
				errAlert.show();
			}
		}
	}
	
	/**
	 * Handle for the Rename action event
	 * @param event ActionEvent
	 */
	public void handleRenameButtonAction(ActionEvent event) {
		if (!listv_albums.getSelectionModel().isEmpty()) {
			TextInputDialog textInputDialog = new TextInputDialog();
			textInputDialog.setTitle("Rename Album");
			textInputDialog.setContentText("Album Name:");
			textInputDialog.initStyle(StageStyle.UTILITY);
			textInputDialog.setHeaderText(null);
			textInputDialog.setGraphic(null);
			
			Optional<String> result = textInputDialog.showAndWait();
			if (result.isPresent()) {
				String newAlbumName = textInputDialog.getResult();

				Album newAlbum = new Album(newAlbumName, currentUser);
				if (!currentUser.getAlbums().contains(newAlbum)) {
					String selectedAlbumToRename = listv_albums.getSelectionModel().getSelectedItem().getName();

					for (int i = 0; i < currentUser.getAlbums().size(); i++) {
						Album a = currentUser.getAlbums().get(i);
						
						if (selectedAlbumToRename.toLowerCase().equals(a.getName().toLowerCase())) {
							a.setName(newAlbumName);
							break;
						}
					}
					updateAlbumList();
				} else {
					Alert errAlert = new Alert(AlertType.ERROR);
					errAlert.setTitle("Error");
					errAlert.setHeaderText(null);
					errAlert.setContentText("Album with the same name exists.");
					errAlert.show();
				}
			}
		}
	}
	
	/**
	 * Handle for the Delete action event
	 * @param event ActionEvent
	 */
	public void handleDeleteButtonAction(ActionEvent event) {
		Album selectedAlbum = listv_albums.getSelectionModel().getSelectedItem();
				
		if (selectedAlbum != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setHeaderText("Delete album?");
			alert.setGraphic(null);
			alert.setHeaderText("Delete Album: " + selectedAlbum.getName());	
			
			Optional<ButtonType> response = alert.showAndWait();
			
			if (response.get() == ButtonType.OK) {
				currentUser.getAlbums().remove(selectedAlbum);
				updateAlbumList();
			}
		}
	}
	
	/**
	 * Handle for the Open action event
	 * @param event ActionEvent
	 */
	public void handleOpenButtonAction(ActionEvent event) {
		if (listv_albums.getSelectionModel().getSelectedItem() != null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/photo/view/ui_photoGallery.fxml"));
			Parent root = null;
			
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (root != null) {
				Scene scene = new Scene(root);
		
				PhotoGallerySceneController photoGallerySceneController = loader.getController();			
				photoGallerySceneController.setPreviousScene(btn_create.getScene());
				scene.setUserData(photoGallerySceneController);
				
				// Needs error checking
				Album album = listv_albums.getSelectionModel().getSelectedItem();
				photoGallerySceneController.setCurrentAlbum(album);
				
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	/**
	 * Update album list for the list view
	 */
	public void updateAlbumList() {
		ObservableList<Album> observableList = FXCollections.observableArrayList(currentUser.getAlbums());
		listv_albums.setItems(observableList);
	}
	
	/**
	 * Handle for the Log off action event
	 * @param event ActionEvent
	 */
	public void handleLogOffButtonAction(ActionEvent event) {
		updateAlbumList();
		
		if (previousScene != null) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(previousScene);
			stage.show();
		}
	}
	
	/**
	 * Set the current selected user
	 * @param user User
	 */
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	/**
	 * Set the list of albums
	 * @param albums List of albums
	 */
	public void setAlbumsList(ArrayList<Album> albums) {
		this.listv_albums.getItems().addAll(albums);
		listv_albums.getSelectionModel().selectFirst();
	}
	
	/**
	 * Set the previous scene
	 * @param scene Scene element
	 */
	public void setPreviousScene(Scene scene) {
		if (scene != null) {
			previousScene = scene;
		}
	}
}
