package photo.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import photo.components.Photo;
import photo.components.Tag;
import photo.components.Album;

/**
 * This class defines a PhotoSearchSceneController
 * @author kkb62
 */
public class PhotoSearchSceneController {

	/** Specify the date picker start */
	@FXML DatePicker datePicker_start;
	
	/** Specify the date picker end */
	@FXML DatePicker datePicker_end;
	
	/** Specify the list of choice box tags */
	@FXML ChoiceBox<String> choiceB_tagA;
	
	/** Specify the list of choice box tags */
	@FXML ChoiceBox<String> choiceB_tagB;
	
	/** Specify the radio for AND */
	@FXML RadioButton radioB_AND;
	
	/** Specify the radio for OR */
	@FXML RadioButton radioB_OR;
	
	/** Specify the text field for tag A */
	@FXML TextField textB_TagAValue;
	
	/** Specify the text field for tag b */
	@FXML TextField textB_TagBValue;
	
	/** Specify the scroll pane for photos */
	@FXML ScrollPane scrollP_photos;
	
	/** Specify the tile pane for photos */
	@FXML TilePane tileP_photos;

	/** Specify the previous scene */
	private Scene previousScene;
	
	/** Specify the album */
	private Album album;
	
	/** Specify the list of tag names */
	private ArrayList<String> tagNames;
	
	/** Specify the list of photos */
	private ArrayList<Photo> photos;
	
	/** Specify the list of photos to be displayed */
	private ArrayList<Photo> toDisplay;
	
	/** Specify an error flag */
	private boolean error;
	
	/**
	 * Load photos of an album
	 * @param album An album
	 */
	public void load(Album album)
	{
		this.album = album;
		tagNames = new ArrayList<String>();
		photos = album.getPhotos();
	//	albums = album.getOwner().getAlbums();
		for(Photo photo : photos)
		{
				for(Tag tag : photo.getTags())
				{
					if(!tagNames.contains(tag.getName()))
					{
						tagNames.add(tag.getName());
					}
				}
		}
		choiceB_tagA.getItems().addAll(tagNames);
		choiceB_tagB.getItems().addAll(tagNames);
	}

	/**
	 * Get a list of photos by tags
	 * @return A list of photos
	 */
	public ArrayList<Photo> searchByTags()
	{
		String tagA_name,tagB_name;
		Tag tagA,tagB;
		toDisplay = new ArrayList<Photo>();
		tagA_name = choiceB_tagA.getSelectionModel().getSelectedItem();
		tagB_name = choiceB_tagB.getSelectionModel().getSelectedItem();
		if(tagA_name != null && tagB_name != null && textB_TagAValue.getText() != null && textB_TagBValue.getText() != null)
		{
			tagA = new Tag(tagA_name,textB_TagAValue.getText());
			tagB = new Tag(tagB_name,textB_TagBValue.getText());
			if(radioB_AND.isSelected())
			{
				for(Photo photo : photos)
				{
					if(photo.getTags().contains(tagA) && photo.getTags().contains(tagB))
					{
						toDisplay.add(photo);
					}
				}
			}
			else if(radioB_OR.isSelected())
			{
				for(Photo photo : photos)
				{
					if(photo.getTags().contains(tagA) || photo.getTags().contains(tagB))
					{
						toDisplay.add(photo);
					}
				}
			}
			else
			{
				error = true;
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Error");
				errAlert.setHeaderText(null);
				errAlert.setContentText("Please Select AND or OR!");
				errAlert.show();
				
			}
		}
		else if(tagA_name == null && tagB_name != null && textB_TagBValue.getText() != null)
		{
			tagB = new Tag(tagB_name,textB_TagBValue.getText());
			for(Photo photo : photos)
			{
				if(photo.getTags().contains(tagB))
				{
					toDisplay.add(photo);
				}
			}
		}
		else if(tagB_name == null && tagA_name != null && textB_TagAValue.getText() != null)
		{
			tagA = new Tag(tagA_name,textB_TagAValue.getText());
			for(Photo photo : photos)
			{
				if(photo.getTags().contains(tagA))
				{
					toDisplay.add(photo);
				}
			}
		}
		
		return toDisplay;
	}
	
