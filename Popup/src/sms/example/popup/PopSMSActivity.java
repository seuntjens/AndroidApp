package sms.example.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class PopSMSActivity extends Activity {
	
	/** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);

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
	        showDialog( msg);
	       
	   }
	   private void showdialog(final PopMessage msg){
		   final String sender = quickCallerId(msg.getSender());
		   final String body = msg.getBody();
		   final String date = msg.getShortDate( msg.getTimestamp() );
		   
		   //toewijzen van de views aan lokale variabelen
		   TextView message = (TextView) findViewById(R.id.messageText);
		   TextView dateText = (TextView) findViewById(R.id.dateText);
		   TextView phone = (TextView) findViewById(R.id.phoneText);
		   QuickContactBadge quickBadge = (QuickContactBadge) findViewById(R.id.quickBadge1);
		   
		   message.setText(body);	
		   phone.setText(sender);
		   dateText.setText(date);
		 //trage manier van het opzoeken van contact photo, werkt momenteel ook niet
		   //quickBadge.assignContactFromPhone(msg.getSender(), false);

		   
		   Button cancel = (Button) findViewById(R.id.cancelButton);
		   cancel.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				goHome();				
			}
		   });
		   
		   Button reply = (Button) findViewById(R.id.replyButton);
		   reply.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				smsReply(msg.getSender(),body);				
			}
		   });
		   AlertDialog.Builder builder;
		   AlertDialog alertDialog;
		    
		
		   Context mContext = this;
		   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		   View layout = inflater.inflate(R.layout.activity_smsreceiver, null);
		   
		   builder = new AlertDialog.Builder(mContext);
		  // builder.setView(layout);
		   builder.setView(reply);
		   
		   alertDialog = builder.create();
		    
		   alertDialog.show();
	   }

	   //telefoonnummer opzoeken in contacten
	   private String quickCallerId(String phoneNumber){
		    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		    ContentResolver resolver=getContentResolver();
		    Cursor cur = resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		    if(cur!=null&&cur.moveToFirst()){
		            String value=cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		            if(value!=null){ 
		                cur.close();
		                return value;
		            }
		    }
		    cur.close();
		    return phoneNumber;
		}

	private void smsReply(String sender, String body){
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("address", sender);
			sendIntent.putExtra("sms_body", "");
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			this.finish(); // close this Activity now
}
	   private void goHome(){
		   	Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(intent);
		    android.os.Process.killProcess(android.os.Process.myPid()); //deze laatste twee lijnen zorgen ervoor dat de applicatie geforceerd wordt te stoppen
		    super.onDestroy();
		    //this.finish(); dit zorgt er voor dat je naar homescreen gaat zonder het ganse programma te sluiten		    
	}
	   
	   /***/
	  private void showDialog(PopMessage msg){

	   	final String sender = quickCallerId(msg.getSender());
	   	final String body = msg.getBody();

	   	final String display = sender + "\n"
	               + msg.getShortDate( msg.getTimestamp() )+ "\n"
	               + body + "\n";

	       // Display in Alert Dialog
	   	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   	builder.setMessage(display)
	   	.setCancelable(false)
	   	.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
	   		public void onClick(DialogInterface dialog, int id) {
	                     // reply by calling SMS program
	   		      smsReply(sender, body);
	   		}
	   	})
	   	.setNegativeButton("Close", new DialogInterface.OnClickListener() {
	   		public void onClick(DialogInterface dialog, int id) {
	                     // go back to the phone home screen
	   	              goHome();
	   		}
	   	});
	   	AlertDialog alert = builder.create();
	   	alert.show();
	   }
}
