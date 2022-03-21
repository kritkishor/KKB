package photo.components;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class defines a user
 * @author mtr103
 * @author kkb62
 */
public class User implements Comparable<User>, Serializable {

	/** Specify the serialization characteristic */
	private static final long serialVersionUID = 1L;
	
	/** Store the list of albums of a user */
	private ArrayList<Album> albums;
	
	/** Store the name of a user */
	private String name;
	
	/** Store the permission level of a user */
	private int type; // 0 for user 1 for admin
	
	/**
	 * Constructor for the user
	 * @param name Name of the user
	 * @param type Type of access
	 */
	public User(String name, int type) {
		this(name, type, new ArrayList<Album>());
	}
	
	/**
	 * Constructor for the user
	 * @param name Name of the user
	 * @param type Type of access
	 * @param albums List of albums owned by user
	 */
	public User(String name, int type, ArrayList<Album> albums) {
		this.name = name;
		this.type = type;
		this.albums = albums;
	}

	/**
	 * Get the list of albums owned by the user
	 * @return The list of albums
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	/**
	 * Set the list of albums for the user
	 * @param albums List of albums
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}

	/**
	 * Get the name of the user
	 * @return The name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the user
	 * @param name Name of the user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the type of access of the user
	 * @return The user level
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the access type of a user
	 * @param type The access type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Add an album to the user's album list
	 * @param album The album
	 */
	public void addAlbum(Album album) {
		this.albums.add(album);
	}

	/**
	 * Allows a user to be converted to a string
	 * @return The name of the user
	 */
	@Override
    public String toString() {
        return String.format(this.name);
    }

	/**
	 * Determine the equality of two users
	 * @param o A user object
	 * @return Whether an album is less than, equal or greater than another user
	 */
	@Override
	public int compareTo(User o) {
		int userComparison = name.toLowerCase().compareTo(o.name.toLowerCase());
		return userComparison;
	}
	
}