	/**
	 * Handle for the search action event
	 * @param event ActionEvent
	 */
	public void handleSearchButtonAction(ActionEvent event) {

		tileP_photos.getChildren().clear();
		LocalDate start = datePicker_start.getValue();
		LocalDate end = datePicker_end.getValue();
		
		ArrayList<Photo> toDisplay;
		toDisplay = searchByTags();
		if(error)
		{
			return;
		}
		if(start == null && end == null)
		{
			
			if(toDisplay != null)
			{
				
				tileP_photos.setAlignment(Pos.CENTER);
				tileP_photos.setHgap(3);
				tileP_photos.setVgap(3);
				
				for (Photo photo : toDisplay) {
					ImageView imageView = photo.getThumbnail();
					//addListenersToThumbnail(imageView);
					tileP_photos.getChildren().add(imageView);	
				}
			}
			else
			{
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Error");
				errAlert.setHeaderText(null);
				errAlert.setContentText("No photos to Display!");
				errAlert.show();
			}
			
		}
		else 
		{
			LocalDate date;
			if(start != null && end == null)
			{
				if(!toDisplay.isEmpty())
				{
					for(Photo photo : toDisplay)
					{
						
						date = photo.getDate();
						System.out.println(date + " " + start  + " " + end);
						if(date.compareTo(start) < 0)
						{
							toDisplay.remove(photo);
						}
					}
				}
				else
				{
					
					for(Photo photo : photos)
					{
						date = photo.getDate();
						if(date.compareTo(start) > 0)
						{
							toDisplay.add(photo);
						}
					}
				}
			
			}
			else if(end != null && start == null)
			{
				if(!toDisplay.isEmpty())
				{
					for(Photo photo : toDisplay)
					{
						date = photo.getDate();
						if(date.compareTo(end) > 0)
						{
							toDisplay.remove(photo);
						}
					}
				}
				else
				{
					for(Photo photo : photos)
					{
						date = photo.getDate();
						if(date.compareTo(end) < 0)
						{
							toDisplay.add(photo);
						}
					}
				}
			
			}
			else
			{
				if(!toDisplay.isEmpty())
				{
					
					for(Photo photo : toDisplay)
					{
						date = photo.getDate();
						if(date.compareTo(start) < 0 || date.compareTo(end) > 0)
						{
							
							toDisplay.remove(photo);
						}
					}
				}
				else
				{
					for(Photo photo : photos)
					{
						date = photo.getDate();
						if(date.compareTo(start) > 0 || date.compareTo(end) < 0)
						{
							toDisplay.add(photo);
						}
					}
				}
			}
			
			if(!toDisplay.isEmpty())
			{
				
				tileP_photos.setAlignment(Pos.CENTER);
				tileP_photos.setHgap(3);
				tileP_photos.setVgap(3);
				
				for (Photo photo : toDisplay) {
					ImageView imageView = photo.getThumbnail();
					tileP_photos.getChildren().add(imageView);	
				}
			}
			else
			{
				Alert errAlert = new Alert(AlertType.ERROR);
				errAlert.setTitle("Error");
				errAlert.setHeaderText(null);
				errAlert.setContentText("No photos to Display!");
				errAlert.show();
			}

		}

	}
	
	/**
	 * Handle for the create album action event
	 * @param event ActionEvent
	 */
	public void handleCreateAlbumButtonAction(ActionEvent event) {
		
		if(toDisplay == null || toDisplay.isEmpty())
		{
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("Error");
			errAlert.setHeaderText(null);
			errAlert.setContentText("No photos to create a new album!");
			errAlert.show();
		}
		else {
			boolean failed = false;
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("New Album");
			dialog.setHeaderText(null);
			dialog.setContentText("Please enter a new name for the album");
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent())
			{
				String albumName = dialog.getResult();
				
				for(Album a: album.getOwner().getAlbums())
				{
					
					if(a.getName().equals(albumName))
					{
						failed = true;
						Alert errAlert = new Alert(AlertType.ERROR);
						errAlert.setTitle("Error");
						errAlert.setHeaderText(null);
						errAlert.setContentText("Album Already Exists!");
						errAlert.show();
						break;
					}
				}
				// Create and Add the album 
				if(!failed)
				{
					ArrayList<Album> albums = album.getOwner().getAlbums();
					Album newAlbum = new Album(albumName,album.getOwner(),toDisplay);
					albums.add(newAlbum);
					//album.getOwner().setAlbums(albums);
					
					Alert successAlert = new Alert(AlertType.CONFIRMATION);
					successAlert.setTitle("Error");
					successAlert.setHeaderText(null);
					successAlert.setContentText("Successfully created a new Album!");
					successAlert.show();	
				}
			}
		}
	}
	
	/**
	 * Handle for the radio action event
	 * @param event ActionEvent
	 */
	public void handleANDRadioButtonAction(ActionEvent event) {
		
	}
	
	/**
	 * Handle for the radio action event
	 * @param event ActionEvent
	 */
	public void handleORRadioButtonAction(ActionEvent event) {
		
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
	 * Set the previous scene
	 * @param scene Scene element
	 */
	public void setPreviousScene(Scene scene) {
		if (scene != null) {
			previousScene = scene;
		}
	}
}
