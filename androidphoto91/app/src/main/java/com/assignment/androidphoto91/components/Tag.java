package com.assignment.androidphoto91.components;

import java.util.Locale;

public class Tag {
    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean contains(Tag tag)
    {
        if(tag.getName().equals(this.name))
        {
            String val = tag.value.toLowerCase();
            String valueHere = value.toLowerCase();
            if(valueHere.startsWith(val))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object other) {
        if (other  != null && (other instanceof Tag)) {
            Tag otherTag = (Tag) other;
            return (this.name.toLowerCase().equals(otherTag.getName().toLowerCase()) && this.value.toLowerCase().equals(otherTag.getValue().toLowerCase()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format(this.name + ": " + this.value);
    }
}
