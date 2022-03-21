package photo.components;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class defines a photo
 * @author mtr103
 */
public class Photo implements Serializable {

	/** Specify the serialization characteristic */
	private static final long serialVersionUID = 1L;
	
	/** Specify the name of a photo */
	private String name;
	
	/** Specify the location of a photo */
	private String path;
	
	/** Specify the caption of a photo */
	private String caption;
	
	/** Specify the date of a photo */
	private LocalDate date;
	
	/** Specify the tags of a photo */
	private ArrayList<Tag> tags;
	
	/** Specify the owner of a photo */
	private Album owner;
	
	/**
	 * Constructor for the photo
	 * @param name Name of a photo
	 * @param path Path location of a photo
	 * @param caption Caption of a photo
	 * @param owner Owner of a photo
	 */
	public Photo(String name, String path, String caption, Album owner) {
		this(name, path, caption, LocalDate.now(), new ArrayList<Tag>(), owner);
	}
	
	/**
	 * Constructor for the photo
	 * @param name Name of a photo
	 * @param path Path location of a photo
	 * @param caption Caption of a photo
	 * @param tags List of tags of a photo
	 * @param owner Owner of a photo
	 */
	public Photo(String name, String path, String caption, ArrayList<Tag> tags, Album owner) {
		this(name, path, caption, LocalDate.now(), tags, owner);
	}
	
	/**
	 * Constructor for the photo
	 * @param name Name of a photo
	 * @param path Path location of a photo
	 * @param caption Caption of a photo
	 * @param date LocalDate of a photo
	 * @param tags List of tags of a photo
	 * @param owner Owner of a photo
	 */
	public Photo(String name, String path, String caption, LocalDate date, ArrayList<Tag> tags, Album owner) {		
		this.name = name;
		this.path = path;
		this.caption = caption;
		this.date = date;
		this.tags = tags;
		this.owner = owner;
	}

	/**
	 * Get the name of a photo
	 * @return The photo name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of a photo
	 * @param name Name of a photo
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the path of a photo
	 * @return The path name
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Set the path of a photo
	 * @param path Path of a photo
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Get the caption of a photo
	 * @return The caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Set the caption of a photo
	 * @param caption Caption of a photo
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Get the date of a photo
	 * @return The date
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/**
	 * Set the date of a photo
	 * @param date Date of a photo
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	/**
	 * Get the tags of a photo
	 * @return The tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * Set the tags of a photo
	 * @param tags Tags of a photo
	 */
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	
	/**
	 * Add a tag to a photo
	 * @param tag Tag of a photo
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	/**
	 * Remove the tag of a photo
	 * @param tag Tag of a photo
	 */
	public void removeTag(Tag tag) {
		this.tags.remove(tag);
	}
	
	/**
	 * Get the owner of a photo
	 * @return The owner name
	 */
	public Album getOwner() {
		return owner;
	}

	/**
	 * Set the owner of a photo
	 * @param owner Owner of a photo
	 */
	public void setOwner(Album owner) {
		this.owner = owner;
	}

	/**
	 * Get the thumb nail of a photo
	 * @return The thumb nail
	 */
	public ImageView getThumbnail() {
		File file = new File(this.path + this.name);
		Image image = new Image(file.toURI().toString());
		
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		imageView.setId(this.name);

		return imageView;
	}
	
	/**
	 * Get the image of a photo
	 * @return The image
	 */
	public Image getImage() {
		File file = new File(this.path + this.name);
		Image image = new Image(file.toURI().toString());
		return image;
	}
}
