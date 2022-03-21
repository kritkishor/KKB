package photo.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.Album;
import photo.components.Photo;

/**
 * This class defines a PhotoGallerySceneController
 * @author mtr103
 */
public class PhotoGallerySceneController {

	/** Specify the add button */
	@FXML Button btn_add;
	
	/** Specify the delete button */
	@FXML Button btn_delete;
	
	/** Specify the edit button */
	@FXML Button btn_edit;
	
	/** Specify the copy button */
	@FXML Button btn_copy;
	
	/** Specify the move button */
	@FXML Button btn_move;
	
	/** Specify the search button */
	@FXML Button btn_search;
	
	/** Specify the view button */
	@FXML Button btn_view;
	
	/** Specify the exit button */
	@FXML Button btn_exit;
	
	/** Specify the tile pane for photos */
	@FXML TilePane tileP_photos;
	
	/** Specify the scroll pane for the tile pane */
	@FXML ScrollPane scrollP_photos;
	
	/** Specify the previous scene */
	private Scene previousScene;
	
	/** Specify the current album */
	private Album currentAlbum;
	
	/** Specify the current photo selected */
	private ImageView currentPhotoSelection;
	
	/**
	 * Handle for the Add action event
	 * @param event ActionEvent
	 */
	public void handleAddButtonAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Picture Files", "*.bmp", "*.png", "*.gif", "*.jpg", "*.jpeg"),
				new FileChooser.ExtensionFilter("Bitmap Files", "*.bmp"),
				new FileChooser.ExtensionFilter("PNG", "*.png"),
				new FileChooser.ExtensionFilter("GIF", "*.gif"),
				new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
				new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog(btn_add.getScene().getWindow());

