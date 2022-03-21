package photo.components;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class defines an album
 * @author mtr103
 */
public class Album implements Serializable {

	/** Specify the serialization characteristic */
	private static final long serialVersionUID = 1L;
	
	/** Store the name of an album */
	private String name;
	
	/** Store the owner of an album */
	private User owner;
	
	/** Store the list of photos of an album */
	private ArrayList<Photo> photos;
	
	/**
	 * Constructor for the Album
	 * @param name Name of an album
	 * @param owner Owner of an album
	 */
	public Album(String name, User owner) {
		this(name, owner, new ArrayList<Photo>());
	}
	
	/**
	 * Constructor for the Album
	 * @param name Name of an album
	 * @param owner Owner of an album
	 * @param photos An array of Photos that make up an album
	 */
	public Album(String name, User owner, ArrayList<Photo> photos) {
		this.name = name;
		this.owner = owner;
		this.photos = photos;
	}
	
	/**
	 * Get the name of an album
	 * @return The album name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of an album
	 * @param name Name of an album
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the list of photos of an album
	 * @return The list of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	/**
	 * Set a list of photos to an album
	 * @param photos An array of Photos that make up an album
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Add a photo to an album
	 * @param photo A photo to be added to an album
	 */
	public void addPhoto(Photo photo) {
		photos.add(photo);
	}
	
	/**
	 * Remove a photo from an album
	 * @param photo A photo to be remove from an album
	 */
	public void removePhoto(Photo photo) {
		photos.remove(photo);
	}
	
	/**
	 * Get the owner of an album
	 * @return The user of an album
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * Set the owner of an album
	 * @param owner An owner of the album
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Determine the equality of two albums
	 * @param other An album object
	 * @return Whether an album equals another album
	 */
	@Override
	public boolean equals(Object other) {
		Album otherAlbum = (Album) other;
		if (otherAlbum != null) {
			return ( this.name.toLowerCase().equals(otherAlbum.getName().toLowerCase()) && this.owner.equals(otherAlbum.getOwner()) );	
		}
		
		return false;
	}
	
	/**
	 * Allows an album to be converted to a string
	 * @return The name of the album
	 */
	@Override
    public String toString() {
        return String.format(this.name);
    }
}
