package com.brogrammers.agora.model;

import java.io.Serializable;
import java.util.Date;

import com.brogrammers.agora.data.DeviceUser;
/**
 * Comment data model class. Contains information on comment body, author and date.
 * @author Group02
 *
 */
public class Comment implements Serializable {
	
	private static final long serialVersionUID = 5525876100406087372L;
	private Long date;
	private String body;
	private Author author;
	private boolean posted;
	private SimpleLocation location;
	private String locationName;

	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	/**
	 * Comment constructor from a string. 
	 * @param text Text will be set as the body.
	 */
	public Comment(String text) {
		body = text;
		author = DeviceUser.getUser();
		date = System.currentTimeMillis();
	}
	
	// for testing: add different author
	/**
	 * Test method for testing different authors.
	 * @param text Text will be set as the body.
	 * @param author Text will be set as the author's username.
	 */
	public Comment(String text, Author author) {
		body = text;
		this.author = author;  
		date = System.currentTimeMillis();
	}
	
	/**
	 * Comment constructor for comments with locations 
	 * @param text body text
	 * @param location location object contain coordinates
	 * @param locationName simple text name of the location
	 */
	public Comment(String text,SimpleLocation location, String locationName) {
		this.location = location;
		this.locationName = locationName;
		author = DeviceUser.getUser();
		body = text;
		date = System.currentTimeMillis();	}

	public void setLocation(SimpleLocation location){
		this.location = location;
	}
	
	public void setLocationName(String locationName){
		this.locationName = locationName;
	}
	
	public SimpleLocation getLocation(){
		if (this.location != null){
            return location;
		} else {
			return null;
		}
	}

    public String getLocationName(){
		if (this.locationName != null){
            return locationName;
		} else {
			return null;
		}
	}
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
}