		if (selectedFile != null) {
			String name = selectedFile.getName();
			String path = selectedFile.getParent().replace('\\', '/') + "/";

			Photo photo = new Photo(name, path, "", currentAlbum);
			currentAlbum.addPhoto(photo);
			ImageView imageView = photo.getThumbnail();
			imageView.setId(name);
			addListenersToThumbnail(imageView);
			tileP_photos.getChildren().add(imageView);
		}
	}
	
	/**
	 * Handle for the delete action event
	 * @param event ActionEvent
	 */
	public void handleDeleteButtonAction(ActionEvent event) {
		if (currentPhotoSelection != null) {
			Photo photo = null;
			for (int i = 0; i < currentAlbum.getPhotos().size(); i++) {
				if (currentAlbum.getPhotos().get(i).getName().equals((currentPhotoSelection.getId()))) {
					//currentAlbum.getPhotos().remove(i);
					photo = currentAlbum.getPhotos().get(i);
					break;
				}
			}
			
			ButtonType btn_yes = new ButtonType("Yes", ButtonData.OK_DONE);
			ButtonType btn_no = new ButtonType("No", ButtonData.CANCEL_CLOSE);
			
			Alert alert = new Alert(AlertType.CONFIRMATION, "", btn_yes, btn_no);
			alert.initStyle(StageStyle.UTILITY);
			alert.setHeaderText("Delete?\n\n");

			Optional<ButtonType> response = alert.showAndWait();

			if (response.get() == btn_yes) {
				currentAlbum.getPhotos().remove(photo);
				tileP_photos.getChildren().remove(currentPhotoSelection);
				currentPhotoSelection = null;	
			}
		}
		tileP_photos.getChildren().forEach(node -> { ( (ImageView) node).setOpacity(1.0); });
	}
	
	/**
	 * Handle for the edit action event
	 * @param event ActionEvent
	 */
	public void handleEditButtonAction(ActionEvent event) {
		if (currentPhotoSelection != null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/photo/view/ui_photoEdit.fxml"));
			Parent root = null;
			
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (root != null) {
				Scene scene = new Scene(root);
		
				PhotoEditSceneController photoEditSceneController = loader.getController();			
				photoEditSceneController.setPreviousScene(btn_add.getScene());
				photoEditSceneController.setCurrent(currentAlbum,currentPhotoSelection);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	/**
	 * Handle for the copy action event
	 * @param event ActionEvent
	 */
	public void handleCopyButtonAction(ActionEvent event) {
		if (currentPhotoSelection != null) {
			if (currentAlbum.getOwner().getAlbums().size() > 1) {
				ChoiceDialog<Album> choiceDialog = new ChoiceDialog<Album>();
				choiceDialog.getItems().addAll(currentAlbum.getOwner().getAlbums());
				choiceDialog.getItems().remove(currentAlbum);
				choiceDialog.setContentText("Copy to album");
				choiceDialog.setTitle("Copy");
				choiceDialog.setHeaderText(null);
				choiceDialog.setGraphic(null);
				
				Optional<Album> selectedAlbum = choiceDialog.showAndWait();
				if (selectedAlbum.isPresent()) {
					Photo photo = null;
					for (int i = 0; i < currentAlbum.getPhotos().size(); i++) {
						if (currentAlbum.getPhotos().get(i).getName().equals((currentPhotoSelection.getId()))) {
							//currentAlbum.getPhotos().remove(i);
							photo = currentAlbum.getPhotos().get(i);
							break;
						}
					}
					selectedAlbum.get().addPhoto(photo);
				}
			} else {
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Notice");
				errAlert.setHeaderText(null);
				errAlert.setContentText("You only have one album. Please create another ");
				errAlert.show();
			}
		}
	}
	
	/**
	 * Handle for the move action event
	 * @param event ActionEvent
	 */
	public void handleMoveButtonAction(ActionEvent event) {
		if (currentPhotoSelection != null) {
			String name = currentPhotoSelection.getId();
			ArrayList<Album> albums = currentAlbum.getOwner().getAlbums();
			
			if (albums.size() > 1) {
				ChoiceDialog<Album> choiceDialog = new ChoiceDialog<Album>();
				choiceDialog.getItems().addAll(currentAlbum.getOwner().getAlbums());
				choiceDialog.getItems().remove(currentAlbum);
				choiceDialog.setContentText("Move to Album");
				choiceDialog.setTitle("Move");
				choiceDialog.setHeaderText(null);
				choiceDialog.setGraphic(null);
				
				Optional<Album> selectedAlbum = choiceDialog.showAndWait();
				Photo photo = null;
				
				if (selectedAlbum.isPresent()) {
					for (Photo p : currentAlbum.getPhotos()) {
						if (p.getName().toLowerCase().equals(name.toLowerCase())) {
							photo = p;
							break;
						}
					}
					
					choiceDialog.getSelectedItem().addPhoto(photo);
					currentAlbum.removePhoto(photo);
					tileP_photos.getChildren().remove(currentPhotoSelection);
					currentPhotoSelection = null;
				}
			} else {
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Notice");
				errAlert.setHeaderText(null);
				errAlert.setContentText("You only have one album. Please create another ");
				errAlert.show();
			}
		}
	}
	
	/**
	 * Handle for the search action event
	 * @param event ActionEvent
	 */
	public void handleSearchButtonAction(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photo/view/ui_photoSearch.fxml"));
		Parent root = null;
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (root != null) {
			Scene scene = new Scene(root);
	
			PhotoSearchSceneController photoSearchSceneController = loader.getController();			
			photoSearchSceneController.setPreviousScene(btn_add.getScene());
			photoSearchSceneController.load(currentAlbum);
		
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
	}
	
	/**
	 * Handle for the view action event
	 * @param event ActionEvent
	 */
	public void handleViewButtonAction(ActionEvent event) {
		if (currentPhotoSelection != null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/photo/view/ui_photoView.fxml"));
			Parent root = null;
			
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (root != null) {
				Scene scene = new Scene(root);
		
				PhotoViewSceneController photoViewSceneController = loader.getController();			
				photoViewSceneController.setPreviousScene(btn_add.getScene());
				photoViewSceneController.setCurrentPhotoSelection(currentPhotoSelection);
				photoViewSceneController.setCurrentAlbum(currentAlbum);
	
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	
	/**
	 * Handle for the exit action event
	 * @param event ActionEvent
	 */
	public void handleExitButtonAction(ActionEvent event) {
		if (previousScene != null) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(previousScene);

			// Call the albumSceneController to update the Album list before loading the album scene
			AlbumSceneController albumSceneController = (AlbumSceneController) previousScene.getUserData();
			albumSceneController.updateAlbumList();
			
			stage.show();
		}
	}
	
	/**
	 * Set the current album
	 * @param album An album
	 */
	public void setCurrentAlbum(Album album) {
		this.currentAlbum = album;
		
		tileP_photos.setAlignment(Pos.CENTER);
		tileP_photos.setHgap(3);
		tileP_photos.setVgap(3);
		
		for (Photo photo : currentAlbum.getPhotos()) {
			ImageView imageView = photo.getThumbnail();
			addListenersToThumbnail(imageView);
			tileP_photos.getChildren().add(imageView);	
		}
		
	}
	
	/**
	 * Set the the listener for images
	 * @param imageView An ImageView
	 */
	private void addListenersToThumbnail(ImageView imageView) {
		imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (currentPhotoSelection == null) {
					((ImageView) event.getTarget()).setOpacity(0.65);
				}
			}});
		
		imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (currentPhotoSelection == null) {
					tileP_photos.getChildren().forEach(node -> { ( (ImageView) node).setOpacity(1.0); });
				}
			}});
		
		imageView.setOnMouseClicked(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton().equals(MouseButton.PRIMARY)) {
							 if (currentPhotoSelection == null && event.getClickCount() == 1) {
								tileP_photos.getChildren().forEach(node -> { ( (ImageView) node).setOpacity(.2); });
								currentPhotoSelection = (ImageView) event.getTarget();
								
								currentPhotoSelection.setOpacity(1.0);
							} else if (currentPhotoSelection != null && event.getClickCount() == 1) {
								tileP_photos.getChildren().forEach(node -> { ( (ImageView) node).setOpacity(1.0); });
								currentPhotoSelection = null;
							}
							
						}
					}}
				);
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
