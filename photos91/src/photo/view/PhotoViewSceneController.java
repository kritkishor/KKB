package photo.view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import photo.components.Album;
import photo.components.Photo;
import photo.components.Tag;

/**
 * This class defines a PhotoViewSceneController
 * @author mtr103
 * @author kkb62
 */
public class PhotoViewSceneController {

	/** Specify the image view of a photo */
	@FXML ImageView imgV_photo;
	
	/** Specify the label of the date */
	@FXML Label label_date;
	
	/** Specify the label of the caption */
	@FXML Label label_caption;
	
	/** Specify the list view tags */
	@FXML ListView<Tag> listV_tags;
	
	/** Specify the exit button */
	@FXML Button btn_Exit;
	
	/** Specify the left navigation button */
	@FXML Button btn_navLeft;
	
	/** Specify the right navigation button */
	@FXML Button btn_navRight;
	
	/** Specify the album */
	private Album album;
	
	/** Specify the previous scene */
	private Scene previousScene;
	
	/** Specify the current photo */
	private Photo currentPhoto;
	
	/** Specify the current photo selection */
	private int curr;
	
	/** Specify the list of photos */
	private ArrayList<Photo> photos;

	/**
	 * Display a photo
	 * @param photo A photo
	 */
	public void display(Photo photo)
	{
		imgV_photo.setImage(photo.getImage());
		label_date.setText(photo.getDate().toString());
		label_caption.setText(photo.getCaption());
		listV_tags.setItems(FXCollections.observableArrayList(photo.getTags()));
		
	}
	
	/**
	 * Handle for the left navigation action event
	 * @param event ActionEvent
	 */
	public void handleNavLeftButtonAction(ActionEvent event) {
		if((curr-1) >= 0)
		{
			curr = curr-1;
			display(photos.get(curr));
		}
		else
		{
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("Error");
			errAlert.setHeaderText(null);
			errAlert.setContentText("No Photos to the left!");
			errAlert.show();	
		}
	}
	
	/**
	 * Handle for the right navigation action event
	 * @param event ActionEvent
	 */
	public void handleNavRightButtonAction(ActionEvent event) {
		if((curr+1) < photos.size())
		{
			curr = curr+1;
			display(photos.get(curr));
		}
		else
		{
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("Error");
			errAlert.setHeaderText(null);
			errAlert.setContentText("No Photos to the right!");
			errAlert.show();	
		}
	}
	
	/**
	 * Handle for the Exit action event
	 * @param event ActionEvent
	 */
	public void handleExitButtonAction(ActionEvent event) {
		if (previousScene != null) {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(previousScene);
			stage.show();
		}
	}
	
	/**
	 * Set the current album to view
	 * @param album The album
	 */
	public void setCurrentAlbum(Album album)
	{
		this.album = album;
		photos = album.getPhotos();
		for(int i = 0; i<photos.size(); i++)
		{		
			ImageView img = new ImageView(photos.get(i).getImage());
			if(img.getImage().getUrl().equals(imgV_photo.getImage().getUrl()))
			{
				currentPhoto = photos.get(i);
				display(currentPhoto);
				curr = i;
				return;
			}
		}
	
	}

	/**
	 * Set the current photo selection of a photo
	 * @param imageView The ImageView of the photo
	 */
	public void setCurrentPhotoSelection(ImageView imageView) {
		imgV_photo.setImage(imageView.getImage());
		
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
