package ponmaran.learn.BrRcvr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OutgoingBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "onReceive";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "phone number: " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
		Log.v(TAG, "Action : " + intent. getAction ());
		Log.v(TAG, "Action : " + intent. getAction ());
		Bundle extData = intent.getExtras();
		if (extData.size() > 0){
			Log.v(TAG, "Extras : " + extData.toString());			
		}
	/*    if (intent. getAction (). equals (Intent. ACTION_NEW_OUTGOING_CALL)) {
	// If it is to call (outgoing)
	    Intent i = new Intent(context, OutgoingCallScreenDisplay.class);
	    i.putExtras(intent);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	      }
	*/      
	    }
}