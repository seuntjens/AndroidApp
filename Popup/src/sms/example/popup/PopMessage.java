package sms.example.popup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;

import android.R.string;
import android.text.format.DateFormat;

public class PopMessage implements Serializable {

    private String sender;
    private String body;
    private long timestamp;

    // getters and setters go here
    public String setPhone(String sender) {
    	return sender;
    }
    
    public void setTime(long timestamp) {
    
    	
    }
    
    public void setBody(String body) {
    	
    }
    // ...

   /**
     * Utility method
     * Display a shorten, more user-friendly readable date from the original timestamp
     */
   public String getShortDate(long timestamp){
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
	  SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd hh:mmaa");
	  sdf.setCalendar(cal);
	  cal.setTime(date);
	  return sdf.format(date);
   }




}