package sms.example.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class PopSMSActivity extends Activity {
	
	/** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_main);
	       
	       Button btn_show = (Button) findViewById(R.id.Popup);
	       btn_show.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View arg0) {
	     
	           //Open popup window
	           	showPopup(PopSMSActivity.this);
	         }
	       });
	   }
	   private void showPopup(final Activity context) {
		// retrieve Serializable sms message object
	       // by the key "msg" used to pass it	       
	       Intent in = this.getIntent();
	       PopMessage msg = (PopMessage) in.getSerializableExtra("msg");

	       // Case where we launch the app to test the UI
	       // i.e. no incoming SMS
	       if(msg == null){
		     msg = new PopMessage();
		     msg.setSender("0123456789");
			 msg.setTimestamp( System.currentTimeMillis() );
			 msg.setBody(" this is a test SMS message!");
	       }
	    // start a new task before dying
	        Intent i = new Intent(context,smsPopup.class);
		    i.setClass(context, smsPopup.class);
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		    // pass Serializable object
		    i.putExtra("msg", msg);

		    // start UI
		    context.startActivity(i);

		    // keep this broadcast to ourselves
		    //abortBroadcast();
		    
		    i.setClass(context, smsPopup.class);
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    i.putExtra("msg", msg);
		    context.startService(i);
	       
		  
		}
	
}
