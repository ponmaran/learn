package com.example.myfirstapp;

import java.util.Date;

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

//		System.out.println("Actual dialed number from URI " + uriData + " Lenght: " + String.valueOf(uriData.length()));

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

        String numSeq = new String();
        
//		Phone number type identification
        if (dialedNumber.substring(0, 2).equals("+1")){
    		//System.out.println(dialedNumber + " is US Number");
    		numSeq = "tel:"+ dialedNumber;
    	}
    	else if ( dialedNumber.substring(0,3).equals("011")){
    		//System.out.println(dialedNumber + " is non US with 011");
    		numSeq = nonUsNumSeqBuild(dialedNumber);

    	}
    	else if (dialedNumber.substring(0,1).equals("+")){
    		//System.out.println(dialedNumber + " is non US with +");
    		String dialedNumberUpdt ="011" + dialedNumber.substring(1);
    		numSeq = nonUsNumSeqBuild(dialedNumberUpdt);

    	}
    	else {
    		//System.out.println(dialedNumber + " is US number w/o country code");
    		numSeq = "tel:" + dialedNumber;
    	};

        System.out.println("Number Sequence: " + numSeq);
        
        Uri number = Uri.parse(numSeq);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        startActivity(callIntent);
        
        if (!numSeq.equals("tel:" + dialedNumber)){
            
            EndCallListener callListener = new EndCallListener();
            callListener.dialedNumber = dialedNumber;
            callListener.bridgeNumber = PhoneNumberUtils.extractNetworkPortion(numSeq);
            TelephonyManager telPhMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            telPhMgr.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
//        	System.out.println("Started Listening");
        	
        }
        
    	//System.out.println("Call Initiated");
    }

    private String nonUsNumSeqBuild(String dialedNum){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        String bridgeNumSeq = sharedPref.getString(getString(R.string.bridgeNum), getString(R.string.bridge_default_value));
        String delayTimeSeq = sharedPref.getString(getString(R.string.delayTime), getString(R.string.delay_default_value));

        String[] bridgeNum = bridgeNumSeq.split(":");
        String[] delayTime = delayTimeSeq.split(":", bridgeNum.length);
        
        int delayTimeNum;
        String pauses = new String();
        String numSeq = "tel:";
        for(int i = 0; i<bridgeNum.length ; i++){
        	delayTimeNum = delayTime[i].equals("")? 0 : Integer.parseInt(delayTime[i]);
        	pauses = "";
            for(int j=0; j < delayTimeNum ;j += 2)
            {
            	pauses = pauses + ",";
            }
        	numSeq = numSeq + bridgeNum[i] + pauses;
        }
        return numSeq + dialedNum;
/*        if (bridgeNum.equals(getString(R.string.bridge_default_value))){
        	return "tel:" + dialedNum;
        }
        else{
            return "tel:" + bridgeNum + pauses + dialedNum;
        }*/
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_call_bridge, menu);
        return true;
    }

    private class EndCallListener extends PhoneStateListener{
    	
    	boolean activeCallInd = false;
    	int secCallInd = 0;
    	String dialedNumber, bridgeNumber, curLogNum;
    	long lastCallDte = 0, offHookTime = 0;
    	
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		
    		Date dte = new Date();
//    		System.out.println("First State change @ " + String.valueOf(dte.getTime()));
    		Cursor c = null;
    		String[] fields = {
		    android.provider.CallLog.Calls.NUMBER,
		    android.provider.CallLog.Calls.DURATION,
		    android.provider.CallLog.Calls.DATE,
		    };
    		String order = android.provider.CallLog.Calls.DATE + " DESC"; 

    		switch (state){
    		case TelephonyManager.CALL_STATE_RINGING:
            	//System.out.println("Phone Ringing for: " + incomingNumber);
            	break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			activeCallInd = true;
//    			secCallInd++;
    			offHookTime = dte.getTime();

/*This is to offset the offHookTime to 2s ahead in case this call is initiated when another is active.
 * In this scenario, offHook happens before call. Only less than a sec ahead. This offset will move offHookTime after call time.
*/    			if(secCallInd == 0){
					System.out.println("Second Call");
    				offHookTime += 2000;
    			}
    			
        		c = getContentResolver().query(
        				android.provider.CallLog.Calls.CONTENT_URI,
        				fields,
        				null,
	        		    null,
	        		    order
	        		    );
        		
        		if(c.moveToFirst()){
        			lastCallDte = Long.parseLong(c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DATE)));
        		}

            	System.out.println("Phone OFFHOOK; Number: \"" + incomingNumber + "\"" 
            	+ " :: Off Hook Time: \"" + String.valueOf(offHookTime) +"\""
            	+ " :: Last Call Time: \"" + String.valueOf(lastCallDte) + "\"");
            	break;
    		case TelephonyManager.CALL_STATE_IDLE:
