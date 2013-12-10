package sms.example.popup;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub //sms onderscheppingscode komt hier
		
		// get the Bundle map from the Intent parameter to onReceive()
		Bundle bundle = intent.getExtras(); 

		// get the SMS received
		Object[] pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] msgs = new SmsMessage[pdus.length];
		
		/** sms sender phone*/
		String smsSender ="";

		/** body of received sms*/
		String smsBody = "";

		/** timestamp */
		long timestamp = 0L;

		for (int i=0; i<msgs.length; i++){
		      msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
		      smsSender += msgs[i].getOriginatingAddress();
		      smsBody += msgs[i].getMessageBody().toString();
		      timestamp += msgs[i].getTimestampMillis();
		}
		   // construct a PopMessage object
		   PopMessage pop_msg = new PopMessage();
		    // populate with timestamp, SMS sender & body using the setters here
		    pop_msg.setSender(smsSender);
		    pop_msg.setBody(smsBody);
		    pop_msg.setTimestamp(timestamp);
		   	   
		   
		    //...

		    // start a new task before dying
		    intent.setClass(context, smsPopup.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		    // pass Serializable object
		    intent.putExtra("msg", pop_msg);

		    // start UI
		    context.startActivity(intent);

		    // keep this broadcast to ourselves
		    //abortBroadcast();
		    
		    intent.setClass(context, smsPopup.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.putExtra("msg", pop_msg);
		    context.startService(intent);
	
	}

}
