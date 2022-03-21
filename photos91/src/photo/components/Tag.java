package photo.components;

import java.io.Serializable;

/**
 * This class defines a tag
 * @author mtr103
 */
public class Tag implements Serializable {

	/** Specify the serialization characteristic */
	private static final long serialVersionUID = 1L;
	
	/** Specify the name of a tag */
	private String name;
	
	/** Specify the value of a tag */
	private String value;
	
	/**
	 * Constructor for the tag
	 * @param name Name of a tag
	 * @param value Value of a tag
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Get the name of a tag
	 * @return The tag name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of a tag
	 * @param name Name of a tag
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of a tag
	 * @return The tag value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value of a tag
	 * @param value Value of a tag
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Determine the equality of two tags
	 * @param other A tag object
	 * @return Whether a tag equals another tag
	 */
	@Override
	public boolean equals(Object other) {
		if (other  != null) {
			Tag otherTag = (Tag) other;
			return (this.name.toLowerCase().equals(otherTag.getName().toLowerCase()) && this.value.toLowerCase().equals(otherTag.getValue().toLowerCase()));
		} else {
			return false;
		}
	}
	
	/**
	 * Allows a tag to be converted to a string
	 * @return The name and value pair of the tag
	 */
	@Override
    public String toString() {
        return String.format(this.name + ": " + this.value);
    }
}
