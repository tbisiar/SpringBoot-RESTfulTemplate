
package com.tbis.model;

import java.io.Serializable;
import javax.persistence.*;
import org.joda.time.DateTime;

/*
 * A simple class to allow organization of tide data by time and height
 */
@Entity
public class TideHeight implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
	private DateTime time;
    @Column(nullable = false)
	private int height;
	
    // JPA required //
    protected TideHeight() {    	
    }
    
    public TideHeight(DateTime time, int height) {
    	this.time = time;
    	this.height = height;
    }
    
	public DateTime getTime() {
		return time;
	}
	
	public void setTime(DateTime time) {
		this.time = time;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}