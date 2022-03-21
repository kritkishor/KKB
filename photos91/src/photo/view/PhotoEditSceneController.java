package photo.view;

import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.Album;
import photo.components.Photo;
import photo.components.Tag;

/**
 * This class defines a PhotoEditSceneController
 * @author kkb62
 * @author mtr103
 */
public class PhotoEditSceneController {

	/** Store the image view of the photo */
	@FXML ImageView imgV_photo;
	
	/** Store the list of tags for the list view */
	@FXML ListView<Tag> listV_tags;
	
	/** Store the add button */
	@FXML Button btn_add;
	
	/** Store the delete button */
	@FXML Button btn_delete;
	
	/** Store the save button */
	@FXML Button btn_save;
	
	/** Store the exit button */
	@FXML Button btn_exit;
	
	/** Store the textfield caption */
	@FXML TextField textF_caption;
	
	/** Store the album */
	private Album album;
	
	/** Store the list of albums */
	ArrayList<Album> albums;
	
	/** Store the list of photos */
	ArrayList<Photo> photos;
	
	/** Store the current photo */
	private Photo currentPhoto;
	
	/** Store the current selection */
	private int curr;
	
	/** Store the previous scene */
	private Scene previousScene;
	
	/** Store the new caption for the photo */
	private String newCaption;

	/**
	 * Set the current photo
	 * @param album An Album
	 * @param imageView An ImageView
	 */
	public void setCurrent(Album album, ImageView imageView)
	{
		imgV_photo.setImage(imageView.getImage());
		this.album = album;
		photos = album.getPhotos();
		for(int i = 0; i<photos.size(); i++)
		{		
			ImageView img = new ImageView(photos.get(i).getImage());
			if(img.getImage().getUrl().equals(imgV_photo.getImage().getUrl()))
			{
				currentPhoto = photos.get(i);
				curr = i;
				break;
			}
		}
		textF_caption.setText(currentPhoto.getCaption());
		listV_tags.getItems().addAll(currentPhoto.getTags());
	}
	
	/**
	 * Handle for the add action event
	 * @param event ActionEvent
	 */
	public void handleAddButtonAction(ActionEvent event) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		
		ButtonType addButton = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);
		
		TextField tagField = new TextField();
		TextField valueField = new TextField();
		
		GridPane gridPane = new GridPane();
		Label tagLabel = new Label("Tag: ");
		Label valueLabel = new Label("Value: ");
		
		gridPane.add(tagLabel, 0, 0);
		gridPane.add(valueLabel, 0, 1);
		gridPane.add(tagField, 1, 0);
		gridPane.add(valueField, 1, 1);
		
		dialog.getDialogPane().setContent(gridPane);
		
		Optional<ButtonType> result = dialog.showAndWait();

		if (result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE) {
			String tag = tagField.getText();
			String value = valueField.getText();
			Tag newTag = new Tag(tag, value);
			if (!currentPhoto.getTags().contains(newTag)) {
				listV_tags.getItems().add(newTag);
				currentPhoto.addTag(newTag);	
			} else {
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Notice");
				errAlert.setHeaderText(null);
				errAlert.setContentText("Tag already exists");
				errAlert.show();
			}
		}
		
	}
	
	/**
	 * Handle for the delete action event
	 * @param event ActionEvent
	 */
	public void handleDeleteButtonAction(ActionEvent event) {
		Tag selectedTag = listV_tags.getSelectionModel().getSelectedItem();

		ButtonType btn_yes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType btn_no = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "", btn_yes, btn_no);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText("Delete?\n\n" + "Tag: " + selectedTag.getName() + "\nValue: " + selectedTag.getValue());

		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == btn_yes) {
			currentPhoto.removeTag(selectedTag);
			listV_tags.getItems().remove(selectedTag);
		}
		
	}
	
	/**
	 * Handle for the save action event
	 * @param event ActionEvent
	 */
	public void handleSaveButtonAction(ActionEvent event) {
		newCaption = textF_caption.getText();
		photos.get(curr).setCaption(newCaption);	
		Alert successAlert = new Alert(AlertType.CONFIRMATION);
		successAlert.setTitle("Error");
		successAlert.setHeaderText(null);
		successAlert.setContentText("Successfully changed the Caption!");
		successAlert.show();	
		
		albums = album.getOwner().getAlbums();
		for(Album alb : albums)
		{
			if(alb.getName().equals(album.getName()))
			{
				alb.setPhotos(photos);
			}
		}
	}
	
	/**
	 * Handle for the exit action event
	 * @param event ActionEvent
	 */
	public void handleExitButtonAction(ActionEvent event) {
		album.setPhotos(photos);
		if (previousScene != null) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(previousScene);
			stage.show();
		}
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
