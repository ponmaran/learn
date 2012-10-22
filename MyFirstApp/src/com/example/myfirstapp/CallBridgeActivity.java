package com.example.myfirstapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;

public class CallBridgeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call_bridge);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        String uriData = new String();
        
        uriData = getIntent().getDataString();
        System.out.println("*************");
        System.out.println("Actual dialed number from URI " + uriData + " Lenght: " + String.valueOf(uriData.length()));

        String uriDataCorr = new String();

        if (uriData.contains("%2B")){
            uriDataCorr = uriData.replaceAll("%2B", "+");
        };

        String dialedNumber = new String();
        if (uriDataCorr.equals("")){
            dialedNumber = PhoneNumberUtils.extractNetworkPortion(uriData);
        }
        else{
            dialedNumber = PhoneNumberUtils.extractNetworkPortion(uriDataCorr);
        };
        System.out.println("After replace " + dialedNumber + " Lenght: " + String.valueOf(dialedNumber.length()));

        String numSeq = new String();
        
//		Phone number type identification
        if (dialedNumber.substring(0, 2).equals("+1")){
    		System.out.println(dialedNumber + " is US Number");
    		numSeq = "tel:"+ dialedNumber;
    	}
    	else if ( dialedNumber.substring(0,3).equals("011")){
    		System.out.println(dialedNumber + " is non US with 011");
    		numSeq = nonUsNumSeqBuild(dialedNumber);
            
            EndCallListener callListener = new EndCallListener();
            callListener.dialedNumber = dialedNumber;
            callListener.bridgeNumber = PhoneNumberUtils.extractNetworkPortion(numSeq);
            TelephonyManager telPhMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            telPhMgr.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        	System.out.println("Started Listening");

    	}
    	else if (dialedNumber.substring(0,1).equals("+")){
    		System.out.println(dialedNumber + " is non US with +");
    		String dialedNumberUpdt ="011" + dialedNumber.substring(1);
    		numSeq = nonUsNumSeqBuild(dialedNumberUpdt);

    		EndCallListener callListener = new EndCallListener();
            callListener.dialedNumber = dialedNumber;
            callListener.bridgeNumber = PhoneNumberUtils.extractNetworkPortion(numSeq);
            TelephonyManager telPhMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            telPhMgr.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        	System.out.println("Started Listening");

    	}
    	else {
    		System.out.println(dialedNumber + " is US number w/o country code");
    		numSeq = "tel:" + dialedNumber;
    	};

        System.out.println("Number Sequence: " + numSeq);
        
        Uri number = Uri.parse(numSeq);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        startActivity(callIntent);
        
    	System.out.println("Call Initiated");
    }

    private String nonUsNumSeqBuild(String dialedNum){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String bridgeNum = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String delayTime = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));

        int delayTimeNum = Integer.parseInt(delayTime);
       
        String pauses = new String();
        for(int i=0;i<delayTimeNum;i+=2)
        {
        	pauses = pauses + ",";
        }

        if (bridgeNum.equals(getString(R.string.bridge_default_value))){
        	return "tel:" + dialedNum;
        }
        else{
            return "tel:" + bridgeNum + pauses + dialedNum;
        }
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_call_bridge, menu);
        return true;
    }

    private class EndCallListener extends PhoneStateListener{
    	
    	boolean activeCallInd = false;
    	String dialedNumber, bridgeNumber, curLogNum;
    	
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {

    		switch (state){
    		case TelephonyManager.CALL_STATE_RINGING:
            	System.out.println("Phone Ringing for: " + incomingNumber);
            	break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			activeCallInd = true;
            	System.out.println("Phone OFFHOOK; Number: \"" + incomingNumber + "\"");
            	break;
    		case TelephonyManager.CALL_STATE_IDLE:
            	System.out.println("Phone Idle; Number: \"" + incomingNumber + "\"");
            	System.out.println(dialedNumber);
            	if (activeCallInd == true ){
            		((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).listen(this, LISTEN_NONE); 
            		System.out.println("After call idle state");
            		String[] fields = {
        		    android.provider.CallLog.Calls.NUMBER, 
        		    };
            		String order = android.provider.CallLog.Calls.DATE + " DESC"; 

            		Cursor c = getContentResolver().query(
            				
            				android.provider.CallLog.Calls.CONTENT_URI,
            				fields,
		        		    null,
		        		    null,
		        		    order
		        		    ); 

	        		if(c.moveToFirst()){
	        			do{
	    	    			curLogNum = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
	    	        		System.out.println("After-cursor " + curLogNum);
	        			}	
	        			while ((!curLogNum.equals(bridgeNumber)) && c.moveToNext());
	        			if (curLogNum.equals(bridgeNumber)){
	        				ContentValues valueSet = new ContentValues();
	    	    	    	valueSet.put(android.provider.CallLog.Calls.NUMBER, this.dialedNumber);
	    	    	    	int result = getContentResolver().update(
	    	    	    			android.provider.CallLog.Calls.CONTENT_URI, 
	    	    	    			valueSet, 
	    	    	    			android.provider.CallLog.Calls.NUMBER + "=" + bridgeNumber, 
	    	    	    			null);
		        			System.out.println(result);
	        			}
//	        	    	Toast.makeText(getApplicationContext(), "Updating call log" + String.valueOf(result), Toast.LENGTH_SHORT).show();
	        		};
            	}
            	else{
            		System.out.println("Before call idle state");
            	};
    		}
        }
    }
    
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}