//            	System.out.println("Phone Idle; Number: \"" + dialedNumber + "\"");
            	if (activeCallInd == true ){
            		((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).listen(this, LISTEN_NONE); 
            		System.out.println("After call idle state :: Number: \"" + dialedNumber + "\"");

            		try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("Sleep interrupted :@");
						e.printStackTrace();
					}

            		c = getContentResolver().query(
            				android.provider.CallLog.Calls.CONTENT_URI,
            				fields,
		        		    android.provider.CallLog.Calls.DATE + " > " + this.lastCallDte,
//            				null,
		        		    null,
		        		    order
		        		    );

/*This is to hold until the log is updated
 * Since sleep() is added, this becomes unnecessary            		
            		System.out.println("Loop Started @ " + String.valueOf(dte.getTime()));
            		while(c.getCount() == 0){
            			c.requery();
            		}
            		System.out.println("Loop ended @ " + String.valueOf(dte.getTime()));
*/            		
	        		if(c.moveToFirst()){
	        			String callDate = new String(), callDuration = new String();
	        			curLogNum = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
	        			callDate = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DATE));
	        			callDuration = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DURATION));
	        			if (!curLogNum.equals(bridgeNumber)){
		        			do{
			        			curLogNum = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
			        			callDate = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DATE));
			        			callDuration = c.getString(c.getColumnIndex(android.provider.CallLog.Calls.DURATION));
			        			if (curLogNum.equals(bridgeNumber) && Long.parseLong(callDate) < offHookTime){
			        				break;
			        			}
		        				System.out.println("Cursor @ " + curLogNum);
		        			}
		        			while (c.moveToNext());
	        			}
	        			System.out.println("lastCallDte: " + lastCallDte);
	        			System.out.println("Call Date  : " + callDate);
	        			System.out.println("offHookTime: " + offHookTime);
	        			
	        			if (curLogNum.equals(bridgeNumber)){
	        				ContentValues valueSet = new ContentValues();
	    	    	    	valueSet.put(android.provider.CallLog.Calls.NUMBER, this.dialedNumber);
	    	    	    	int result = getContentResolver().update(
	    	    	    			android.provider.CallLog.Calls.CONTENT_URI, 
	    	    	    			valueSet, 
	    	    	    			android.provider.CallLog.Calls.NUMBER + "=" + bridgeNumber
	    	    	    			+ " and " + android.provider.CallLog.Calls.DATE + " < " + offHookTime
	    	    	    			+ " and " + android.provider.CallLog.Calls.DATE + " > " + this.lastCallDte
	    	    	    			+ " and " + android.provider.CallLog.Calls.DURATION + "=" + callDuration 
	    	    	    			,null);
		        			System.out.println("Rows updated: " + result);
	        			}
	        		};
            	}
            	else{
            		System.out.println("Before call idle state :: Date: " + String.valueOf(dte.getTime()));
            		secCallInd++;
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